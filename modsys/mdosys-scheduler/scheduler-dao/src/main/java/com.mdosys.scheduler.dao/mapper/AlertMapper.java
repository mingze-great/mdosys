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

package com.mdosys.scheduler.dao.mapper;

import com.mdosys.scheduler.common.enums.AlertStatus;
import com.mdosys.scheduler.dao.entity.Alert;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * alert mapper interface
 */
public interface AlertMapper extends BaseMapper<Alert> {

    /**
     * list alert by status
     * @param alertStatus alertStatus
     * @return alert list
     */
    List<Alert> listAlertByStatus(@Param("alertStatus") AlertStatus alertStatus);

    /**
     * Insert server crash alert
     * <p>This method will ensure that there is at most one unsent alert which has the same content in the database.
     */
    void insertAlertWhenServerCrash(@Param("alert") Alert alert);

}
