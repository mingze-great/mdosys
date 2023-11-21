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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.mdosys.scheduler.api.aspect.AccessLogAnnotation;
import com.mdosys.scheduler.api.exceptions.ApiException;
import com.mdosys.scheduler.api.service.TenantService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.utils.ParameterUtils;
import com.mdosys.scheduler.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

import static com.mdosys.scheduler.api.enums.Status.*;

/**
 * tenant controller
 */
@Api(tags = "TENANT_TAG")
@RestController
@RequestMapping("/tenants")
public class TenantController extends BaseController {

    @Autowired
    private TenantService tenantService;

    /**
     * create tenant
     *
     * @param loginUser login user
     * @param tenantCode tenant code
     * @param queueId queue id
     * @param description description
     * @return create result code
     */
    @ApiOperation(value = "createTenant", notes = "CREATE_TENANT_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tenantCode", value = "TENANT_CODE", required = true, dataType = "String"),
        @ApiImplicitParam(name = "queueId", value = "QUEUE_ID", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "description", value = "TENANT_DESC", dataType = "String")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(CREATE_TENANT_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result createTenant(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                               @RequestParam(value = "tenantCode") String tenantCode,
                               @RequestParam(value = "queueId") int queueId,
                               @RequestParam(value = "description", required = false) String description) throws Exception {

        Map<String, Object> result = tenantService.createTenant(loginUser, tenantCode, queueId, description);
        return returnDataList(result);
    }

    /**
     * query tenant list paging
     *
     * @param loginUser login user
     * @param searchVal search value
     * @param pageNo page number
     * @param pageSize page size
     * @return tenant list page
     */
    @ApiOperation(value = "queryTenantlistPaging", notes = "QUERY_TENANT_LIST_PAGING_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "searchVal", value = "SEARCH_VAL", dataType = "String"),
        @ApiImplicitParam(name = "pageNo", value = "PAGE_NO", required = true, dataType = "Int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "PAGE_SIZE", required = true, dataType = "Int", example = "20")
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiException(QUERY_TENANT_LIST_PAGING_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result queryTenantlistPaging(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                        @RequestParam(value = "searchVal", required = false) String searchVal,
                                        @RequestParam("pageNo") Integer pageNo,
                                        @RequestParam("pageSize") Integer pageSize) {
        Result result = checkPageParams(pageNo, pageSize);
        if (!result.checkResult()) {
            return result;

        }
        searchVal = ParameterUtils.handleEscapes(searchVal);
        result = tenantService.queryTenantList(loginUser, searchVal, pageNo, pageSize);
        return result;
    }


    /**
     * tenant list
     *
     * @param loginUser login user
     * @return tenant list
     */
    @ApiOperation(value = "queryTenantlist", notes = "QUERY_TENANT_LIST_NOTES")
    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(QUERY_TENANT_LIST_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result queryTenantlist(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser) {
        Map<String, Object> result = tenantService.queryTenantList(loginUser);
        return returnDataList(result);
    }


    /**
     * update tenant
     *
     * @param loginUser login user
     * @param id tenant id
     * @param tenantCode tenant code
     * @param queueId queue id
     * @param description description
     * @return update result code
     */
    @ApiOperation(value = "updateTenant", notes = "UPDATE_TENANT_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "TENANT_ID", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "tenantCode", value = "TENANT_CODE", required = true, dataType = "String"),
        @ApiImplicitParam(name = "queueId", value = "QUEUE_ID", required = true, dataType = "Int", example = "100"),
        @ApiImplicitParam(name = "description", value = "TENANT_DESC", type = "String")
    })
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(UPDATE_TENANT_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result updateTenant(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                               @PathVariable(value = "id") int id,
                               @RequestParam(value = "tenantCode") String tenantCode,
                               @RequestParam(value = "queueId") int queueId,
                               @RequestParam(value = "description", required = false) String description) throws Exception {

        Map<String, Object> result = tenantService.updateTenant(loginUser, id, tenantCode, queueId, description);
        return returnDataList(result);
    }

    /**
     * delete tenant by id
     *
     * @param loginUser login user
     * @param id tenant id
     * @return delete result code
     */
    @ApiOperation(value = "deleteTenantById", notes = "DELETE_TENANT_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "TENANT_ID", required = true, dataType = "Int", example = "100")
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(DELETE_TENANT_BY_ID_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result deleteTenantById(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @PathVariable(value = "id") int id) throws Exception {
        Map<String, Object> result = tenantService.deleteTenantById(loginUser, id);
        return returnDataList(result);
    }

    /**
     * verify tenant code
     *
     * @param loginUser login user
     * @param tenantCode tenant code
     * @return true if tenant code can user, otherwise return false
     */
    @ApiOperation(value = "verifyTenantCode", notes = "VERIFY_TENANT_CODE_NOTES")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tenantCode", value = "TENANT_CODE", required = true, dataType = "String")
    })
    @GetMapping(value = "/verify-code")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(VERIFY_OS_TENANT_CODE_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result verifyTenantCode(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @RequestParam(value = "tenantCode") String tenantCode) {
        return tenantService.verifyTenantCode(tenantCode);
    }

}