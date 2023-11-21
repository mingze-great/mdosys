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

package com.mdosys.scheduler.plugin.task.sqoop.generator.sources;

import com.mdosys.scheduler.plugin.datasource.api.utils.DatasourceUtil;
import com.mdosys.scheduler.plugin.task.sqoop.SqoopQueryType;
import com.mdosys.scheduler.plugin.task.sqoop.generator.ISourceGenerator;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.SqoopParameters;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.sources.SourceMysqlParameter;
import com.mdosys.scheduler.spi.datasource.BaseConnectionParam;
import com.mdosys.scheduler.spi.enums.DbType;
import com.mdosys.scheduler.spi.task.Property;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import com.mdosys.scheduler.spi.utils.JSONUtils;
import com.mdosys.scheduler.spi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mdosys.scheduler.plugin.datasource.api.utils.PasswordUtils.decodePassword;
import static com.mdosys.scheduler.plugin.task.sqoop.SqoopConstants.*;
import static com.mdosys.scheduler.spi.task.TaskConstants.*;

/**
 * mysql source generator
 */
public class MysqlSourceGenerator implements ISourceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MysqlSourceGenerator.class);

    @Override
    public String generate(SqoopParameters sqoopParameters, TaskRequest taskExecutionContext) {

        StringBuilder mysqlSourceSb = new StringBuilder();

        try {
            SourceMysqlParameter sourceMysqlParameter = JSONUtils.parseObject(sqoopParameters.getSourceParams(),
                    SourceMysqlParameter.class);

            if (null != sourceMysqlParameter) {
                BaseConnectionParam baseDataSource = (BaseConnectionParam) DatasourceUtil.buildConnectionParams(
                        DbType.of(taskExecutionContext.getSqoopTaskExecutionContext().getSourcetype()),
                        taskExecutionContext.getSqoopTaskExecutionContext().getSourceConnectionParams());

                if (null != baseDataSource) {

                    mysqlSourceSb.append(SPACE).append(DB_CONNECT)
                            .append(SPACE).append(DOUBLE_QUOTES)
                            .append(DatasourceUtil.getJdbcUrl(DbType.MYSQL, baseDataSource)).append(DOUBLE_QUOTES)
                            .append(SPACE).append(DB_USERNAME)
                            .append(SPACE).append(baseDataSource.getUser())
                            .append(SPACE).append(DB_PWD)
                            .append(SPACE).append(DOUBLE_QUOTES)
                            .append(decodePassword(baseDataSource.getPassword())).append(DOUBLE_QUOTES);

                    // sqoop table & sql query
                    if (sourceMysqlParameter.getSrcQueryType() == SqoopQueryType.FORM.getCode()) {
                        if (StringUtils.isNotEmpty(sourceMysqlParameter.getSrcTable())) {
                            mysqlSourceSb.append(SPACE).append(TABLE)
                                    .append(SPACE).append(sourceMysqlParameter.getSrcTable());
                        }

                        if (StringUtils.isNotEmpty(sourceMysqlParameter.getSrcColumns())) {
                            mysqlSourceSb.append(SPACE).append(COLUMNS)
                                    .append(SPACE).append(sourceMysqlParameter.getSrcColumns());
                        }
                    } else if (sourceMysqlParameter.getSrcQueryType() == SqoopQueryType.SQL.getCode()
                            && StringUtils.isNotEmpty(sourceMysqlParameter.getSrcQuerySql())) {

                        String srcQuery = sourceMysqlParameter.getSrcQuerySql();
                        mysqlSourceSb.append(SPACE).append(QUERY)
                                .append(SPACE).append(DOUBLE_QUOTES).append(srcQuery);

                        if (srcQuery.toLowerCase().contains(QUERY_WHERE)) {
                            mysqlSourceSb.append(SPACE).append(QUERY_CONDITION).append(DOUBLE_QUOTES);
                        } else {
                            mysqlSourceSb.append(SPACE).append(QUERY_WITHOUT_CONDITION).append(DOUBLE_QUOTES);
                        }
                    }

                    // sqoop hive map column
                    List<Property> mapColumnHive = sourceMysqlParameter.getMapColumnHive();

                    if (null != mapColumnHive && !mapColumnHive.isEmpty()) {
                        StringBuilder columnMap = new StringBuilder();
                        for (Property item : mapColumnHive) {
                            columnMap.append(item.getProp()).append(EQUAL_SIGN).append(item.getValue()).append(COMMA);
                        }

                        if (StringUtils.isNotEmpty(columnMap.toString())) {
                            mysqlSourceSb.append(SPACE).append(MAP_COLUMN_HIVE)
                                    .append(SPACE).append(columnMap.substring(0, columnMap.length() - 1));
                        }
                    }

                    // sqoop map column java
                    List<Property> mapColumnJava = sourceMysqlParameter.getMapColumnJava();

                    if (null != mapColumnJava && !mapColumnJava.isEmpty()) {
                        StringBuilder columnMap = new StringBuilder();
                        for (Property item : mapColumnJava) {
                            columnMap.append(item.getProp()).append(EQUAL_SIGN).append(item.getValue()).append(COMMA);
                        }

                        if (StringUtils.isNotEmpty(columnMap.toString())) {
                            mysqlSourceSb.append(SPACE).append(MAP_COLUMN_JAVA)
                                    .append(SPACE).append(columnMap.substring(0, columnMap.length() - 1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Sqoop task mysql source params build failed: [%s]", e.getMessage()));
        }

        return mysqlSourceSb.toString();
    }
}
