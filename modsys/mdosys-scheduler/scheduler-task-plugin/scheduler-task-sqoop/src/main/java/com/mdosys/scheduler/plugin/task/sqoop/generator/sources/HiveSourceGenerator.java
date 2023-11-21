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

import com.mdosys.scheduler.plugin.task.sqoop.generator.ISourceGenerator;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.SqoopParameters;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.sources.SourceHiveParameter;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import com.mdosys.scheduler.spi.utils.JSONUtils;
import com.mdosys.scheduler.spi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mdosys.scheduler.plugin.task.sqoop.SqoopConstants.*;
import static com.mdosys.scheduler.spi.task.TaskConstants.SPACE;

/**
 * hive source generator
 */
public class HiveSourceGenerator implements ISourceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(HiveSourceGenerator.class);

    @Override
    public String generate(SqoopParameters sqoopParameters, TaskRequest taskExecutionContext) {

        StringBuilder hiveSourceSb = new StringBuilder();

        try {
            SourceHiveParameter sourceHiveParameter = JSONUtils.parseObject(sqoopParameters.getSourceParams(),
                    SourceHiveParameter.class);

            if (null != sourceHiveParameter) {
                if (StringUtils.isNotEmpty(sourceHiveParameter.getHiveDatabase())) {
                    hiveSourceSb.append(SPACE).append(HCATALOG_DATABASE)
                            .append(SPACE).append(sourceHiveParameter.getHiveDatabase());
                }

                if (StringUtils.isNotEmpty(sourceHiveParameter.getHiveTable())) {
                    hiveSourceSb.append(SPACE).append(HCATALOG_TABLE)
                            .append(SPACE).append(sourceHiveParameter.getHiveTable());
                }

                if (StringUtils.isNotEmpty(sourceHiveParameter.getHivePartitionKey())
                        && StringUtils.isNotEmpty(sourceHiveParameter.getHivePartitionValue())) {
                    hiveSourceSb.append(SPACE).append(HCATALOG_PARTITION_KEYS)
                            .append(SPACE).append(sourceHiveParameter.getHivePartitionKey())
                            .append(SPACE).append(HCATALOG_PARTITION_VALUES)
                            .append(SPACE).append(sourceHiveParameter.getHivePartitionValue());
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Sqoop hive source params build failed: [%s]", e.getMessage()));
        }

        return hiveSourceSb.toString();
    }
}
