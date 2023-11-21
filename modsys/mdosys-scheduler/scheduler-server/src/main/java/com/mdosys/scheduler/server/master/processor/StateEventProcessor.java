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
import com.mdosys.scheduler.common.enums.StateEvent;
import com.mdosys.scheduler.common.enums.StateEventType;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.remote.command.Command;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.command.StateEventChangeCommand;
import com.mdosys.scheduler.remote.processor.NettyRequestProcessor;
import com.mdosys.scheduler.server.master.processor.queue.StateEventResponseService;
import com.mdosys.scheduler.server.master.runner.WorkflowExecuteThread;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * handle state event received from master/api
 */
public class StateEventProcessor implements NettyRequestProcessor {

    private final Logger logger = LoggerFactory.getLogger(StateEventProcessor.class);

    private StateEventResponseService stateEventResponseService;

    public StateEventProcessor() {
        stateEventResponseService = SpringApplicationContext.getBean(StateEventResponseService.class);
    }

    public void init(ConcurrentHashMap<Integer, WorkflowExecuteThread> processInstanceExecMaps) {
        this.stateEventResponseService.init(processInstanceExecMaps);
    }

    @Override
    public void process(Channel channel, Command command) {
        Preconditions.checkArgument(CommandType.STATE_EVENT_REQUEST == command.getType(),
                String.format("invalid command type: %s", command.getType()));

        StateEventChangeCommand stateEventChangeCommand = JSONUtils.parseObject(command.getBody(),
                StateEventChangeCommand.class);
        StateEvent stateEvent = new StateEvent();
        stateEvent.setKey(stateEventChangeCommand.getKey());
        if (stateEventChangeCommand.getSourceProcessInstanceId() != stateEventChangeCommand
                .getDestProcessInstanceId()) {
            stateEvent.setExecutionStatus(ExecutionStatus.RUNNING_EXECUTION);
        } else {
            stateEvent.setExecutionStatus(stateEventChangeCommand.getSourceStatus());
        }
        stateEvent.setProcessInstanceId(stateEventChangeCommand.getDestProcessInstanceId());
        stateEvent.setTaskInstanceId(stateEventChangeCommand.getDestTaskInstanceId());
        StateEventType type = stateEvent.getTaskInstanceId() == 0 ? StateEventType.PROCESS_STATE_CHANGE
                : StateEventType.TASK_STATE_CHANGE;
        stateEvent.setType(type);

        logger.info("received command : {}", stateEvent);
        stateEventResponseService.addResponse(stateEvent);
    }

}
