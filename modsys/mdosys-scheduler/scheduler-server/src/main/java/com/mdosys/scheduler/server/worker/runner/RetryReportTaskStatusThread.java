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

import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.common.thread.ThreadUtils;
import com.mdosys.scheduler.remote.command.Command;
import com.mdosys.scheduler.server.worker.cache.ResponceCache;
import com.mdosys.scheduler.server.worker.config.WorkerConfig;
import com.mdosys.scheduler.server.worker.processor.TaskCallbackService;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Retry Report Task Status Thread
 */
@Component
public class RetryReportTaskStatusThread implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(RetryReportTaskStatusThread.class);

    @Autowired
    WorkerConfig workerConfig;

    /**
     * task callback service
     */
    private final TaskCallbackService taskCallbackService;

    public void start() {
        Thread thread = new Thread(this, "RetryReportTaskStatusThread");
        thread.setDaemon(true);
        thread.start();
    }

    public RetryReportTaskStatusThread() {
        this.taskCallbackService = SpringApplicationContext.getBean(TaskCallbackService.class);
    }

    /**
     * retry ack/response
     */
    @Override
    public void run() {
        ResponceCache responceCache = ResponceCache.get();

        while (Stopper.isRunning()) {

            // sleep 5 minutes
            ThreadUtils.sleep(workerConfig.getRetryReportTaskStatusInterval() * 1000);

            try {
                if (!responceCache.getAckCache().isEmpty()) {
                    Map<Integer, Command> ackCache = responceCache.getAckCache();
                    for (Map.Entry<Integer, Command> entry : ackCache.entrySet()) {
                        Integer taskInstanceId = entry.getKey();
                        Command ackCommand = entry.getValue();
                        taskCallbackService.sendAck(taskInstanceId, ackCommand);
                    }
                }

                if (!responceCache.getResponseCache().isEmpty()) {
                    Map<Integer, Command> responseCache = responceCache.getResponseCache();
                    for (Map.Entry<Integer, Command> entry : responseCache.entrySet()) {
                        Integer taskInstanceId = entry.getKey();
                        Command responseCommand = entry.getValue();
                        taskCallbackService.sendResult(taskInstanceId, responseCommand);
                    }
                }
            } catch (Exception e) {
                logger.warn("retry report task status error", e);
            }
        }
    }
}
