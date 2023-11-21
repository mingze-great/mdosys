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
import com.mdosys.scheduler.api.service.TaskInstanceService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.ExecutionStatus;
import com.mdosys.scheduler.common.utils.ParameterUtils;
import com.mdosys.scheduler.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

import static com.mdosys.scheduler.api.enums.Status.FORCE_TASK_SUCCESS_ERROR;
import static com.mdosys.scheduler.api.enums.Status.QUERY_TASK_LIST_PAGING_ERROR;

/**
 * task instance controller
 */
@Api(tags = "TASK_INSTANCE_TAG")
@RestController
@RequestMapping("/projects/{projectCode}/task-instances")
public class TaskInstanceController extends BaseController {

    @Autowired
    TaskInstanceService taskInstanceService;

    /**
     * query task list paging
     *
     * @param loginUser login user
     * @param projectCode project code
     * @param processInstanceId process instance id
     * @param searchVal search value
     * @param taskName task name
     * @param stateType state type
     * @param host host
     * @param startTime start time
     * @param endTime end time
     * @param pageNo page number
     * @param pageSize page size
     * @return task list page
     */
    @ApiOperation(value = "queryTaskListPaging", notes = "QUERY_TASK_INSTANCE_LIST_PAGING_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "processInstanceId", value = "PROCESS_INSTANCE_ID", required = false, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "processInstanceName", value = "PROCESS_INSTANCE_NAME", required = false, type = "String"),
        @ApiImplicitParam(name = "searchVal", value = "SEARCH_VAL", type = "String"),
        @ApiImplicitParam(name = "taskName", value = "TASK_NAME", type = "String"),
        @ApiImplicitParam(name = "executorName", value = "EXECUTOR_NAME", type = "String"),
        @ApiImplicitParam(name = "stateType", value = "EXECUTION_STATUS", type = "ExecutionStatus"),
        @ApiImplicitParam(name = "host", value = "HOST", type = "String"),
        @ApiImplicitParam(name = "startDate", value = "START_DATE", type = "String"),
        @ApiImplicitParam(name = "endDate", value = "END_DATE", type = "String"),
        @ApiImplicitParam(name = "pageNo", value = "PAGE_NO", required = true, dataType = "Int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "PAGE_SIZE", required = true, dataType = "Int", example = "20")
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(QUERY_TASK_LIST_PAGING_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result queryTaskListPaging(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                      @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                      @RequestParam(value = "processInstanceId", required = false, defaultValue = "0") Integer processInstanceId,
                                      @RequestParam(value = "processInstanceName", required = false) String processInstanceName,
                                      @RequestParam(value = "searchVal", required = false) String searchVal,
                                      @RequestParam(value = "taskName", required = false) String taskName,
                                      @RequestParam(value = "executorName", required = false) String executorName,
                                      @RequestParam(value = "stateType", required = false) ExecutionStatus stateType,
                                      @RequestParam(value = "host", required = false) String host,
                                      @RequestParam(value = "startDate", required = false) String startTime,
                                      @RequestParam(value = "endDate", required = false) String endTime,
                                      @RequestParam("pageNo") Integer pageNo,
                                      @RequestParam("pageSize") Integer pageSize) {
        Result result = checkPageParams(pageNo, pageSize);
        if (!result.checkResult()) {
            return result;
        }
        searchVal = ParameterUtils.handleEscapes(searchVal);
        result = taskInstanceService.queryTaskListPaging(loginUser, projectCode, processInstanceId, processInstanceName,
                taskName, executorName, startTime, endTime, searchVal, stateType, host, pageNo, pageSize);
        return result;
    }

    /**
     * change one task instance's state from FAILURE to FORCED_SUCCESS
     *
     * @param loginUser login user
     * @param projectCode project code
     * @param id task instance id
     * @return the result code and msg
     */
    @ApiOperation(value = "force-success", notes = "FORCE_TASK_SUCCESS")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "TASK_INSTANCE_ID", required = true, dataType = "Int", example = "12")
    })
    @PostMapping(value = "/{id}/force-success")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(FORCE_TASK_SUCCESS_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result<Object> forceTaskSuccess(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                           @ApiParam(name = "projectCode", value = "PROJECT_CODE", required = true) @PathVariable long projectCode,
                                           @PathVariable(value = "id") Integer id) {
        Map<String, Object> result = taskInstanceService.forceTaskSuccess(loginUser, projectCode, id);
        return returnDataList(result);
    }

}
