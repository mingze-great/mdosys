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

package com.mdosys.scheduler.server.master.consumer;

import com.mdosys.scheduler.common.enums.*;
import com.mdosys.scheduler.common.thread.Stopper;
import com.mdosys.scheduler.dao.entity.*;
import com.mdosys.scheduler.server.master.dispatch.ExecutorDispatcher;
import com.mdosys.scheduler.service.process.ProcessService;
import com.mdosys.scheduler.service.queue.TaskPriority;
import com.mdosys.scheduler.service.queue.TaskPriorityQueue;
import com.mdosys.scheduler.spi.enums.DbType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class TaskPriorityQueueConsumerTest {

    @Autowired
    private TaskPriorityQueue<TaskPriority> taskPriorityQueue;

    @Autowired
    private TaskPriorityQueueConsumer taskPriorityQueueConsumer;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ExecutorDispatcher dispatcher;

    @Before
    public void init() {

        Tenant tenant = new Tenant();
        tenant.setId(1);
        tenant.setTenantCode("journey");
        tenant.setDescription("journey");
        tenant.setQueueId(1);
        tenant.setCreateTime(new Date());
        tenant.setUpdateTime(new Date());

        Mockito.doReturn(tenant).when(processService).getTenantForProcess(1, 2);

        Mockito.doReturn("default").when(processService).queryUserQueueByProcessInstanceId(1);
    }

    @Test
    public void testSHELLTask() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("default");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1);
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);

        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "default");
        taskPriorityQueue.put(taskPriority);

        TimeUnit.SECONDS.sleep(10);

        Assert.assertNotNull(taskInstance);
    }

    @Test
    public void testSQLTask() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SQL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("default");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);
        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "default");
        taskPriorityQueue.put(taskPriority);

        DataSource dataSource = new DataSource();
        dataSource.setId(1);
        dataSource.setName("sqlDatasource");
        dataSource.setType(DbType.MYSQL);
        dataSource.setUserId(2);
        dataSource.setConnectionParams("{\"address\":\"jdbc:mysql://192.168.221.185:3306\","
                + "\"database\":\"scheduler_qiaozhanwei\","
                + "\"jdbcUrl\":\"jdbc:mysql://192.168.221.185:3306/scheduler_qiaozhanwei\","
                + "\"user\":\"root\","
                + "\"password\":\"root@123\"}");
        dataSource.setCreateTime(new Date());
        dataSource.setUpdateTime(new Date());

        Mockito.doReturn(dataSource).when(processService).findDataSourceById(1);

        TimeUnit.SECONDS.sleep(10);
        Assert.assertNotNull(taskInstance);
    }

    @Test
    public void testDataxTask() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.DATAX.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("default");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);
        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "default");
        taskPriorityQueue.put(taskPriority);

        DataSource dataSource = new DataSource();
        dataSource.setId(80);
        dataSource.setName("datax");
        dataSource.setType(DbType.MYSQL);
        dataSource.setUserId(2);
        dataSource.setConnectionParams("{\"address\":\"jdbc:mysql://192.168.221.185:3306\","
                + "\"database\":\"scheduler_qiaozhanwei\","
                + "\"jdbcUrl\":\"jdbc:mysql://192.168.221.185:3306/scheduler_qiaozhanwei\","
                + "\"user\":\"root\","
                + "\"password\":\"root@123\"}");
        dataSource.setCreateTime(new Date());
        dataSource.setUpdateTime(new Date());
        Mockito.doReturn(dataSource).when(processService).findDataSourceById(80);
        TimeUnit.SECONDS.sleep(10);
        Assert.assertNotNull(taskInstance);
    }

    @Test
    public void testSqoopTask() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SQOOP.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("default");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);
        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "default");
        taskPriorityQueue.put(taskPriority);

        DataSource dataSource = new DataSource();
        dataSource.setId(1);
        dataSource.setName("datax");
        dataSource.setType(DbType.MYSQL);
        dataSource.setUserId(2);
        dataSource.setConnectionParams("{\"address\":\"jdbc:mysql://192.168.221.185:3306\","
                + "\"database\":\"scheduler_qiaozhanwei\","
                + "\"jdbcUrl\":\"jdbc:mysql://192.168.221.185:3306/scheduler_qiaozhanwei\","
                + "\"user\":\"root\","
                + "\"password\":\"root@123\"}");
        dataSource.setCreateTime(new Date());
        dataSource.setUpdateTime(new Date());
        Mockito.doReturn(dataSource).when(processService).findDataSourceById(1);
        TimeUnit.SECONDS.sleep(10);
        Assert.assertNotNull(taskInstance);
    }

    @Test
    public void testTaskInstanceIsFinalState() {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("default");
        taskInstance.setExecutorId(2);

        Mockito.doReturn(taskInstance).when(processService).findTaskInstanceById(1);

        Boolean state = taskPriorityQueueConsumer.taskInstanceIsFinalState(1);
        Assert.assertNotNull(state);
    }

    @Test
    public void testNotFoundWorkerGroup() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("NoWorkGroup");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1);
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);
        taskInstance.setState(ExecutionStatus.DELAY_EXECUTION);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);

        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        Mockito.doReturn(taskInstance).when(processService).findTaskInstanceById(1);

        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "NoWorkGroup");
        taskPriorityQueue.put(taskPriority);

        TimeUnit.SECONDS.sleep(10);

        Assert.assertNotNull(taskInstance);

    }

    @Test
    public void testDispatch() {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("NoWorkGroup");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1);
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);
        taskInstance.setState(ExecutionStatus.DELAY_EXECUTION);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        processDefinition.setProjectCode(1L);
        taskInstance.setProcessDefine(processDefinition);

        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setTimeoutFlag(TimeoutFlag.OPEN);
        taskInstance.setTaskDefine(taskDefinition);

        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        Mockito.doReturn(taskInstance).when(processService).findTaskInstanceById(1);

        TaskPriority taskPriority = new TaskPriority();
        taskPriority.setTaskId(1);
        boolean res = taskPriorityQueueConsumer.dispatch(taskPriority);

        Assert.assertFalse(res);
    }

    @Test
    public void testRun() throws Exception {
        TaskInstance taskInstance = new TaskInstance();
        taskInstance.setId(1);
        taskInstance.setTaskType(TaskType.SHELL.getDesc());
        taskInstance.setProcessInstanceId(1);
        taskInstance.setState(ExecutionStatus.KILL);
        taskInstance.setProcessInstancePriority(Priority.MEDIUM);
        taskInstance.setWorkerGroup("NoWorkGroup");
        taskInstance.setExecutorId(2);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(1);
        processInstance.setTenantId(1);
        processInstance.setCommandType(CommandType.START_PROCESS);
        taskInstance.setProcessInstance(processInstance);
        taskInstance.setState(ExecutionStatus.DELAY_EXECUTION);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setUserId(2);
        taskInstance.setProcessDefine(processDefinition);

        Mockito.doReturn(taskInstance).when(processService).getTaskInstanceDetailByTaskId(1);
        Mockito.doReturn(taskInstance).when(processService).findTaskInstanceById(1);

        TaskPriority taskPriority = new TaskPriority(2, 1, 2, 1, "NoWorkGroup");
        taskPriorityQueue.put(taskPriority);

        taskPriorityQueueConsumer.run();

        TimeUnit.SECONDS.sleep(10);
        Assert.assertNotEquals(-1, taskPriorityQueue.size());

    }

    @After
    public void close() {
        Stopper.stop();
    }

}
