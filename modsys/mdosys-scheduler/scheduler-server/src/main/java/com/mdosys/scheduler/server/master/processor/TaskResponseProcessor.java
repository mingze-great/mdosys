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

package com.mdosys.scheduler.server.master.processor;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.remote.command.Command;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.command.TaskExecuteResponseCommand;
import com.mdosys.scheduler.remote.processor.NettyRequestProcessor;
import com.mdosys.scheduler.server.master.processor.queue.TaskResponseEvent;
import com.mdosys.scheduler.server.master.processor.queue.TaskResponseService;
import com.mdosys.scheduler.server.master.runner.WorkflowExecuteThread;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * task response processor
 */
public class TaskResponseProcessor implements NettyRequestProcessor {

    private final Logger logger = LoggerFactory.getLogger(TaskResponseProcessor.class);

    /**
     * process service
     */
    private final TaskResponseService taskResponseService;

    public TaskResponseProcessor() {
        this.taskResponseService = SpringApplicationContext.getBean(TaskResponseService.class);
    }

    public void init(ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceExecMaps) {
        this.taskResponseService.init(processInstanceExecMaps);
    }

    /**
     * task final result response
     * need master process , state persistence
     *
     * @param channel channel
     * @param command command
     */
    @Override
    public void process(Channel channel, Command command) {
        Preconditions.checkArgument(CommandType.TASK_EXECUTE_RESPONSE == command.getType(),
                String.format("invalid command type : %s", command.getType()));

        TaskExecuteResponseCommand responseCommand = JSONUtils.parseObject(command.getBody(),
                TaskExecuteResponseCommand.class);
        logger.info("received command : {}", responseCommand);

        // TaskResponseEvent
        TaskResponseEvent taskResponseEvent = TaskResponseEvent.newResult(
                ExecutionStatus.of(responseCommand.getStatus()),
                responseCommand.getEndTime(),
                responseCommand.getProcessId(),
                responseCommand.getAppIds(),
                responseCommand.getTaskInstanceId(),
                responseCommand.getVarPool(),
                channel,
                responseCommand.getProcessInstanceId());
        taskResponseService.addResponse(taskResponseEvent);
    }
}
