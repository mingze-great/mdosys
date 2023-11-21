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

package com.mdosys.scheduler.server.worker;

import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.IStoppable;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.common.utils.PropertyUtils;
import com.mdosys.scheduler.remote.NettyRemotingServer;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.config.NettyServerConfig;
import com.mdosys.scheduler.server.worker.config.WorkerConfig;
import com.mdosys.scheduler.server.worker.plugin.TaskPluginManager;
import com.mdosys.scheduler.server.worker.processor.*;
import com.mdosys.scheduler.server.worker.registry.WorkerRegistryClient;
import com.mdosys.scheduler.server.worker.runner.RetryReportTaskStatusThread;
import com.mdosys.scheduler.server.worker.runner.WorkerManagerThread;
import com.mdosys.scheduler.service.alert.AlertClientService;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

import static com.mdosys.scheduler.common.Constants.SPRING_DATASOURCE_DRIVER_CLASS_NAME;

/**
 * worker server
 */
@ComponentScan(value = "com.mdosys.scheduler", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "com.mdosys.scheduler.server.master.*",
                "com.mdosys.scheduler.server.monitor.*",
                "com.mdosys.scheduler.server.log.*",
                "com.mdosys.scheduler.alert.*"
        })
})
@EnableTransactionManagement
public class WorkerServer implements IStoppable {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(WorkerServer.class);

    /**
     * netty remote server
     */
    private NettyRemotingServer nettyRemotingServer;

    /**
     * worker config
     */
    @Autowired
    private WorkerConfig workerConfig;

    /**
     * spring application context
     * only use it for initialization
     */
    @Autowired
    private SpringApplicationContext springApplicationContext;

    /**
     * alert model netty remote server
     */
    private AlertClientService alertClientService;

    @Autowired
    private RetryReportTaskStatusThread retryReportTaskStatusThread;

    @Autowired
    private WorkerManagerThread workerManagerThread;

    /**
     * worker registry
     */
    @Autowired
    private WorkerRegistryClient workerRegistryClient;

    @Autowired
    private TaskPluginManager taskPluginManager;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * worker server startup, not use web service
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Thread.currentThread().setName(Constants.THREAD_NAME_WORKER_SERVER);
        new SpringApplicationBuilder(WorkerServer.class)
                .web(WebApplicationType.NONE)
                .profiles("worker")
                .run(args);
    }

    /**
     * worker server run
     */
    @PostConstruct
    public void run() {
        PropertyUtils.setValue(SPRING_DATASOURCE_DRIVER_CLASS_NAME, driverClassName);

        // alert-server client registry
        alertClientService = new AlertClientService(workerConfig.getAlertListenHost(), Constants.ALERT_RPC_PORT);

        // init remoting server
        NettyServerConfig serverConfig = new NettyServerConfig();
        serverConfig.setListenPort(workerConfig.getListenPort());
        this.nettyRemotingServer = new NettyRemotingServer(serverConfig);
        this.nettyRemotingServer.registerProcessor(CommandType.TASK_EXECUTE_REQUEST,
                new TaskExecuteProcessor(alertClientService, taskPluginManager));
        this.nettyRemotingServer.registerProcessor(CommandType.TASK_KILL_REQUEST, new TaskKillProcessor());
        this.nettyRemotingServer.registerProcessor(CommandType.DB_TASK_ACK, new DBTaskAckProcessor());
        this.nettyRemotingServer.registerProcessor(CommandType.DB_TASK_RESPONSE, new DBTaskResponseProcessor());
        this.nettyRemotingServer.registerProcessor(CommandType.PROCESS_HOST_UPDATE_REQUEST, new HostUpdateProcessor());
        this.nettyRemotingServer.start();

        // worker registry
        try {
            this.workerRegistryClient.registry();
            this.workerRegistryClient.setRegistryStoppable(this);
        } catch (Exception e) {
            logger.error("worker registry error", e);
            throw new RuntimeException(e);
        }

        // solve dead lock
        logger.info(com.mdosys.scheduler.spi.utils.PropertyUtils.dumpProperties());

        // task execute manager
        this.workerManagerThread.start();

        // retry report task status
        this.retryReportTaskStatusThread.start();

        /*
         * registry hooks, which are called before the process exits
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Stopper.isRunning()) {
                close("shutdownHook");
            }
        }));
    }

    public void close(String cause) {
        try {
            // execute only once
            if (Stopper.isStopped()) {
                return;
            }

            logger.info("worker server is stopping ..., cause : {}", cause);

            // set stop signal is true
            Stopper.stop();

            try {
                // thread sleep 3 seconds for thread quitely stop
                Thread.sleep(3000L);
            } catch (Exception e) {
                logger.warn("thread sleep exception", e);
            }

            // close
            this.nettyRemotingServer.close();
            this.workerRegistryClient.unRegistry();
            this.alertClientService.close();
            this.springApplicationContext.close();
            logger.info("springApplicationContext close");
        } catch (Exception e) {
            logger.error("worker server stop exception ", e);
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
