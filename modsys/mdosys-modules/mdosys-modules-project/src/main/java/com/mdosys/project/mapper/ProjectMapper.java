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

package com.mdosys.project.mapper;

import com.mdosys.project.domain.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project mapper interface
 */
@Mapper
@Repository
public interface ProjectMapper{

    /**
     * 向数据库插入一条project数据
     * @param project 要插入的数据体
     * @return 是否成功标志
     */
    int insertProjectByCode(Project project);

    /**
     * 根据code更新项目
     */
    int updateProjectByCode(Project project);

    /**
     * 根据id删除项目
     */
    int delProjectByID(@Param("projectId") long id);

    /**
     * 根据code删除项目
     */
    int delProjectByCode(@Param("projectCode") long code);

    /**
     * 查询项目根据code
     */
    Project selectProjectByCode(@Param("projectCode") long code);

    Project selectProjectByName(@Param("projectName")String projectName,@Param("userId")long userId);
    /**
     * 查询项目根据name
     */
    List<Project> queryProjectByName(@Param("projectName")String projectName,@Param("userId")long userId);

    List<Project> queryProjectByUserId(@Param("userId")long userId);
    /**
     * id
     */
    Project selectProjectById(@Param("projectId") long id);

}
