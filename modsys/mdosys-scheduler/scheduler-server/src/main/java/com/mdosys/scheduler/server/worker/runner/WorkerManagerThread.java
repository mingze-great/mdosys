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

package com.mdosys.scheduler.server.worker.runner;

import com.mdosys.scheduler.common.enums.Event;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.common.thread.ThreadUtils;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.remote.command.TaskExecuteResponseCommand;
import com.mdosys.scheduler.server.worker.cache.ResponceCache;
import com.mdosys.scheduler.server.worker.config.WorkerConfig;
import com.mdosys.scheduler.server.worker.processor.TaskCallbackService;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.queue.entity.TaskExecutionContext;
import com.mdosys.scheduler.spi.task.TaskExecutionContextCacheManager;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Manage tasks
 */
@Component
public class WorkerManagerThread implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(WorkerManagerThread.class);

    /**
     * task queue
     */
    private final DelayQueue<TaskExecuteThread> workerExecuteQueue = new DelayQueue<>();

    /**
     * worker config
     */
    private final WorkerConfig workerConfig;

    /**
     * thread executor service
     */
    private final ExecutorService workerExecService;

    /**
     * task callback service
     */
    private final TaskCallbackService taskCallbackService;

    public WorkerManagerThread() {
        this.workerConfig = SpringApplicationContext.getBean(WorkerConfig.class);
        this.workerExecService = ThreadUtils.newDaemonFixedThreadExecutor("Worker-Execute-Thread",
                this.workerConfig.getWorkerExecThreads());
        this.taskCallbackService = SpringApplicationContext.getBean(TaskCallbackService.class);
    }

    /**
     * get delay queue size
     *
     * @return queue size
     */
    public int getDelayQueueSize() {
        return workerExecuteQueue.size();
    }

    /**
     * get thread pool queue size
     *
     * @return queue size
     */
    public int getThreadPoolQueueSize() {
        return ((ThreadPoolExecutor) workerExecService).getQueue().size();
    }

    /**
     * Kill tasks that have not been executed, like delay task
     * then send Response to Master, update the execution status of task instance
     */
    public void killTaskBeforeExecuteByInstanceId(Integer taskInstanceId) {
        workerExecuteQueue.stream()
                .filter(taskExecuteThread -> taskExecuteThread.getTaskExecutionContext()
                        .getTaskInstanceId() == taskInstanceId)
                .forEach(workerExecuteQueue::remove);
        sendTaskKillResponse(taskInstanceId);
    }

    /**
     * kill task before execute , like delay task
     */
    private void sendTaskKillResponse(Integer taskInstanceId) {
        TaskRequest taskRequest = TaskExecutionContextCacheManager.getByTaskInstanceId(taskInstanceId);
        if (taskRequest == null) {
            return;
        }
        TaskExecutionContext taskExecutionContext = JSONUtils.parseObject(JSONUtils.toJsonString(taskRequest),
                TaskExecutionContext.class);
        TaskExecuteResponseCommand responseCommand = new TaskExecuteResponseCommand(
                taskExecutionContext.getTaskInstanceId(), taskExecutionContext.getProcessInstanceId());
        responseCommand.setStatus(ExecutionStatus.KILL.getCode());
        ResponceCache.get().cache(taskExecutionContext.getTaskInstanceId(), responseCommand.convert2Command(),
                Event.RESULT);
        taskCallbackService.sendResult(taskExecutionContext.getTaskInstanceId(), responseCommand.convert2Command());
    }

    /**
     * submit task
     *
     * @param taskExecuteThread taskExecuteThread
     * @return submit result
     */
    public boolean offer(TaskExecuteThread taskExecuteThread) {
        return workerExecuteQueue.offer(taskExecuteThread);
    }

    public void start() {
        Thread thread = new Thread(this, this.getClass().getName());
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Worker-Execute-Manager-Thread");
        TaskExecuteThread taskExecuteThread;
        while (Stopper.isRunning()) {
            try {
                taskExecuteThread = workerExecuteQueue.take();
                workerExecService.submit(taskExecuteThread);
            } catch (Exception e) {
                logger.error("An unexpected interrupt is happened, "
                        + "the exception will be ignored and this thread will continue to run", e);
            }
        }
    }
}
