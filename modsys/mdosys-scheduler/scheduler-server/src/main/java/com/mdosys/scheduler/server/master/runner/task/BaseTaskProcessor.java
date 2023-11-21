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

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.enums.SqoopJobType;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.common.enums.UdfType;
import com.mdosys.scheduler.common.process.ResourceInfo;
import com.mdosys.scheduler.common.task.AbstractParameters;
import com.mdosys.scheduler.common.task.datax.DataxParameters;
import com.mdosys.scheduler.common.task.procedure.ProcedureParameters;
import com.mdosys.scheduler.common.task.sql.SqlParameters;
import com.mdosys.scheduler.common.task.sqoop.SqoopParameters;
import com.mdosys.scheduler.common.task.sqoop.sources.SourceMysqlParameter;
import com.mdosys.scheduler.common.task.sqoop.targets.TargetMysqlParameter;
import com.mdosys.scheduler.common.utils.HadoopUtils;
import com.mdosys.scheduler.common.utils.JSONUtils;
import com.mdosys.scheduler.common.utils.TaskParametersUtils;
import com.mdosys.scheduler.dao.entity.*;
import com.mdosys.scheduler.server.builder.TaskExecutionContextBuilder;
import com.mdosys.scheduler.service.bean.SpringApplicationContext;
import com.mdosys.scheduler.service.process.ProcessService;
import com.mdosys.scheduler.service.queue.entity.TaskExecutionContext;
import com.mdosys.scheduler.spi.enums.ResourceType;
import com.mdosys.scheduler.spi.task.TaskConstants;
import com.mdosys.scheduler.spi.task.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseTaskProcessor implements ITaskProcessor {

    protected final Logger logger = LoggerFactory
            .getLogger(String.format(TaskConstants.TASK_LOG_LOGGER_NAME, getClass()));

    protected boolean killed = false;

    protected boolean paused = false;

    protected boolean timeout = false;

    protected TaskInstance taskInstance = null;

    protected ProcessInstance processInstance;

    protected ProcessService processService = SpringApplicationContext.getBean(ProcessService.class);

    /**
     * pause task, common tasks donot need this.
     *
     * @return
     */
    protected abstract boolean pauseTask();

    /**
     * kill task, all tasks need to realize this function
     *
     * @return
     */
    protected abstract boolean killTask();

    /**
     * task timeout process
     * 
     * @return
     */
    protected abstract boolean taskTimeout();

    @Override
    public void run() {
    }

    @Override
    public boolean action(TaskAction taskAction) {

        switch (taskAction) {
            case STOP:
                return stop();
            case PAUSE:
                return pause();
            case TIMEOUT:
                return timeout();
            default:
                logger.error("unknown task action: {}", taskAction.toString());

        }
        return false;
    }

    protected boolean timeout() {
        if (timeout) {
            return true;
        }
        timeout = taskTimeout();
        return timeout;
    }

    /**
     * @return
     */
    protected boolean pause() {
        if (paused) {
            return true;
        }
        paused = pauseTask();
        return paused;
    }

    protected boolean stop() {
        if (killed) {
            return true;
        }
        killed = killTask();
        return killed;
    }

    @Override
    public String getType() {
        return null;
    }

    /**
     * get TaskExecutionContext
     *
     * @param taskInstance taskInstance
     * @return TaskExecutionContext
     */
    protected TaskExecutionContext getTaskExecutionContext(TaskInstance taskInstance) {
        processService.setTaskInstanceDetail(taskInstance);

        int userId = taskInstance.getProcessDefine() == null ? 0 : taskInstance.getProcessDefine().getUserId();
        Tenant tenant = processService.getTenantForProcess(taskInstance.getProcessInstance().getTenantId(), userId);

        // verify tenant is null
        if (verifyTenantIsNull(tenant, taskInstance)) {
            processService.changeTaskState(taskInstance, ExecutionStatus.FAILURE,
                    taskInstance.getStartTime(),
                    taskInstance.getHost(),
                    null,
                    null,
                    taskInstance.getId());
            return null;
        }
        // set queue for process instance, user-specified queue takes precedence over
        // tenant queue
        String userQueue = processService.queryUserQueueByProcessInstanceId(taskInstance.getProcessInstanceId());
        taskInstance.getProcessInstance().setQueue(StringUtils.isEmpty(userQueue) ? tenant.getQueue() : userQueue);
        taskInstance.getProcessInstance().setTenantCode(tenant.getTenantCode());
        taskInstance.setResources(getResourceFullNames(taskInstance));

        SQLTaskExecutionContext sqlTaskExecutionContext = new SQLTaskExecutionContext();
        DataxTaskExecutionContext dataxTaskExecutionContext = new DataxTaskExecutionContext();
        ProcedureTaskExecutionContext procedureTaskExecutionContext = new ProcedureTaskExecutionContext();
        SqoopTaskExecutionContext sqoopTaskExecutionContext = new SqoopTaskExecutionContext();

        // SQL task
        if (TaskType.SQL.getDesc().equalsIgnoreCase(taskInstance.getTaskType())) {
            setSQLTaskRelation(sqlTaskExecutionContext, taskInstance);
        }

        // DATAX task
        if (TaskType.DATAX.getDesc().equalsIgnoreCase(taskInstance.getTaskType())) {
            setDataxTaskRelation(dataxTaskExecutionContext, taskInstance);
        }

        // procedure task
        if (TaskType.PROCEDURE.getDesc().equalsIgnoreCase(taskInstance.getTaskType())) {
            setProcedureTaskRelation(procedureTaskExecutionContext, taskInstance);
        }

        if (TaskType.SQOOP.getDesc().equalsIgnoreCase(taskInstance.getTaskType())) {
            setSqoopTaskRelation(sqoopTaskExecutionContext, taskInstance);
        }

        return TaskExecutionContextBuilder.get()
                .buildTaskInstanceRelatedInfo(taskInstance)
                .buildTaskDefinitionRelatedInfo(taskInstance.getTaskDefine())
                .buildProcessInstanceRelatedInfo(taskInstance.getProcessInstance())
                .buildProcessDefinitionRelatedInfo(taskInstance.getProcessDefine())
                .buildSQLTaskRelatedInfo(sqlTaskExecutionContext)
                .buildDataxTaskRelatedInfo(dataxTaskExecutionContext)
                .buildProcedureTaskRelatedInfo(procedureTaskExecutionContext)
                .buildSqoopTaskRelatedInfo(sqoopTaskExecutionContext)
                .create();
    }

    /**
     * set procedure task relation
     *
     * @param procedureTaskExecutionContext procedureTaskExecutionContext
     * @param taskInstance                  taskInstance
     */
    private void setProcedureTaskRelation(ProcedureTaskExecutionContext procedureTaskExecutionContext,
            TaskInstance taskInstance) {
        ProcedureParameters procedureParameters = JSONUtils.parseObject(taskInstance.getTaskParams(),
                ProcedureParameters.class);
        int datasourceId = procedureParameters.getDatasource();
        DataSource datasource = processService.findDataSourceById(datasourceId);
        procedureTaskExecutionContext.setConnectionParams(datasource.getConnectionParams());
    }

    /**
     * set datax task relation
     *
     * @param dataxTaskExecutionContext dataxTaskExecutionContext
     * @param taskInstance              taskInstance
     */
    protected void setDataxTaskRelation(DataxTaskExecutionContext dataxTaskExecutionContext,
            TaskInstance taskInstance) {
        DataxParameters dataxParameters = JSONUtils.parseObject(taskInstance.getTaskParams(), DataxParameters.class);

        DataSource dbSource = processService.findDataSourceById(dataxParameters.getDataSource());
        DataSource dbTarget = processService.findDataSourceById(dataxParameters.getDataTarget());

        if (dbSource != null) {
            dataxTaskExecutionContext.setDataSourceId(dataxParameters.getDataSource());
            dataxTaskExecutionContext.setSourcetype(dbSource.getType().getCode());
            dataxTaskExecutionContext.setSourceConnectionParams(dbSource.getConnectionParams());
        }

        if (dbTarget != null) {
            dataxTaskExecutionContext.setDataTargetId(dataxParameters.getDataTarget());
            dataxTaskExecutionContext.setTargetType(dbTarget.getType().getCode());
            dataxTaskExecutionContext.setTargetConnectionParams(dbTarget.getConnectionParams());
        }
    }

    /**
     * set sqoop task relation
     *
     * @param sqoopTaskExecutionContext sqoopTaskExecutionContext
     * @param taskInstance              taskInstance
     */
    private void setSqoopTaskRelation(SqoopTaskExecutionContext sqoopTaskExecutionContext, TaskInstance taskInstance) {
        SqoopParameters sqoopParameters = JSONUtils.parseObject(taskInstance.getTaskParams(), SqoopParameters.class);

        // sqoop job type is template set task relation
        if (sqoopParameters.getJobType().equals(SqoopJobType.TEMPLATE.getDescp())) {
            SourceMysqlParameter sourceMysqlParameter = JSONUtils.parseObject(sqoopParameters.getSourceParams(),
                    SourceMysqlParameter.class);
            TargetMysqlParameter targetMysqlParameter = JSONUtils.parseObject(sqoopParameters.getTargetParams(),
                    TargetMysqlParameter.class);

            DataSource dataSource = processService.findDataSourceById(sourceMysqlParameter.getSrcDatasource());
            DataSource dataTarget = processService.findDataSourceById(targetMysqlParameter.getTargetDatasource());

            if (dataSource != null) {
                sqoopTaskExecutionContext.setDataSourceId(dataSource.getId());
                sqoopTaskExecutionContext.setSourcetype(dataSource.getType().getCode());
                sqoopTaskExecutionContext.setSourceConnectionParams(dataSource.getConnectionParams());
            }

            if (dataTarget != null) {
                sqoopTaskExecutionContext.setDataTargetId(dataTarget.getId());
                sqoopTaskExecutionContext.setTargetType(dataTarget.getType().getCode());
                sqoopTaskExecutionContext.setTargetConnectionParams(dataTarget.getConnectionParams());
            }
        }
    }

    /**
     * set SQL task relation
     *
     * @param sqlTaskExecutionContext sqlTaskExecutionContext
     * @param taskInstance            taskInstance
     */
    private void setSQLTaskRelation(SQLTaskExecutionContext sqlTaskExecutionContext, TaskInstance taskInstance) {
        SqlParameters sqlParameters = JSONUtils.parseObject(taskInstance.getTaskParams(), SqlParameters.class);
        int datasourceId = sqlParameters.getDatasource();
        DataSource datasource = processService.findDataSourceById(datasourceId);
        sqlTaskExecutionContext.setConnectionParams(datasource.getConnectionParams());

        sqlTaskExecutionContext.setDefaultFS(HadoopUtils.getInstance().getDefaultFS());

        // whether udf type
        boolean udfTypeFlag = Enums.getIfPresent(UdfType.class, Strings.nullToEmpty(sqlParameters.getType()))
                .isPresent()
                && !StringUtils.isEmpty(sqlParameters.getUdfs());

        if (udfTypeFlag) {
            String[] udfFunIds = sqlParameters.getUdfs().split(",");
            int[] udfFunIdsArray = new int[udfFunIds.length];
            for (int i = 0; i < udfFunIds.length; i++) {
                udfFunIdsArray[i] = Integer.parseInt(udfFunIds[i]);
            }

            List<UdfFunc> udfFuncList = processService.queryUdfFunListByIds(udfFunIdsArray);
            UdfFuncRequest udfFuncRequest;
            Map<UdfFuncRequest, String> udfFuncRequestMap = new HashMap<>();
            for (UdfFunc udfFunc : udfFuncList) {
                udfFuncRequest = JSONUtils.parseObject(JSONUtils.toJsonString(udfFunc), UdfFuncRequest.class);
                String tenantCode = processService.queryTenantCodeByResName(udfFunc.getResourceName(),
                        ResourceType.UDF);
                udfFuncRequestMap.put(udfFuncRequest, tenantCode);
            }
            sqlTaskExecutionContext.setUdfFuncTenantCodeMap(udfFuncRequestMap);
        }
    }

    /**
     * whehter tenant is null
     *
     * @param tenant       tenant
     * @param taskInstance taskInstance
     * @return result
     */
    protected boolean verifyTenantIsNull(Tenant tenant, TaskInstance taskInstance) {
        if (tenant == null) {
            logger.error("tenant not exists,process instance id : {},task instance id : {}",
                    taskInstance.getProcessInstance().getId(),
                    taskInstance.getId());
            return true;
        }
        return false;
    }

    /**
     * get resource map key is full name and value is tenantCode
     */
    protected Map<String, String> getResourceFullNames(TaskInstance taskInstance) {
        Map<String, String> resourcesMap = new HashMap<>();
        AbstractParameters baseParam = TaskParametersUtils.getParameters(taskInstance.getTaskType(),
                taskInstance.getTaskParams());

        if (baseParam != null) {
            List<ResourceInfo> projectResourceFiles = baseParam.getResourceFilesList();
            if (CollectionUtils.isNotEmpty(projectResourceFiles)) {

                // filter the resources that the resource id equals 0
                Set<ResourceInfo> oldVersionResources = projectResourceFiles.stream().filter(t -> t.getId() == 0)
                        .collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(oldVersionResources)) {
                    oldVersionResources.forEach(t -> resourcesMap.put(t.getRes(),
                            processService.queryTenantCodeByResName(t.getRes(), ResourceType.FILE)));
                }

                // get the resource id in order to get the resource names in batch
                Stream<Integer> resourceIdStream = projectResourceFiles.stream().map(ResourceInfo::getId);
                Set<Integer> resourceIdsSet = resourceIdStream.collect(Collectors.toSet());

                if (CollectionUtils.isNotEmpty(resourceIdsSet)) {
                    Integer[] resourceIds = resourceIdsSet.toArray(new Integer[resourceIdsSet.size()]);

                    List<Resource> resources = processService.listResourceByIds(resourceIds);
                    resources.forEach(t -> resourcesMap.put(t.getFullName(),
                            processService.queryTenantCodeByResName(t.getFullName(), ResourceType.FILE)));
                }
            }
        }

        return resourcesMap;
    }
}
