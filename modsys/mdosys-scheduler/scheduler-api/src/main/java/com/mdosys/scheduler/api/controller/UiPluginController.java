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
import com.mdosys.scheduler.api.service.UiPluginService;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.PluginType;
import com.mdosys.scheduler.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

import static com.mdosys.scheduler.api.enums.Status.QUERY_PLUGINS_ERROR;

/**
 * ui plugin controller
 * Some plugins (such as alert plugin) need to provide UI interfaces to users.
 * We use from-creat to dynamically generate UI interfaces. Related parameters are mainly provided by pluginParams.
 * From-create can generate dynamic ui based on this parameter.
 */
@Api(tags = "UI_PLUGINS_TAG")
@RestController
@RequestMapping("ui-plugins")
public class UiPluginController extends BaseController {

    @Autowired
    UiPluginService uiPluginService;

    @ApiOperation(value = "queryUiPluginsByType", notes = "QUERY_UI_PLUGINS_BY_TYPE")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pluginType", value = "pluginType", required = true, dataType = "PluginType"),
    })
    @GetMapping(value = "/query-by-type")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(QUERY_PLUGINS_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result queryUiPluginsByType(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                       @RequestParam(value = "pluginType") PluginType pluginType) {

        Map<String, Object> result = uiPluginService.queryUiPluginsByType(pluginType);
        return returnDataList(result);
    }

    @ApiOperation(value = "queryUiPluginDetailById", notes = "QUERY_UI_PLUGIN_DETAIL_BY_ID")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "PLUGIN_ID", required = true, dataType = "Int", example = "100"),
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiException(QUERY_PLUGINS_ERROR)
    @AccessLogAnnotation(ignoreRequestArgs = "loginUser")
    public Result queryUiPluginDetailById(@ApiIgnore @RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                          @PathVariable("id") Integer pluginId) {

        Map<String, Object> result = uiPluginService.queryUiPluginDetailById(pluginId);
        return returnDataList(result);
    }
}
