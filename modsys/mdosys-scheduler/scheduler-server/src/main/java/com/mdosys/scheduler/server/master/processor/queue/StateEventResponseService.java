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

import io.netty.channel.Channel;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.StateEvent;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.remote.command.StateEventResponseCommand;
import com.mdosys.scheduler.server.master.runner.WorkflowExecuteThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * task manager
 */
@Component
public class StateEventResponseService {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(StateEventResponseService.class);

    /**
     * attemptQueue
     */
    private final BlockingQueue<StateEvent> eventQueue = new LinkedBlockingQueue<>(5000);

    /**
     * task response worker
     */
    private Thread responseWorker;

    private ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceMapper;

    public void init(ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceMapper) {
        if (this.processInstanceMapper == null) {
            this.processInstanceMapper = processInstanceMapper;
        }
    }

    @PostConstruct
    public void start() {
        this.responseWorker = new StateEventResponseWorker();
        this.responseWorker.setName("StateEventResponseWorker");
        this.responseWorker.start();
    }

    @PreDestroy
    public void stop() {
        this.responseWorker.interrupt();
        if (!eventQueue.isEmpty()) {
            List<StateEvent> remainEvents = new ArrayList<>(eventQueue.size());
            eventQueue.drainTo(remainEvents);
            for (StateEvent event : remainEvents) {
                this.persist(event);
            }
        }
    }

    /**
     * put task to attemptQueue
     */
    public void addResponse(StateEvent stateEvent) {
        try {
            eventQueue.put(stateEvent);
        } catch (InterruptedException e) {
            logger.error("put state event : {} error :{}", stateEvent, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * task worker thread
     */
    class StateEventResponseWorker extends Thread {

        @Override
        public void run() {

            while (Stopper.isRunning()) {
                try {
                    // if not task , blocking here
                    StateEvent stateEvent = eventQueue.take();
                    persist(stateEvent);
                } catch (InterruptedException e) {
                    logger.warn("persist task error", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            logger.info("StateEventResponseWorker stopped");
        }
    }

    private void writeResponse(StateEvent stateEvent, ExecutionStatus status) {
        Channel channel = stateEvent.getChannel();
        if (channel != null) {
            StateEventResponseCommand command = new StateEventResponseCommand(status.getCode(), stateEvent.getKey());
            channel.writeAndFlush(command.convert2Command());
        }
    }

    private void persist(StateEvent stateEvent) {
        try {
            if (!this.processInstanceMapper.containsKey(stateEvent.getProcessInstanceId())) {
                writeResponse(stateEvent, ExecutionStatus.FAILURE);
                return;
            }

            WorkflowExecuteThread workflowExecuteThread = this.processInstanceMapper
                    .get(stateEvent.getProcessInstanceId());
            workflowExecuteThread.addStateEvent(stateEvent);
            writeResponse(stateEvent, ExecutionStatus.SUCCESS);
        } catch (Exception e) {
            logger.error("persist event queue error, event: {}", stateEvent, e);
        }
    }

    public BlockingQueue<StateEvent> getEventQueue() {
        return eventQueue;
    }
}
