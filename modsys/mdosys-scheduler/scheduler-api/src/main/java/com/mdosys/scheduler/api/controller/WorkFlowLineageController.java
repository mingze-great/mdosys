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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.mdosys.scheduler.api.aspect.AccessLogAnnotation;
import com.mdosys.scheduler.api.service.WorkFlowLineageService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.utils.ParameterUtils;
import com.mdosys.scheduler.dao.entity.User;
import com.mdosys.scheduler.dao.entity.WorkFlowLineage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

import static com.mdosys.scheduler.api.enums.Status.QUERY_WORKFLOW_LINEAGE_ERROR;
import static com.mdosys.scheduler.common.Constants.SESSION_USER;

/**
 * work flow lineage controller
 */
@Api(tags = "WORK_FLOW_LINEAGE_TAG")
@RestController
@RequestMapping("projects/{projectCode}/lineages")
public class WorkFlowLineageController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WorkFlowLineageController.class);

    @Autowired
    private WorkFlowLineageService workFlowLineageService;

    @ApiOperation(value = "queryLineageByWorkFlowName", notes = "QUERY_WORKFLOW_LINEAGE_BY_NAME_NOTES")
    @GetMapping(value = "/query-by-name")
    @ResponseStatus(HttpStatus.OK)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<List<WorkFlowLineage>> queryWorkFlowLineageByName(@ApiIgnore @RequestAttribute(value = SESSION_USER) User loginUser,
                                                                    @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                                                    @RequestParam(value = "workFlowName", required = false) String workFlowName) {
        try {
            workFlowName = ParameterUtils.handleEscapes(workFlowName);
            Map<String, Object> result = workFlowLineageService.queryWorkFlowLineageByName(projectCode, workFlowName);
            return returnDataList(result);
        } catch (Exception e) {
            logger.error(QUERY_WORKFLOW_LINEAGE_ERROR.getMsg(), e);
            return error(QUERY_WORKFLOW_LINEAGE_ERROR.getCode(), QUERY_WORKFLOW_LINEAGE_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "queryLineageByWorkFlowCode", notes = "QUERY_WORKFLOW_LINEAGE_BY_CODES_NOTES")
    @GetMapping(value = "/{workFlowCode}")
    @ResponseStatus(HttpStatus.OK)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<Map<String, Object>> queryWorkFlowLineageByCode(@ApiIgnore @RequestAttribute(value = SESSION_USER) User loginUser,
                                                                 @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                                                 @PathVariable(value = "workFlowCode", required = true) long workFlowCode) {
        try {
            Map<String, Object> result = workFlowLineageService.queryWorkFlowLineageByCode(projectCode, workFlowCode);
            return returnDataList(result);
        } catch (Exception e) {
            logger.error(QUERY_WORKFLOW_LINEAGE_ERROR.getMsg(), e);
            return error(QUERY_WORKFLOW_LINEAGE_ERROR.getCode(), QUERY_WORKFLOW_LINEAGE_ERROR.getMsg());
        }
    }

    @ApiOperation(value = "queryWorkFlowList", notes = "QUERY_WORKFLOW_LINEAGE_NOTES")
    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<Map<String, Object>> queryWorkFlowLineage(@ApiIgnore @RequestAttribute(value = SESSION_USER) User loginUser,
                                                                 @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode) {
        try {
            Map<String, Object> result = workFlowLineageService.queryWorkFlowLineage(projectCode);
            return returnDataList(result);
        } catch (Exception e) {
            logger.error(QUERY_WORKFLOW_LINEAGE_ERROR.getMsg(), e);
            return error(QUERY_WORKFLOW_LINEAGE_ERROR.getCode(), QUERY_WORKFLOW_LINEAGE_ERROR.getMsg());
        }
    }
}
