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

package com.mdosys.scheduler.server.worker.processor;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import com.mdosys.scheduler.common.enums.Event;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.common.utils.*;
import com.mdosys.scheduler.remote.command.Command;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.command.TaskExecuteAckCommand;
import com.mdosys.scheduler.remote.command.TaskExecuteRequestCommand;
import com.mdosys.scheduler.remote.processor.NettyRemoteChannel;
import com.mdosys.scheduler.remote.processor.NettyRequestProcessor;
import com.mdosys.scheduler.server.utils.LogUtils;
import com.mdosys.scheduler.server.worker.cache.ResponceCache;
import com.mdosys.scheduler.server.worker.config.WorkerConfig;
import com.mdosys.scheduler.server.worker.plugin.TaskPluginManager;
import com.mdosys.scheduler.server.worker.runner.TaskExecuteThread;
import com.mdosys.scheduler.server.worker.runner.WorkerManagerThread;
import com.mdosys.scheduler.service.alert.AlertClientService;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.queue.entity.TaskExecutionContext;
import com.mdosys.scheduler.spi.task.TaskExecutionContextCacheManager;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * worker request processor
 */
public class TaskExecuteProcessor implements NettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecuteProcessor.class);

    /**
     * worker config
     */
    private final WorkerConfig workerConfig;

    /**
     * task callback service
     */
    private final TaskCallbackService taskCallbackService;

    /**
     * alert client service
     */
    private AlertClientService alertClientService;

    private TaskPluginManager taskPluginManager;

    /*
     * task execute manager
     */
    private final WorkerManagerThread workerManager;

    public TaskExecuteProcessor() {
        this.taskCallbackService = SpringApplicationContext.getBean(TaskCallbackService.class);
        this.workerConfig = SpringApplicationContext.getBean(WorkerConfig.class);
        this.workerManager = SpringApplicationContext.getBean(WorkerManagerThread.class);
    }

    /**
     * Pre-cache task to avoid extreme situations when kill task. There is no such
     * task in the cache
     *
     * @param taskExecutionContext task
     */
    private void setTaskCache(TaskExecutionContext taskExecutionContext) {
        TaskExecutionContext preTaskCache = new TaskExecutionContext();
        preTaskCache.setTaskInstanceId(taskExecutionContext.getTaskInstanceId());
        TaskRequest taskRequest = JSONUtils.parseObject(JSONUtils.toJsonString(taskExecutionContext),
                TaskRequest.class);
        TaskExecutionContextCacheManager.cacheTaskExecutionContext(taskRequest);
    }

    public TaskExecuteProcessor(AlertClientService alertClientService, TaskPluginManager taskPluginManager) {
        this();
        this.alertClientService = alertClientService;
        this.taskPluginManager = taskPluginManager;
    }

    @Override
    public void process(Channel channel, Command command) {
        Preconditions.checkArgument(CommandType.TASK_EXECUTE_REQUEST == command.getType(),
                String.format("invalid command type : %s", command.getType()));

        TaskExecuteRequestCommand taskRequestCommand = JSONUtils.parseObject(
                command.getBody(), TaskExecuteRequestCommand.class);

        logger.info("received command : {}", taskRequestCommand);

        if (taskRequestCommand == null) {
            logger.error("task execute request command is null");
            return;
        }

        String contextJson = taskRequestCommand.getTaskExecutionContext();
        TaskExecutionContext taskExecutionContext = JSONUtils.parseObject(contextJson, TaskExecutionContext.class);

        if (taskExecutionContext == null) {
            logger.error("task execution context is null");
            return;
        }

        setTaskCache(taskExecutionContext);
        // todo custom logger

        taskExecutionContext.setHost(NetUtils.getAddr(workerConfig.getListenPort()));
        taskExecutionContext.setLogPath(LogUtils.getTaskLogPath(taskExecutionContext));

        // local execute path
        String execLocalPath = getExecLocalPath(taskExecutionContext);
        logger.info("task instance local execute path : {}", execLocalPath);
        taskExecutionContext.setExecutePath(execLocalPath);

        try {
            FileUtils.createWorkDirIfAbsent(execLocalPath);
            if (CommonUtils.isSudoEnable() && workerConfig.getWorkerTenantAutoCreate()) {
                OSUtils.createUserIfAbsent(taskExecutionContext.getTenantCode());
            }
        } catch (Throwable ex) {
            logger.error("create execLocalPath: {}", execLocalPath, ex);
            TaskExecutionContextCacheManager.removeByTaskInstanceId(taskExecutionContext.getTaskInstanceId());
        }

        taskCallbackService.addRemoteChannel(taskExecutionContext.getTaskInstanceId(),
                new NettyRemoteChannel(channel, command.getOpaque()));

        // delay task process
        long remainTime = DateUtils.getRemainTime(taskExecutionContext.getFirstSubmitTime(),
                taskExecutionContext.getDelayTime() * 60L);
        if (remainTime > 0) {
            logger.info("delay the execution of task instance {}, delay time: {} s",
                    taskExecutionContext.getTaskInstanceId(), remainTime);
            taskExecutionContext.setCurrentExecutionStatus(ExecutionStatus.DELAY_EXECUTION);
            taskExecutionContext.setStartTime(null);
        } else {
            taskExecutionContext.setCurrentExecutionStatus(ExecutionStatus.RUNNING_EXECUTION);
            taskExecutionContext.setStartTime(new Date());
        }

        this.doAck(taskExecutionContext);

        // submit task to manager
        if (!workerManager.offer(new TaskExecuteThread(taskExecutionContext, taskCallbackService, alertClientService,
                taskPluginManager))) {
            logger.info("submit task to manager error, queue is full, queue size is {}",
                    workerManager.getDelayQueueSize());
        }
    }

    private void doAck(TaskExecutionContext taskExecutionContext) {
        // tell master that task is in executing
        TaskExecuteAckCommand ackCommand = buildAckCommand(taskExecutionContext);
        ResponceCache.get().cache(taskExecutionContext.getTaskInstanceId(), ackCommand.convert2Command(), Event.ACK);
        taskCallbackService.sendAck(taskExecutionContext.getTaskInstanceId(), ackCommand.convert2Command());
    }

    /**
     * build ack command
     *
     * @param taskExecutionContext taskExecutionContext
     * @return TaskExecuteAckCommand
     */
    private TaskExecuteAckCommand buildAckCommand(TaskExecutionContext taskExecutionContext) {
        TaskExecuteAckCommand ackCommand = new TaskExecuteAckCommand();
        ackCommand.setTaskInstanceId(taskExecutionContext.getTaskInstanceId());
        ackCommand.setStatus(taskExecutionContext.getCurrentExecutionStatus().getCode());
        ackCommand.setLogPath(LogUtils.getTaskLogPath(taskExecutionContext));
        ackCommand.setHost(taskExecutionContext.getHost());
        ackCommand.setStartTime(taskExecutionContext.getStartTime());
        if (TaskType.SQL.getDesc().equalsIgnoreCase(taskExecutionContext.getTaskType())
                || TaskType.PROCEDURE.getDesc().equalsIgnoreCase(taskExecutionContext.getTaskType())) {
            ackCommand.setExecutePath(null);
        } else {
            ackCommand.setExecutePath(taskExecutionContext.getExecutePath());
        }
        taskExecutionContext.setLogPath(ackCommand.getLogPath());
        ackCommand.setProcessInstanceId(taskExecutionContext.getProcessInstanceId());

        return ackCommand;
    }

    /**
     * get execute local path
     *
     * @param taskExecutionContext taskExecutionContext
     * @return execute local path
     */
    private String getExecLocalPath(TaskExecutionContext taskExecutionContext) {
        return FileUtils.getProcessExecDir(taskExecutionContext.getProjectCode(),
                taskExecutionContext.getProcessDefineCode(),
                taskExecutionContext.getProcessDefineVersion(),
                taskExecutionContext.getProcessInstanceId(),
                taskExecutionContext.getTaskInstanceId());
    }
}
