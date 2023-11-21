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

package com.mdosys.scheduler.service.alert;

import com.mdosys.scheduler.common.enums.CommandType;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.WarningType;
import com.mdosys.scheduler.dao.AlertDao;

import com.mdosys.scheduler.dao.entity.ProcessInstance;
import com.mdosys.scheduler.dao.entity.ProjectUser;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ProcessAlertManager Test
 */
@RunWith(PowerMockRunner.class)
public class ProcessAlertManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(ProcessAlertManagerTest.class);

    @InjectMocks
    ProcessAlertManager processAlertManager = new ProcessAlertManager();

    @Mock
    private AlertDao alertDao;

    /**
     * send worker alert fault tolerance
     */
    @Test
    public void sendWarningWorkerToleranceFaultTest() {
        // process instance
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setName("test");

        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setName("test-task-1");
        taskInstance.setHost("127.0.0.1");
        taskInstance.setRetryTimes(3);
        List<TaskInstance> taskInstanceList = new ArrayList<>();
        taskInstanceList.add(taskInstance);

        processAlertManager.sendAlertWorkerToleranceFault(processInstance, taskInstanceList);
    }

    /**
     * send worker alert fault tolerance
     */
    @Test
    public void sendWarnningOfProcessInstanceTest() {
        // process instance
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setWarningType(WarningType.SUCCESS);
        processInstance.setState(ExecutionStatus.SUCCESS);
        processInstance.setCommandType(CommandType.COMPLEMENT_DATA);
        processInstance.setWarningGroupId(1);

        ProjectUser projectUser = new ProjectUser();
        TaskInstance taskInstance = new TaskInstance();
        List<TaskInstance> taskInstanceList = new ArrayList<>();
        taskInstanceList.add(taskInstance);

        processAlertManager.sendAlertProcessInstance(processInstance, taskInstanceList, projectUser);
    }

}
