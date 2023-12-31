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

package com.mdosys.scheduler.api.service;

import com.mdosys.scheduler.api.utils.Result;
import com.mdosys.scheduler.dao.entity.User;

import java.util.Map;

/**
 * logger service
 */
public interface LoggerService {

    /**
     * view log
     *
     * @param taskInstId task instance id
     * @param skipLineNum skip line number
     * @param limit limit
     * @return log string data
     */
    Result<String> queryLog(int taskInstId, int skipLineNum, int limit);


    /**
     * get log size
     *
     * @param taskInstId task instance id
     * @return log byte array
     */
    byte[] getLogBytes(int taskInstId);

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
    Map<String, Object> queryLog(User loginUser, long projectCode, int taskInstId, int skipLineNum, int limit);

    /**
     * get log bytes
     *
     * @param loginUser   login user
     * @param projectCode project code
     * @param taskInstId  task instance id
     * @return log byte array
     */
    byte[] getLogBytes(User loginUser, long projectCode, int taskInstId);
}
