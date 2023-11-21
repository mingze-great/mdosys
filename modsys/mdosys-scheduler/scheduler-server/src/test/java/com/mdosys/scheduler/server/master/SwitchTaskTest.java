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

package com.mdosys.scheduler.server.master;

import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.TaskTimeoutStrategy;
import com.mdosys.scheduler.common.enums.TimeoutFlag;
import com.mdosys.scheduler.common.task.switchtask.SwitchParameters;
import com.mdosys.scheduler.common.task.switchtask.SwitchResultVo;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.dao.entity.ProcessInstance;
import com.mdosys.scheduler.dao.entity.TaskDefinition;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.process.ProcessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SwitchTaskTest {

    private ProcessService processService;

    private ProcessInstance processInstance;

    @Before
    public void before() {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        SpringApplicationContext springApplicationContext = new SpringApplicationContext();
        springApplicationContext.setApplicationContext(applicationContext);

        MasterConfig config = new MasterConfig();
        Mockito.when(applicationContext.getBean(MasterConfig.class)).thenReturn(config);
        config.setMasterTaskCommitRetryTimes(3);
        config.setMasterTaskCommitInterval(1000);

        processService = Mockito.mock(ProcessService.class);
        Mockito.when(applicationContext.getBean(ProcessService.class)).thenReturn(processService);

        processInstance = getProcessInstance();
        Mockito.when(processService
                .findProcessInstanceById(processInstance.getId()))
                .thenReturn(processInstance);
    }

    private TaskInstance testBasicInit(ExecutionStatus expectResult) {
        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setTimeoutFlag(TimeoutFlag.OPEN);
        taskDefinition.setTimeoutNotifyStrategy(TaskTimeoutStrategy.WARN);
        taskDefinition.setTimeout(0);
        Mockito.when(processService.findTaskDefinition(1L, 1))
                .thenReturn(taskDefinition);
        TaskInstance taskInstance = getTaskInstance(getTaskNode(), processInstance);

        // for MasterBaseTaskExecThread.submit
        Mockito.when(processService
                .submitTask(taskInstance))
                .thenReturn(taskInstance);
        // for MasterBaseTaskExecThread.call
        Mockito.when(processService
                .findTaskInstanceById(taskInstance.getId()))
                .thenReturn(taskInstance);
        // for SwitchTaskExecThread.initTaskParameters
        Mockito.when(processService
                .saveTaskInstance(taskInstance))
                .thenReturn(true);
        // for SwitchTaskExecThread.updateTaskState
        Mockito.when(processService
                .updateTaskInstance(taskInstance))
                .thenReturn(true);

        return taskInstance;
    }

    @Test
    public void testExe() throws Exception {
        TaskInstance taskInstance = testBasicInit(ExecutionStatus.SUCCESS);
        taskInstance.setState(ExecutionStatus.SUBMITTED_SUCCESS);
        // SwitchTaskExecThread taskExecThread = new SwitchTaskExecThread(taskInstance);
        // taskExecThread.call();
        // Assert.assertEquals(ExecutionStatus.SUCCESS,
        // taskExecThread.getTaskInstance().getState());
    }

    private SwitchParameters getTaskNode() {
        SwitchParameters conditionsParameters = new SwitchParameters();

        SwitchResultVo switchResultVo1 = new SwitchResultVo();
        switchResultVo1.setCondition(" 2 == 1");
        switchResultVo1.setNextNode("t1");
        SwitchResultVo switchResultVo2 = new SwitchResultVo();
        switchResultVo2.setCondition(" 2 == 2");
        switchResultVo2.setNextNode("t2");
        SwitchResultVo switchResultVo3 = new SwitchResultVo();
        switchResultVo3.setCondition(" 3 == 2");
        switchResultVo3.setNextNode("t3");
        List<SwitchResultVo> list = new ArrayList<>();
        list.add(switchResultVo1);
        list.add(switchResultVo2);
        list.add(switchResultVo3);
        conditionsParameters.setDependTaskList(list);
        conditionsParameters.setNextNode("t");
        conditionsParameters.setRelation("AND");

        return conditionsParameters;
    }

    private ProcessInstance getProcessInstance() {
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1000);
        processInstance.setState(ExecutionStatus.RUNNING_EXECUTION);
        processInstance.setProcessDefinitionCode(1L);
        return processInstance;
    }

    private TaskInstance getTaskInstance(SwitchParameters conditionsParameters, ProcessInstance processInstance) {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1000);
        Map<String, Object> taskParamsMap = new HashMap<>();
        taskParamsMap.put(Constants.SWITCH_RESULT, "");
        taskInstance.setTaskParams(JSONUtils.toJsonString(taskParamsMap));
        taskInstance.setSwitchDependency(conditionsParameters);
        taskInstance.setName("C");
        taskInstance.setTaskType("SWITCH");
        taskInstance.setProcessInstanceId(processInstance.getId());
        taskInstance.setTaskCode(1L);
        taskInstance.setTaskDefinitionVersion(1);
        return taskInstance;
    }
}