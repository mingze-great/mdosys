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

import com.mdosys.scheduler.api.enums.Status;
import com.mdosys.scheduler.api.service.impl.WorkFlowLineageServiceImpl;
import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.common.Constants;
import com.mdosys.scheduler.common.enums.UserType;
import com.mdosys.scheduler.dao.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * work flow lineage controller test
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkFlowLineageControllerTest {

    @InjectMocks
    private WorkFlowLineageController workFlowLineageController;

    @Mock
    private WorkFlowLineageServiceImpl workFlowLineageService;

    protected User user;

    @Before
    public void before() {
        User loginUser = new User();
        loginUser.setId(1);
        loginUser.setUserType(UserType.GENERAL_USER);
        loginUser.setUserName("admin");
        user = loginUser;
    }

    private void putMsg(Map<String, Object> result, Status status, Object... statusParams) {
        result.put(Constants.STATUS, status);
        if (statusParams != null && statusParams.length > 0) {
            result.put(Constants.MSG, MessageFormat.format(status.getMsg(), statusParams));
        } else {
            result.put(Constants.MSG, status.getMsg());
        }
    }

    @Test
    public void testQueryWorkFlowLineageByName() {
        long projectCode = 1L;
        String searchVal = "test";
        Map<String, Object> result = new HashMap<>();
        putMsg(result, Status.SUCCESS);
        result.put(Constants.DATA_LIST, 1);
        Mockito.when(workFlowLineageService.queryWorkFlowLineageByName(projectCode, searchVal)).thenReturn(result);
        Result response = workFlowLineageController.queryWorkFlowLineageByName(user, projectCode, searchVal);
        Assert.assertEquals(Status.SUCCESS.getCode(), response.getCode().intValue());
    }

    @Test
    public  void testQueryWorkFlowLineageByCode() {
        long projectCode = 1L;
        long code = 1L;
        Map<String, Object> result = new HashMap<>();
        putMsg(result, Status.SUCCESS);
        result.put(Constants.DATA_LIST, 1);
        Mockito.when(workFlowLineageService.queryWorkFlowLineageByCode(projectCode, code)).thenReturn(result);
        Result response = workFlowLineageController.queryWorkFlowLineageByCode(user, projectCode, code);
        Assert.assertEquals(Status.SUCCESS.getCode(), response.getCode().intValue());
    }
}
