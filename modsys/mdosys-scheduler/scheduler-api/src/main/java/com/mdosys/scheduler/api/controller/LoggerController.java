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

package com.mdosys.scheduler.api.controller;

import io.swagger.annotations.*;
import com.mdosys.scheduler.api.aspect.AccessLogAnnotation;
import com.mdosys.scheduler.api.exceptions.ApiException;
import com.mdosys.scheduler.api.service.LoggerService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.mdosys.scheduler.api.enums.Status.DOWNLOAD_TASK_INSTANCE_LOG_FILE_ERROR;
import static com.mdosys.scheduler.api.enums.Status.QUERY_TASK_INSTANCE_LOG_ERROR;

/**
 * logger controller
 */
@Api(tags = "LOGGER_TAG")
@RestController
@RequestMapping("/log")
public class LoggerController extends BaseController {

    @Autowired
    private LoggerService loggerService;

    /**
     * query task log
     *
     * @param loginUser login user
     * @param taskInstanceId task instance id
     * @param skipNum skip number
     * @param limit limit
     * @return task log content
     */
    @ApiOperation(value = "queryLog", notes = "QUERY_TASK_INSTANCE_LOG_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskInstanceId", value = "TASK_ID", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "skipLineNum", value = "SKIP_LINE_NUM", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "limit", value = "LIMIT", required = true, dataType = "Int", example = "100")
    })
    @GetMapping(value = "/detail")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(QUERY_TASK_INSTANCE_LOG_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<String> queryLog(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @RequestParam(value = "taskInstanceId") int taskInstanceId,
                                   @RequestParam(value = "skipLineNum") int skipNum,
                                   @RequestParam(value = "limit") int limit) {
        return loggerService.queryLog(taskInstanceId, skipNum, limit);
    }

    /**
     * download log file
     *
     * @param loginUser login user
     * @param taskInstanceId task instance id
     * @return log file content
     */
    @ApiOperation(value = "downloadTaskLog", notes = "DOWNLOAD_TASK_INSTANCE_LOG_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskInstanceId", value = "TASK_ID", required = true, dataType = "Int", example = "100")
    })
    @GetMapping(value = "/download-log")
    @ResponseBody
    @ApiException(DOWNLOAD_TASK_INSTANCE_LOG_FILE_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public ResponseEntity downloadTaskLog(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                          @RequestParam(value = "taskInstanceId") int taskInstanceId) {
        byte[] logBytes = loggerService.getLogBytes(taskInstanceId);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + System.currentTimeMillis() + ".log" + "\"")
            .body(logBytes);
    }

    /**
     * query task log in specified project
     *
     * @param loginUser      login user
     * @param projectCode project code
     * @param taskInstanceId task instance id
     * @param skipNum        skip number
     * @param limit          limit
     * @return task log content
     */
    @ApiOperation(value = "queryLogInSpecifiedProject", notes = "QUERY_TASK_INSTANCE_LOG_IN_SPECIFIED_PROJECT_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectCode", value = "PROJECT_CODE", required = true, type = "Long"),
        @ApiImplicitParam(name = "taskInstanceId", value = "TASK_ID", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "skipLineNum", value = "SKIP_LINE_NUM", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "limit", value = "LIMIT", required = true, dataType = "Int", example = "100")
    })
    @GetMapping(value = "/{projectCode}/detail")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(QUERY_TASK_INSTANCE_LOG_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<String> queryLog(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                   @RequestParam(value = "taskInstanceId") int taskInstanceId,
                                   @RequestParam(value = "skipLineNum") int skipNum,
                                   @RequestParam(value = "limit") int limit) {
        return returnDataList(loggerService.queryLog(loginUser, projectCode, taskInstanceId, skipNum, limit));
    }

    /**
     * download log file
     *
     * @param loginUser      login user
     * @param projectCode    project code
     * @param taskInstanceId task instance id
     * @return log file content
     */
    @ApiOperation(value = "downloadTaskLogInSpecifiedProject", notes = "DOWNLOAD_TASK_INSTANCE_LOG_IN_SPECIFIED_PROJECT_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectCode", value = "PROJECT_CODE", required = true, type = "Long"),
        @ApiImplicitParam(name = "taskInstanceId", value = "TASK_ID", required = true, dataType = "Int", example = "100")
    })
    @GetMapping(value = "/{projectCode}/download-log")
    @ResponseBody
    @ApiException(DOWNLOAD_TASK_INSTANCE_LOG_FILE_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public ResponseEntity downloadTaskLog(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                          @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                          @RequestParam(value = "taskInstanceId") int taskInstanceId) {
        byte[] logBytes = loggerService.getLogBytes(loginUser, projectCode, taskInstanceId);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + System.currentTimeMillis() + ".log" + "\"")
                .body(logBytes);
    }
}
