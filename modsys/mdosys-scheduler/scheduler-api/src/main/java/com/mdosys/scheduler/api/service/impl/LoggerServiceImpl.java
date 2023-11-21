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

package com.mdosys.scheduler.api.service.impl;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang.StringUtils;
import com.mdosys.scheduler.api.enums.Status;
import com.mdosys.scheduler.api.exceptions.ServiceException;
import com.mdosys.scheduler.api.service.LoggerService;
import com.mdosys.scheduler.api.service.ProjectService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.dao.entity.Project;
import com.mdosys.scheduler.dao.entity.TaskDefinition;
import com.mdosys.scheduler.dao.entity.TaskInstance;
import com.mdosys.scheduler.dao.entity.User;
import com.mdosys.scheduler.dao.mapper.ProjectMapper;
import com.mdosys.scheduler.dao.mapper.TaskDefinitionMapper;
import com.mdosys.scheduler.remote.utils.Host;
import com.mdosys.scheduler.service.log.LogClientService;
import com.mdosys.scheduler.service.process.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * logger service impl
 */
@Service
public class LoggerServiceImpl extends BaseServiceImpl implements LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerServiceImpl.class);

    private static final String LOG_HEAD_FORMAT = "[LOG-PATH]: %s, [HOST]:  %s%s";

    @Autowired
    private ProcessService processService;

    private LogClientService logClient;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    ProjectService projectService;

    @Autowired
    TaskDefinitionMapper taskDefinitionMapper;

    @PostConstruct
    public void init() {
        if (Objects.isNull(this.logClient)) {
            this.logClient = new LogClientService();
        }
    }

    @PreDestroy
    public void close() {
        if (Objects.nonNull(this.logClient) && this.logClient.isRunning()) {
            logClient.close();
        }
    }

    /**
     * view log
     *
     * @param taskInstId task instance id
     * @param skipLineNum skip line number
     * @param limit limit
     * @return log string data
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<String> queryLog(int taskInstId, int skipLineNum, int limit) {

        TaskInstance taskInstance = processService.findTaskInstanceById(taskInstId);

        if (taskInstance == null || StringUtils.isBlank(taskInstance.getHost())) {
            return Result.error(Status.TASK_INSTANCE_NOT_FOUND);
        }
        Result<String> result = new Result<>(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg());
        String log = queryLog(taskInstance,skipLineNum,limit);
        result.setData(log);
        return result;
    }


    /**
     * get log size
     *
     * @param taskInstId task instance id
     * @return log byte array
     */
    @Override
    public byte[] getLogBytes(int taskInstId) {
        TaskInstance taskInstance = processService.findTaskInstanceById(taskInstId);
        if (taskInstance == null || StringUtils.isBlank(taskInstance.getHost())) {
            throw new ServiceException("task instance is null or host is null");
        }
        return getLogBytes(taskInstance);
    }

    /**
     * query log
     *
     * @param loginUser   login user
     * @param projectCode project code
     * @param taskInstId  task instance id
     * @param skipLineNum skip line number
     * @param limit       limit
     * @return log string data
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryLog(User loginUser, long projectCode, int taskInstId, int skipLineNum, int limit) {
        Project project = projectMapper.queryByCode(projectCode);
        //check user access for project
        Map<String, Object> result = projectService.checkProjectAndAuth(loginUser, project, projectCode);
        if (result.get(Constants.STATUS) != Status.SUCCESS) {
            return result;
        }
        // check whether the task instance can be found
        TaskInstance task = processService.findTaskInstanceById(taskInstId);
        if (task == null || StringUtils.isBlank(task.getHost())) {
            putMsg(result, Status.TASK_INSTANCE_NOT_FOUND);
            return result;
        }

        TaskDefinition taskDefinition = taskDefinitionMapper.queryByCode(task.getTaskCode());
        if (taskDefinition != null && projectCode != taskDefinition.getProjectCode()) {
            putMsg(result, Status.TASK_INSTANCE_NOT_FOUND, taskInstId);
            return result;
        }
        String log = queryLog(task, skipLineNum, limit);
        result.put(Constants.DATA_LIST, log);
        return result;
    }

    /**
     * get log bytes
     *
     * @param loginUser   login user
     * @param projectCode project code
     * @param taskInstId  task instance id
     * @return log byte array
     */
    @Override
    public byte[] getLogBytes(User loginUser, long projectCode, int taskInstId) {
        Project project = projectMapper.queryByCode(projectCode);
        //check user access for project
        Map<String, Object> result = projectService.checkProjectAndAuth(loginUser, project, projectCode);
        if (result.get(Constants.STATUS) != Status.SUCCESS) {
            throw new ServiceException("user has no permission");
        }
        // check whether the task instance can be found
        TaskInstance task = processService.findTaskInstanceById(taskInstId);
        if (task == null || StringUtils.isBlank(task.getHost())) {
            throw new ServiceException("task instance is null or host is null");
        }

        TaskDefinition taskDefinition = taskDefinitionMapper.queryByCode(task.getTaskCode());
        if (taskDefinition != null && projectCode != taskDefinition.getProjectCode()) {
            throw new ServiceException("task instance does not exist in project");
        }
        return getLogBytes(task);
    }

    /**
     * get host
     *
     * @param address address
     * @return old version return true ,otherwise return false
     */
    private String getHost(String address) {
        if (Boolean.TRUE.equals(Host.isOldVersion(address))) {
            return address;
        }
        return Host.of(address).getIp();
    }

    /**
     * query log
     *
     * @param taskInstance  task instance
     * @param skipLineNum skip line number
     * @param limit       limit
     * @return log string data
     */
    private String queryLog(TaskInstance taskInstance, int skipLineNum, int limit) {

        String host = getHost(taskInstance.getHost());

        logger.info("log host : {} , logPath : {} , logServer port : {}", host, taskInstance.getLogPath(),
                Constants.RPC_PORT);

        StringBuilder log = new StringBuilder();
        if (skipLineNum == 0) {
            String head = String.format(LOG_HEAD_FORMAT,
                    taskInstance.getLogPath(),
                    host,
                    Constants.SYSTEM_LINE_SEPARATOR);
            log.append(head);
        }

        log.append(logClient
                .rollViewLog(host, Constants.RPC_PORT, taskInstance.getLogPath(), skipLineNum, limit));

        return log.toString();
    }

    /**
     * get log bytes
     *
     * @param taskInstance task instance
     * @return log byte array
     */
    private byte[] getLogBytes(TaskInstance taskInstance) {
        String host = getHost(taskInstance.getHost());
        byte[] head = String.format(LOG_HEAD_FORMAT,
                taskInstance.getLogPath(),
                host,
                Constants.SYSTEM_LINE_SEPARATOR).getBytes(StandardCharsets.UTF_8);
        return Bytes.concat(head,
                logClient.getLogBytes(host, Constants.RPC_PORT, taskInstance.getLogPath()));
    }
}
