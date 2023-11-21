/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mdosys.scheduler.server.master;

import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.IStoppable;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.common.utils.PropertyUtils;
import com.mdosys.scheduler.remote.NettyRemotingServer;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.config.NettyServerConfig;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.master.processor.StateEventProcessor;
import com.mdosys.scheduler.server.master.processor.TaskAckProcessor;
import com.mdosys.scheduler.server.master.processor.TaskKillResponseProcessor;
import com.mdosys.scheduler.server.master.processor.TaskResponseProcessor;
import com.mdosys.scheduler.server.master.registry.MasterRegistryClient;
import com.mdosys.scheduler.server.master.runner.EventExecuteService;
import com.mdosys.scheduler.server.master.runner.FailoverExecuteThread;
import com.mdosys.scheduler.server.master.runner.MasterSchedulerService;
import com.mdosys.scheduler.server.master.runner.WorkflowExecuteThread;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.quartz.QuartzExecutors;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ConcurrentHashMap;

import static com.mdosys.scheduler.common.Constants.SPRING_DATASOURCE_DRIVER_CLASS_NAME;

/**
 * master server
 */
@ComponentScan(value = "com.mdosys.scheduler", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "com.mdosys.scheduler.server.worker.*",
                "com.mdosys.scheduler.server.monitor.*",
                "com.mdosys.scheduler.server.log.*",
                "com.mdosys.scheduler.alert.*"
        })
})
@EnableTransactionManagement
public class MasterServer implements IStoppable {
    /**
     * logger of MasterServer
     */
    private static final Logger logger = LoggerFactory.getLogger(MasterServer.class);

    /**
     * master config
     */
    @Autowired
    private MasterConfig masterConfig;

    /**
     * spring application context
     * only use it for initialization
     */
    @Autowired
    private SpringApplicationContext springApplicationContext;

    /**
     * netty remote server
     */
    private NettyRemotingServer nettyRemotingServer;

    /**
     * zk master client
     */
    @Autowired
    private MasterRegistryClient masterRegistryClient;

    /**
     * scheduler service
     */
    @Autowired
    private MasterSchedulerService masterSchedulerService;

    @Autowired
    private EventExecuteService eventExecuteService;

    @Autowired
    private FailoverExecuteThread failoverExecuteThread;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    private ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceExecMaps = new ConcurrentHashMap<>();

    /**
     * master server startup, not use web service
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Thread.currentThread().setName(Constants.THREAD_NAME_MASTER_SERVER);
        new SpringApplicationBuilder(MasterServer.class).web(WebApplicationType.NONE).run(args);
    }

    /**
     * run master server
     */
    @EventListener
    public void run(ApplicationReadyEvent ignored) {
        PropertyUtils.setValue(SPRING_DATASOURCE_DRIVER_CLASS_NAME, driverClassName);

        // init remoting server
        NettyServerConfig serverConfig = new NettyServerConfig();
        serverConfig.setListenPort(masterConfig.getListenPort());
        this.nettyRemotingServer = new NettyRemotingServer(serverConfig);
        TaskAckProcessor ackProcessor = new TaskAckProcessor();
        ackProcessor.init(processInstanceExecMaps);
        TaskResponseProcessor taskResponseProcessor = new TaskResponseProcessor();
        taskResponseProcessor.init(processInstanceExecMaps);
        StateEventProcessor stateEventProcessor = new StateEventProcessor();
        stateEventProcessor.init(processInstanceExecMaps);
        this.nettyRemotingServer.registerProcessor(CommandType.TASK_EXECUTE_RESPONSE, taskResponseProcessor);
        this.nettyRemotingServer.registerProcessor(CommandType.TASK_EXECUTE_ACK, ackProcessor);
        this.nettyRemotingServer.registerProcessor(CommandType.TASK_KILL_RESPONSE, new TaskKillResponseProcessor());
        this.nettyRemotingServer.registerProcessor(CommandType.STATE_EVENT_REQUEST, stateEventProcessor);
        this.nettyRemotingServer.start();

        // self tolerant
        this.masterRegistryClient.init(this.processInstanceExecMaps);
        this.masterRegistryClient.setRegistryStoppable(this);
        this.masterRegistryClient.start();

        this.eventExecuteService.init(this.processInstanceExecMaps);
        this.eventExecuteService.start();
        // scheduler start
        this.masterSchedulerService.init(this.processInstanceExecMaps);

        this.masterSchedulerService.start();

        this.failoverExecuteThread.start();

        // start QuartzExecutors
        // what system should do if exception
        try {
            logger.info("start Quartz server...");
            QuartzExecutors.getInstance().start();
        } catch (Exception e) {
            try {
                QuartzExecutors.getInstance().shutdown();
            } catch (SchedulerException e1) {
                logger.error("QuartzExecutors shutdown failed : " + e1.getMessage(), e1);
            }
            logger.error("start Quartz failed", e);
        }

        /**
         * register hooks, which are called before the process exits
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Stopper.isRunning()) {
                close("shutdownHook");
            }
        }));

    }

    /**
     * gracefully close
     *
     * @param cause close cause
     */
    public void close(String cause) {

        try {
            // execute only once
            if (Stopper.isStopped()) {
                return;
            }

            logger.info("master server is stopping ..., cause : {}", cause);

            // set stop signal is true
            Stopper.stop();

            try {
                // thread sleep 3 seconds for thread quietly stop
                Thread.sleep(3000L);
            } catch (Exception e) {
                logger.warn("thread sleep exception ", e);
            }
            // close
            this.masterSchedulerService.close();
            this.nettyRemotingServer.close();
            this.masterRegistryClient.closeRegistry();
            // close quartz
            try {
                QuartzExecutors.getInstance().shutdown();
                logger.info("Quartz service stopped");
            } catch (Exception e) {
                logger.warn("Quartz service stopped exception:{}", e.getMessage());
            }
            // close spring Context and will invoke method with @PreDestroy annotation to
            // destory beans. like
            // ServerNodeManager,HostManager,TaskResponseService,CuratorZookeeperClient,etc
            springApplicationContext.close();
            logger.info("springApplicationContext close");
        } catch (Exception e) {
            logger.error("master server stop exception ", e);
        } finally {
            try {
                // thread sleep 60 seconds for quietly stop
                Thread.sleep(60000L);
            } catch (Exception e) {
                logger.warn("thread sleep exception ", e);
            }
            System.exit(1);
        }
    }

    @Override
    public void stop(String cause) {
        close(cause);
    }
}
