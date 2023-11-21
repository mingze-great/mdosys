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

package com.mdosys.scheduler.server.master.runner;

import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.TaskTimeoutStrategy;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.common.enums.TimeoutFlag;
import com.mdosys.scheduler.dao.entity.TaskDefinition;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.process.ProcessService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.Silent.class)
@Ignore
public class MasterTaskExecThreadTest {

    private SpringApplicationContext springApplicationContext;

    @Before
    public void setUp() {
        ApplicationContext applicationContext = PowerMockito.mock(ApplicationContext.class);
        this.springApplicationContext = new SpringApplicationContext();
        springApplicationContext.setApplicationContext(applicationContext);
        // this.registryCenter = PowerMockito.mock(RegistryCenter.class);
        // PowerMockito.when(SpringApplicationContext.getBean(RegistryCenter.class))
        // .thenReturn(this.registryCenter);
        ProcessService processService = Mockito.mock(ProcessService.class);
        Mockito.when(SpringApplicationContext.getBean(ProcessService.class))
                .thenReturn(processService);
        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setTimeoutFlag(TimeoutFlag.OPEN);
        taskDefinition.setTimeoutNotifyStrategy(TaskTimeoutStrategy.WARN);
        taskDefinition.setTimeout(0);
        Mockito.when(processService.findTaskDefinition(1L, 1))
                .thenReturn(taskDefinition);
        // this.masterTaskExecThread = new MasterTaskExecThread(getTaskInstance());
    }

    @Test
    public void testExistsValidWorkerGroup1() {

        /*
         * Mockito.when(registryCenter.getWorkerGroupDirectly()).thenReturn(Sets.
         * newHashSet());
         * boolean b = masterTaskExecThread.existsValidWorkerGroup("default");
         * Assert.assertFalse(b);
         */
    }

    @Test
    public void testExistsValidWorkerGroup2() {
        Set<String> workerGorups = new HashSet<>();
        workerGorups.add("test1");
        workerGorups.add("test2");

        /*
         * Mockito.when(registryCenter.getWorkerGroupDirectly()).thenReturn(workerGorups
         * );
         * boolean b = masterTaskExecThread.existsValidWorkerGroup("default");
         * Assert.assertFalse(b);
         */
    }

    @Test
    public void testExistsValidWorkerGroup3() {
        Set<String> workerGorups = new HashSet<>();
        workerGorups.add("test1");
        /*
         * Mockito.when(registryCenter.getWorkerGroupDirectly()).thenReturn(workerGorups
         * );
         * Mockito.when(registryCenter.getWorkerGroupNodesDirectly("test1")).thenReturn(
         * workerGorups);
         * boolean b = masterTaskExecThread.existsValidWorkerGroup("test1");
         * Assert.assertTrue(b);
         */
    }

    @Test
    public void testPauseTask() {
        ProcessService processService = Mockito.mock(ProcessService.class);
        Mockito.when(SpringApplicationContext.getBean(ProcessService.class))
                .thenReturn(processService);

        TaskInstance taskInstance = getTaskInstance();
        Mockito.when(processService.findTaskInstanceById(252612))
                .thenReturn(taskInstance);

        Mockito.when(processService.updateTaskInstance(taskInstance))
                .thenReturn(true);

        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setTimeoutFlag(TimeoutFlag.OPEN);
        taskDefinition.setTimeoutNotifyStrategy(TaskTimeoutStrategy.WARN);
        taskDefinition.setTimeout(0);
        Mockito.when(processService.findTaskDefinition(1L, 1))
                .thenReturn(taskDefinition);

        // MasterTaskExecThread masterTaskExecThread = new
        // MasterTaskExecThread(taskInstance);
        // masterTaskExecThread.pauseTask();
        // org.junit.Assert.assertEquals(ExecutionStatus.PAUSE,
        // taskInstance.getState());
    }

    private TaskInstance getTaskInstance() {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setId(252612);
        taskInstance.setName("C");
        taskInstance.setProcessInstanceId(10111);
        taskInstance.setState(ExecutionStatus.SUBMITTED_SUCCESS);
        taskInstance.setTaskCode(1L);
        taskInstance.setTaskDefinitionVersion(1);
        return taskInstance;
    }

}
