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
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.remote.command.Command;
import com.mdosys.scheduler.remote.command.CommandType;
import com.mdosys.scheduler.remote.command.DBTaskResponseCommand;
import com.mdosys.scheduler.remote.processor.NettyRequestProcessor;
import com.mdosys.scheduler.server.worker.cache.ResponceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * db task response processor
 */
public class DBTaskResponseProcessor implements NettyRequestProcessor {

    private final Logger logger = LoggerFactory.getLogger(DBTaskResponseProcessor.class);

    @Override
    public void process(Channel channel, Command command) {
        Preconditions.checkArgument(CommandType.DB_TASK_RESPONSE == command.getType(),
                String.format("invalid command type : %s", command.getType()));

        DBTaskResponseCommand taskResponseCommand = JSONUtils.parseObject(
                command.getBody(), DBTaskResponseCommand.class);

        if (taskResponseCommand == null) {
            return;
        }

        if (taskResponseCommand.getStatus() == ExecutionStatus.SUCCESS.getCode()) {
            ResponceCache.get().removeResponseCache(taskResponseCommand.getTaskInstanceId());
            logger.debug("removeResponseCache: taskinstance id:{}", taskResponseCommand.getTaskInstanceId());
            TaskCallbackService.remove(taskResponseCommand.getTaskInstanceId());
            logger.debug("remove REMOTE_CHANNELS, task instance id:{}", taskResponseCommand.getTaskInstanceId());
        }
    }

}
