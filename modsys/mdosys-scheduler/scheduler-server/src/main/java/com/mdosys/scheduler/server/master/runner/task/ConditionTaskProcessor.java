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

package com.mdosys.scheduler.server.master.runner.task;

import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.DependResult;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.TaskTimeoutStrategy;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.common.model.DependentItem;
import com.mdosys.scheduler.common.model.DependentTaskModel;
import com.mdosys.scheduler.common.task.dependent.DependentParameters;
import com.mdosys.scheduler.common.utils.DependentUtils;
import com.mdosys.scheduler.common.utils.LoggerUtils;
import com.mdosys.scheduler.common.utils.NetUtils;
import com.mdosys.scheduler.dao.entity.ProcessInstance;
import com.mdosys.scheduler.dao.entity.TaskDefinition;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.utils.LogUtils;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.spi.task.TaskConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * condition task processor
 */
public class ConditionTaskProcessor extends BaseTaskProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(TaskConstants.TASK_LOG_LOGGER_NAME);

    /**
     * dependent parameters
     */
    private DependentParameters dependentParameters;

    ProcessInstance processInstance;

    /**
     * condition result
     */
    private DependResult conditionResult = DependResult.WAITING;

    /**
     * complete task map
     */
    private Map<Long, ExecutionStatus> completeTaskList = new ConcurrentHashMap<>();

    MasterConfig masterConfig = SpringApplicationContext.getBean(MasterConfig.class);

    private TaskDefinition taskDefinition;

    @Override
    public boolean submit(TaskInstance task, ProcessInstance processInstance, int masterTaskCommitRetryTimes,
            int masterTaskCommitInterval) {
        this.processInstance = processInstance;
        this.taskInstance = processService.submitTask(task, masterTaskCommitRetryTimes, masterTaskCommitInterval);

        if (this.taskInstance == null) {
            return false;
        }
        taskDefinition = processService.findTaskDefinition(
                taskInstance.getTaskCode(), taskInstance.getTaskDefinitionVersion());

        String threadLoggerInfoName = LoggerUtils.buildTaskId(LoggerUtils.TASK_LOGGER_INFO_PREFIX,
                processInstance.getProcessDefinitionCode(),
                processInstance.getProcessDefinitionVersion(),
                taskInstance.getProcessInstanceId(),
                taskInstance.getId());
        Thread.currentThread()
                .setName(String.format(TaskConstants.TASK_LOGGER_THREAD_NAME_FORMAT, threadLoggerInfoName));
        initTaskParameters();
        logger.info("dependent task start");
        return true;
    }

    @Override
    public ExecutionStatus taskState() {
        return this.taskInstance.getState();
    }

    @Override
    public void run() {
        if (conditionResult.equals(DependResult.WAITING)) {
            setConditionResult();
            endTask();
        } else {
            endTask();
        }
    }

    @Override
    protected boolean pauseTask() {
        this.taskInstance.setState(ExecutionStatus.PAUSE);
        this.taskInstance.setEndTime(new Date());
        processService.saveTaskInstance(taskInstance);
        return true;
    }

    @Override
    protected boolean taskTimeout() {
        TaskTimeoutStrategy taskTimeoutStrategy = taskDefinition.getTimeoutNotifyStrategy();
        if (taskTimeoutStrategy == TaskTimeoutStrategy.WARN) {
            return true;
        }
        logger.info("condition task {} timeout, strategy {} ",
                taskInstance.getId(), taskTimeoutStrategy.getDescp());
        conditionResult = DependResult.FAILED;
        endTask();
        return true;
    }

    @Override
    protected boolean killTask() {
        this.taskInstance.setState(ExecutionStatus.KILL);
        this.taskInstance.setEndTime(new Date());
        processService.saveTaskInstance(taskInstance);
        return true;
    }

    @Override
    public String getType() {
        return TaskType.CONDITIONS.getDesc();
    }

    private void initTaskParameters() {
        taskInstance.setLogPath(LogUtils.getTaskLogPath(processInstance.getProcessDefinitionCode(),
                processInstance.getProcessDefinitionVersion(),
                taskInstance.getProcessInstanceId(),
                taskInstance.getId()));
        this.taskInstance.setHost(NetUtils.getAddr(masterConfig.getListenPort()));
        taskInstance.setState(ExecutionStatus.RUNNING_EXECUTION);
        taskInstance.setStartTime(new Date());
        this.processService.saveTaskInstance(taskInstance);
        this.dependentParameters = taskInstance.getDependency();
    }

    private void setConditionResult() {

        List<TaskInstance> taskInstances = processService
                .findValidTaskListByProcessId(taskInstance.getProcessInstanceId());
        for (TaskInstance task : taskInstances) {
            completeTaskList.putIfAbsent(task.getTaskCode(), task.getState());
        }

        List<DependResult> modelResultList = new ArrayList<>();
        for (DependentTaskModel dependentTaskModel : dependentParameters.getDependTaskList()) {
            List<DependResult> itemDependResult = new ArrayList<>();
            for (DependentItem item : dependentTaskModel.getDependItemList()) {
                itemDependResult.add(getDependResultForItem(item));
            }
            DependResult modelResult = DependentUtils.getDependResultForRelation(dependentTaskModel.getRelation(),
                    itemDependResult);
            modelResultList.add(modelResult);
        }
        conditionResult = DependentUtils.getDependResultForRelation(dependentParameters.getRelation(), modelResultList);
        logger.info("the conditions task depend result : {}", conditionResult);
    }

    /**
     * depend result for depend item
     */
    private DependResult getDependResultForItem(DependentItem item) {

        DependResult dependResult = DependResult.SUCCESS;
        if (!completeTaskList.containsKey(item.getDepTaskCode())) {
            logger.info("depend item: {} have not completed yet.", item.getDepTaskCode());
            dependResult = DependResult.FAILED;
            return dependResult;
        }
        ExecutionStatus executionStatus = completeTaskList.get(item.getDepTaskCode());
        if (executionStatus != item.getStatus()) {
            logger.info("depend item : {} expect status: {}, actual status: {}", item.getDepTaskCode(),
                    item.getStatus(), executionStatus);
            dependResult = DependResult.FAILED;
        }
        logger.info("dependent item complete {} {},{}",
                Constants.DEPENDENT_SPLIT, item.getDepTaskCode(), dependResult);
        return dependResult;
    }

    /**
     *
     */
    private void endTask() {
        ExecutionStatus status = (conditionResult == DependResult.SUCCESS) ? ExecutionStatus.SUCCESS
                : ExecutionStatus.FAILURE;
        taskInstance.setState(status);
        taskInstance.setEndTime(new Date());
        processService.updateTaskInstance(taskInstance);
    }
}
