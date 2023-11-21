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

package com.mdosys.scheduler.server.master.runner.task;

import org.apache.commons.lang.StringUtils;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.dao.entity.ProcessInstance;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.remote.command.TaskKillRequestCommand;
import com.mdosys.scheduler.remote.utils.Host;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.master.dispatch.context.ExecutionContext;
import com.mdosys.scheduler.server.master.dispatch.enums.ExecutorType;
import com.mdosys.scheduler.server.master.dispatch.exceptions.ExecuteException;
import com.mdosys.scheduler.server.master.dispatch.executor.NettyExecutorManager;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.queue.TaskPriority;
import com.mdosys.scheduler.service.queue.TaskPriorityQueue;
import com.mdosys.scheduler.service.queue.TaskPriorityQueueImpl;
import com.mdosys.scheduler.service.queue.entity.TaskExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * common task processor
 */
public class CommonTaskProcessor extends BaseTaskProcessor {

    @Autowired
    private TaskPriorityQueue taskUpdateQueue;

    @Autowired
    MasterConfig masterConfig;

    @Autowired
    NettyExecutorManager nettyExecutorManager = SpringApplicationContext.getBean(NettyExecutorManager.class);

    /**
     * logger of MasterBaseTaskExecThread
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean submit(TaskInstance task, ProcessInstance processInstance, int maxRetryTimes, int commitInterval) {
        this.processInstance = processInstance;
        this.taskInstance = processService.submitTask(task, maxRetryTimes, commitInterval);

        if (this.taskInstance == null) {
            return false;
        }
        return dispatchTask(taskInstance, processInstance);
    }

    @Override
    public ExecutionStatus taskState() {
        return this.taskInstance.getState();
    }

    @Override
    public void run() {
    }

    @Override
    protected boolean taskTimeout() {
        return true;
    }

    /**
     * common task cannot be paused
     */
    @Override
    protected boolean pauseTask() {
        return true;
    }

    @Override
    public String getType() {
        return Constants.COMMON_TASK_TYPE;
    }

    private boolean dispatchTask(TaskInstance taskInstance, ProcessInstance processInstance) {

        try {
            if (taskUpdateQueue == null) {
                this.initQueue();
            }
            if (taskInstance.getState().typeIsFinished()) {
                logger.info(String.format("submit task , but task [%s] state [%s] is already  finished. ",
                        taskInstance.getName(), taskInstance.getState().toString()));
                return true;
            }
            // task cannot be submitted because its execution state is RUNNING or DELAY.
            if (taskInstance.getState() == ExecutionStatus.RUNNING_EXECUTION
                    || taskInstance.getState() == ExecutionStatus.DELAY_EXECUTION) {
                logger.info("submit task, but the status of the task {} is already running or delayed.",
                        taskInstance.getName());
                return true;
            }
            logger.info("task ready to submit: {}", taskInstance);

            TaskPriority taskPriority = new TaskPriority(processInstance.getProcessInstancePriority().getCode(),
                    processInstance.getId(), taskInstance.getProcessInstancePriority().getCode(),
                    taskInstance.getId(), com.mdosys.scheduler.common.Constants.DEFAULT_WORKER_GROUP);

            TaskExecutionContext taskExecutionContext = getTaskExecutionContext(taskInstance);
            taskPriority.setTaskExecutionContext(taskExecutionContext);

            taskUpdateQueue.put(taskPriority);
            logger.info("master submit success, task id:{}, task name:{}, process id:{}",
                    taskInstance.getId(), taskInstance.getName(), taskInstance.getProcessInstanceId());
            return true;
        } catch (Exception e) {
            logger.error("submit task  Exception: ", e);
            logger.error("task error : {}", JSONUtils.toJsonString(taskInstance));
            return false;
        }
    }

    public void initQueue() {
        this.taskUpdateQueue = SpringApplicationContext.getBean(TaskPriorityQueueImpl.class);
    }

    @Override
    public boolean killTask() {

        try {
            taskInstance = processService.findTaskInstanceById(taskInstance.getId());
            if (taskInstance == null) {
                return true;
            }
            if (taskInstance.getState().typeIsFinished()) {
                return true;
            }
            if (StringUtils.isBlank(taskInstance.getHost())) {
                taskInstance.setState(ExecutionStatus.KILL);
                taskInstance.setEndTime(new Date());
                processService.updateTaskInstance(taskInstance);
                return true;
            }

            TaskKillRequestCommand killCommand = new TaskKillRequestCommand();
            killCommand.setTaskInstanceId(taskInstance.getId());

            ExecutionContext executionContext = new ExecutionContext(killCommand.convert2Command(),
                    ExecutorType.WORKER);

            Host host = Host.of(taskInstance.getHost());
            executionContext.setHost(host);

            nettyExecutorManager.executeDirectly(executionContext);
        } catch (ExecuteException e) {
            logger.error("kill task error:", e);
            return false;
        }

        logger.info("master kill taskInstance name :{} taskInstance id:{}",
                taskInstance.getName(), taskInstance.getId());
        return true;
    }
}
