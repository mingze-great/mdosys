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

package com.mdosys.scheduler.server.master.consumer;

import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.master.dispatch.ExecutorDispatcher;
import com.mdosys.scheduler.server.master.dispatch.context.ExecutionContext;
import com.mdosys.scheduler.server.master.dispatch.enums.ExecutorType;
import com.mdosys.scheduler.server.master.dispatch.exceptions.ExecuteException;
import com.mdosys.scheduler.service.process.ProcessService;
import com.mdosys.scheduler.service.queue.TaskPriority;
import com.mdosys.scheduler.service.queue.TaskPriorityQueue;
import com.mdosys.scheduler.service.queue.entity.TaskExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * TaskUpdateQueue consumer
 */
@Component
public class TaskPriorityQueueConsumer extends Thread {

    /**
     * logger of TaskUpdateQueueConsumer
     */
    private static final Logger logger = LoggerFactory.getLogger(TaskPriorityQueueConsumer.class);

    /**
     * taskUpdateQueue
     */
    @Autowired
    private TaskPriorityQueue<TaskPriority> taskPriorityQueue;

    /**
     * processService
     */
    @Autowired
    private ProcessService processService;

    /**
     * executor dispatcher
     */
    @Autowired
    private ExecutorDispatcher dispatcher;

    /**
     * master config
     */
    @Autowired
    private MasterConfig masterConfig;

    @PostConstruct
    public void init() {
        super.setName("TaskUpdateQueueConsumerThread");
        super.start();
    }

    @Override
    public void run() {
        List<TaskPriority> failedDispatchTasks = new ArrayList<>();
        while (Stopper.isRunning()) {
            try {
                int fetchTaskNum = masterConfig.getMasterDispatchTaskNumber();
                failedDispatchTasks.clear();
                for (int i = 0; i < fetchTaskNum; i++) {
                    TaskPriority taskPriority = taskPriorityQueue.poll(Constants.SLEEP_TIME_MILLIS,
                            TimeUnit.MILLISECONDS);
                    if (Objects.isNull(taskPriority)) {
                        continue;
                    }

                    boolean dispatchResult = dispatch(taskPriority);
                    if (!dispatchResult) {
                        failedDispatchTasks.add(taskPriority);
                    }
                }
                if (!failedDispatchTasks.isEmpty()) {
                    for (TaskPriority dispatchFailedTask : failedDispatchTasks) {
                        taskPriorityQueue.put(dispatchFailedTask);
                    }
                    // If there are tasks in a cycle that cannot find the worker group,
                    // sleep for 1 second
                    if (taskPriorityQueue.size() <= failedDispatchTasks.size()) {
                        TimeUnit.MILLISECONDS.sleep(Constants.SLEEP_TIME_MILLIS);
                    }
                }
            } catch (Exception e) {
                logger.error("dispatcher task error", e);
            }
        }
    }

    /**
     * dispatch task
     *
     * @param taskPriority taskPriority
     * @return result
     */
    protected boolean dispatch(TaskPriority taskPriority) {
        boolean result = false;
        try {
            TaskExecutionContext context = taskPriority.getTaskExecutionContext();
            ExecutionContext executionContext = new ExecutionContext(context.toCommand(), ExecutorType.WORKER,
                    context.getWorkerGroup());

            if (taskInstanceIsFinalState(taskPriority.getTaskId())) {
                // when task finish, ignore this task, there is no need to dispatch anymore
                return true;
            } else {
                result = dispatcher.dispatch(executionContext);
            }
            if (result) {
                processService.updateHostAndSubmitTimeById(taskPriority.getTaskId(),
                        executionContext.getHost().getAddress(), new Date());
            }
        } catch (ExecuteException e) {
            logger.error("ExecuteException dispatch error: {}", e.getMessage(), e);
        } catch (Throwable t) {
            logger.error("dispatch error: {}", t, t);
        }
        return result;
    }

    /**
     * taskInstance is final state
     * success，failure，kill，stop，pause，threadwaiting is final state
     *
     * @param taskInstanceId taskInstanceId
     * @return taskInstance is final state
     */
    public Boolean taskInstanceIsFinalState(int taskInstanceId) {
        TaskInstance taskInstance = processService.findTaskInstanceById(taskInstanceId);
        return taskInstance.getState().typeIsFinished();
    }
}
