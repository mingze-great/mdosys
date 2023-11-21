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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdosys.scheduler.common.enums.DependResult;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.TaskTimeoutStrategy;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.common.model.DependentTaskModel;
import com.mdosys.scheduler.common.task.dependent.DependentParameters;
import com.mdosys.scheduler.common.utils.DependentUtils;
import com.mdosys.scheduler.common.utils.NetUtils;
import com.mdosys.scheduler.dao.entity.ProcessInstance;
import com.mdosys.scheduler.dao.entity.TaskDefinition;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.server.master.config.MasterConfig;
import com.mdosys.scheduler.server.utils.DependentExecute;
import com.mdosys.scheduler.server.utils.LogUtils;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;

import java.util.*;

import static com.mdosys.scheduler.common.Constants.DEPENDENT_SPLIT;

/**
 * dependent task processor
 */
public class DependentTaskProcessor extends BaseTaskProcessor {

    private DependentParameters dependentParameters;

    /**
     * dependent task list
     */
    private List<DependentExecute> dependentTaskList = new ArrayList<>();

    /**
     * depend item result map
     * save the result to log file
     */
    private Map<String, DependResult> dependResultMap = new HashMap<>();

    /**
     * dependent date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dependentDate;

    DependResult result;

    ProcessInstance processInstance;
    TaskDefinition taskDefinition;

    MasterConfig masterConfig = SpringApplicationContext.getBean(MasterConfig.class);

    boolean allDependentItemFinished;

    @Override
    public boolean submit(TaskInstance task, ProcessInstance processInstance, int masterTaskCommitRetryTimes,
            int masterTaskCommitInterval) {
        this.processInstance = processInstance;
        this.taskInstance = task;
        this.taskInstance = processService.submitTask(task, masterTaskCommitRetryTimes, masterTaskCommitInterval);

        if (this.taskInstance == null) {
            return false;
        }
        taskDefinition = processService.findTaskDefinition(
                taskInstance.getTaskCode(), taskInstance.getTaskDefinitionVersion());
        taskInstance.setLogPath(LogUtils.getTaskLogPath(processInstance.getProcessDefinitionCode(),
                processInstance.getProcessDefinitionVersion(),
                taskInstance.getProcessInstanceId(),
                taskInstance.getId()));
        taskInstance.setHost(NetUtils.getAddr(masterConfig.getListenPort()));
        taskInstance.setState(ExecutionStatus.RUNNING_EXECUTION);
        taskInstance.setStartTime(new Date());
        processService.updateTaskInstance(taskInstance);
        initDependParameters();
        return true;
    }

    @Override
    public ExecutionStatus taskState() {
        return this.taskInstance.getState();
    }

    @Override
    public void run() {
        if (!allDependentItemFinished) {
            allDependentItemFinished = allDependentTaskFinish();
        }
        if (allDependentItemFinished) {
            getTaskDependResult();
            endTask();
        }
    }

    @Override
    protected boolean taskTimeout() {
        TaskTimeoutStrategy taskTimeoutStrategy = taskDefinition.getTimeoutNotifyStrategy();
        if (TaskTimeoutStrategy.FAILED != taskTimeoutStrategy
                && TaskTimeoutStrategy.WARNFAILED != taskTimeoutStrategy) {
            return true;
        }
        logger.info("dependent task {} timeout, strategy {} ",
                taskInstance.getId(), taskTimeoutStrategy.getDescp());
        result = DependResult.FAILED;
        endTask();
        return true;
    }

    /**
     * init dependent parameters
     */
    private void initDependParameters() {
        this.dependentParameters = taskInstance.getDependency();
        for (DependentTaskModel taskModel : dependentParameters.getDependTaskList()) {
            this.dependentTaskList.add(new DependentExecute(taskModel.getDependItemList(), taskModel.getRelation()));
        }
        if (processInstance.getScheduleTime() != null) {
            this.dependentDate = this.processInstance.getScheduleTime();
        } else {
            this.dependentDate = new Date();
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
    protected boolean killTask() {
        this.taskInstance.setState(ExecutionStatus.KILL);
        this.taskInstance.setEndTime(new Date());
        processService.saveTaskInstance(taskInstance);
        return true;
    }

    /**
     * judge all dependent tasks finish
     *
     * @return whether all dependent tasks finish
     */
    private boolean allDependentTaskFinish() {
        boolean finish = true;
        for (DependentExecute dependentExecute : dependentTaskList) {
            for (Map.Entry<String, DependResult> entry : dependentExecute.getDependResultMap().entrySet()) {
                if (!dependResultMap.containsKey(entry.getKey())) {
                    dependResultMap.put(entry.getKey(), entry.getValue());
                    // save depend result to log
                    logger.info("dependent item complete {} {},{}", DEPENDENT_SPLIT, entry.getKey(), entry.getValue());
                }
            }
            if (!dependentExecute.finish(dependentDate)) {
                finish = false;
            }
        }
        return finish;
    }

    /**
     * get dependent result
     *
     * @return DependResult
     */
    private DependResult getTaskDependResult() {
        List<DependResult> dependResultList = new ArrayList<>();
        for (DependentExecute dependentExecute : dependentTaskList) {
            DependResult dependResult = dependentExecute.getModelDependResult(dependentDate);
            dependResultList.add(dependResult);
        }
        result = DependentUtils.getDependResultForRelation(this.dependentParameters.getRelation(), dependResultList);
        logger.info("dependent task completed, dependent result:{}", result);
        return result;
    }

    /**
     *
     */
    private void endTask() {
        ExecutionStatus status;
        status = (result == DependResult.SUCCESS) ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE;
        taskInstance.setState(status);
        taskInstance.setEndTime(new Date());
        processService.saveTaskInstance(taskInstance);
    }

    @Override
    public String getType() {
        return TaskType.DEPENDENT.getDesc();
    }
}
