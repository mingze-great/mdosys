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

import io.netty.channel.Channel;
import com.mdosys.scheduler.remote.command.TaskExecuteAckCommand;
import com.mdosys.scheduler.server.master.processor.queue.TaskResponseEvent;
import com.mdosys.scheduler.server.master.processor.queue.TaskResponseService;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.process.ProcessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

/**
 * task ack processor test
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SpringApplicationContext.class, TaskResponseEvent.class })
public class TaskAckProcessorTest {

    private TaskAckProcessor taskAckProcessor;
    private TaskResponseService taskResponseService;
    private ProcessService processService;
    private TaskExecuteAckCommand taskExecuteAckCommand;
    private TaskResponseEvent taskResponseEvent;
    private Channel channel;

    @Before
    public void before() {
        PowerMockito.mockStatic(SpringApplicationContext.class);

        taskResponseService = PowerMockito.mock(TaskResponseService.class);
        PowerMockito.when(SpringApplicationContext.getBean(TaskResponseService.class)).thenReturn(taskResponseService);

        processService = PowerMockito.mock(ProcessService.class);
        PowerMockito.when(SpringApplicationContext.getBean(ProcessService.class)).thenReturn(processService);

        taskAckProcessor = new TaskAckProcessor();

        channel = PowerMockito.mock(Channel.class);
        taskResponseEvent = PowerMockito.mock(TaskResponseEvent.class);

        taskExecuteAckCommand = new TaskExecuteAckCommand();
        taskExecuteAckCommand.setStatus(1);
        taskExecuteAckCommand.setExecutePath("/scheduler/worker");
        taskExecuteAckCommand.setHost("localhost");
        taskExecuteAckCommand.setLogPath("/temp/worker.log");
        taskExecuteAckCommand.setStartTime(new Date());
        taskExecuteAckCommand.setTaskInstanceId(1);
        taskExecuteAckCommand.setProcessInstanceId(1);
    }

    @Test
    public void testProcess() {
        // Command command = taskExecuteAckCommand.convert2Command();
        // Assert.assertEquals(CommandType.TASK_EXECUTE_ACK,command.getType());
        // InetSocketAddress socketAddress = new InetSocketAddress("localhost",12345);
        // PowerMockito.when(channel.remoteAddress()).thenReturn(socketAddress);
        // PowerMockito.mockStatic(TaskResponseEvent.class);
        //
        // PowerMockito.when(TaskResponseEvent.newAck(Mockito.any(), Mockito.any(),
        // Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
        // Mockito.anyInt(), channel))
        // .thenReturn(taskResponseEvent);
        // TaskInstance taskInstance = PowerMockito.mock(TaskInstance.class);
        // PowerMockito.when(processService.findTaskInstanceById(Mockito.any())).thenReturn(taskInstance);
        //
        // taskAckProcessor.process(channel,command);
    }
}
