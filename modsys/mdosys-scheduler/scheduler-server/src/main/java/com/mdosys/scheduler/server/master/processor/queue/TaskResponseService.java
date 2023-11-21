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

package com.mdosys.scheduler.server.master.processor.queue;

import com.google.common.util.concurrent.*;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.common.thread.ThreadUtils;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.master.runner.WorkflowExecuteThread;
import com.mdosys.scheduler.service.process.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * task manager
 */
@Component
public class TaskResponseService {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(TaskResponseService.class);

    /**
     * attemptQueue
     */
    private final BlockingQueue<TaskResponseEvent> eventQueue = new LinkedBlockingQueue<>();

    /**
     * process service
     */
    @Autowired
    private ProcessService processService;

    @Autowired
    private MasterConfig masterConfig;

    /**
     * task response worker
     */
    private Thread taskResponseWorker;

    /**
     * event handler
     */
    private Thread taskResponseEventHandler;

    private ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceMap;

    private final ConcurrentHashMap<String, TaskResponsePersistThread> taskResponseEventHandlerMap = new ConcurrentHashMap<>();

    private ListeningExecutorService listeningExecutorService;

    private ExecutorService eventExecService;

    /**
     * task response mapper
     */
    private final ConcurrentHashMap<Integer, TaskResponsePersistThread> processTaskResponseMap = new ConcurrentHashMap<>();

    public void init(ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceMap) {
        if (this.processInstanceMap == null) {
            this.processInstanceMap = processInstanceMap;
        }
    }

    @PostConstruct
    public void start() {
        eventExecService = ThreadUtils.newDaemonFixedThreadExecutor("PersistEventState",
                masterConfig.getMasterPersistEventStateThreads());
        this.listeningExecutorService = MoreExecutors.listeningDecorator(eventExecService);
        this.taskResponseWorker = new TaskResponseWorker();
        this.taskResponseWorker.setName("TaskResponseWorker");
        this.taskResponseWorker.start();
        this.taskResponseEventHandler = new TaskResponseEventHandler();
        this.taskResponseEventHandler.setName("TaskResponseEventHandler");
        this.taskResponseEventHandler.start();
    }

    @PreDestroy
    public void stop() {
        try {
            this.taskResponseWorker.interrupt();
            this.taskResponseEventHandler.interrupt();
            this.eventExecService.shutdown();
        } catch (Exception e) {
            logger.error("stop error:", e);
        }
    }

    /**
     * put task to attemptQueue
     *
     * @param taskResponseEvent taskResponseEvent
     */
    public void addResponse(TaskResponseEvent taskResponseEvent) {
        try {
            eventQueue.put(taskResponseEvent);
            logger.debug("eventQueue size:{}", eventQueue.size());
        } catch (InterruptedException e) {
            logger.error("put task : {} error :{}", taskResponseEvent, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * task worker thread
     */
    class TaskResponseWorker extends Thread {

        @Override
        public void run() {
            while (Stopper.isRunning()) {
                try {
                    // if not task , blocking here
                    TaskResponseEvent taskResponseEvent = eventQueue.take();
                    if (processInstanceMap.containsKey(taskResponseEvent.getProcessInstanceId())
                            && !processTaskResponseMap.containsKey(taskResponseEvent.getProcessInstanceId())) {
                        TaskResponsePersistThread taskResponsePersistThread = new TaskResponsePersistThread(
                                processService, processInstanceMap, taskResponseEvent.getProcessInstanceId());
                        processTaskResponseMap.put(taskResponseEvent.getProcessInstanceId(), taskResponsePersistThread);
                    }
                    TaskResponsePersistThread taskResponsePersistThread = processTaskResponseMap
                            .get(taskResponseEvent.getProcessInstanceId());
                    if (null != taskResponsePersistThread) {
                        if (taskResponsePersistThread.addEvent(taskResponseEvent)) {
                            logger.debug(
                                    "submit task response persist queue success, task instance id:{},process instance id:{}, state:{} ",
                                    taskResponseEvent.getTaskInstanceId(), taskResponseEvent.getProcessInstanceId(),
                                    taskResponseEvent.getState());
                        } else {
                            logger.error(
                                    "submit task response persist queue error, task instance id:{},process instance id:{} ",
                                    taskResponseEvent.getTaskInstanceId(), taskResponseEvent.getProcessInstanceId());
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("persist task error", e);
                }
            }
            logger.info("StateEventResponseWorker stopped");
        }
    }

    /**
     * event handler thread
     */
    class TaskResponseEventHandler extends Thread {

        @Override
        public void run() {
            logger.info("event handler thread started");
            while (Stopper.isRunning()) {
                try {
                    eventHandler();

                    TimeUnit.MILLISECONDS.sleep(Constants.SLEEP_TIME_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("event handler thread error", e);
                }
            }
        }

        private void eventHandler() {

            for (TaskResponsePersistThread taskResponsePersistThread : processTaskResponseMap.values()) {

                if (taskResponseEventHandlerMap.containsKey(taskResponsePersistThread.getKey())) {
                    continue;
                }
                if (taskResponsePersistThread.eventSize() == 0) {
                    if (!processInstanceMap.containsKey(taskResponsePersistThread.getProcessInstanceId())) {
                        processTaskResponseMap.remove(taskResponsePersistThread.getProcessInstanceId());
                        logger.info("remove process instance: {}", taskResponsePersistThread.getProcessInstanceId());
                    }
                    continue;
                }
                logger.info("already exists handler process size:{}", taskResponseEventHandlerMap.size());
                taskResponseEventHandlerMap.put(taskResponsePersistThread.getKey(), taskResponsePersistThread);

                ListenableFuture future = listeningExecutorService.submit(taskResponsePersistThread);
                FutureCallback futureCallback = new FutureCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        logger.info("persist events {} succeeded.", taskResponsePersistThread.getProcessInstanceId());
                        if (!processInstanceMap.containsKey(taskResponsePersistThread.getProcessInstanceId())) {
                            processTaskResponseMap.remove(taskResponsePersistThread.getProcessInstanceId());
                            logger.info("remove process instance: {}",
                                    taskResponsePersistThread.getProcessInstanceId());
                        }
                        taskResponseEventHandlerMap.remove(taskResponsePersistThread.getKey());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.error("persist events failed: {}", throwable);
                        if (!processInstanceMap.containsKey(taskResponsePersistThread.getProcessInstanceId())) {
                            processTaskResponseMap.remove(taskResponsePersistThread.getProcessInstanceId());
                            logger.info("remove process instance: {}",
                                    taskResponsePersistThread.getProcessInstanceId());
                        }
                        taskResponseEventHandlerMap.remove(taskResponsePersistThread.getKey());
                    }
                };
                Futures.addCallback(future, futureCallback, listeningExecutorService);
            }
        }
    }

    public BlockingQueue<TaskResponseEvent> getEventQueue() {
        return eventQueue;
    }
}