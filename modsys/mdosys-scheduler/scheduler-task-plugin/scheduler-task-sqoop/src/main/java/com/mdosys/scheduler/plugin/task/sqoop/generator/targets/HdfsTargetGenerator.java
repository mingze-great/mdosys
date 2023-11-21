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

package com.mdosys.scheduler.plugin.task.sqoop.generator.targets;

import com.mdosys.scheduler.plugin.task.sqoop.generator.ITargetGenerator;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.SqoopParameters;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.targets.TargetHdfsParameter;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import com.mdosys.scheduler.spi.utils.JSONUtils;
import com.mdosys.scheduler.spi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mdosys.scheduler.plugin.task.sqoop.SqoopConstants.*;
import static com.mdosys.scheduler.spi.task.TaskConstants.SINGLE_QUOTES;
import static com.mdosys.scheduler.spi.task.TaskConstants.SPACE;

/**
 * hdfs target generator
 */
public class HdfsTargetGenerator implements ITargetGenerator {

    private static final Logger logger = LoggerFactory.getLogger(HdfsTargetGenerator.class);

    @Override
    public String generate(SqoopParameters sqoopParameters, TaskRequest taskExecutionContext) {

        StringBuilder hdfsTargetSb = new StringBuilder();

        try {
            TargetHdfsParameter targetHdfsParameter = JSONUtils.parseObject(sqoopParameters.getTargetParams(),
                    TargetHdfsParameter.class);

            if (null != targetHdfsParameter) {

                if (StringUtils.isNotEmpty(targetHdfsParameter.getTargetPath())) {
                    hdfsTargetSb.append(SPACE).append(TARGET_DIR)
                            .append(SPACE).append(targetHdfsParameter.getTargetPath());
                }

                if (StringUtils.isNotEmpty(targetHdfsParameter.getCompressionCodec())) {
                    hdfsTargetSb.append(SPACE).append(COMPRESSION_CODEC)
                            .append(SPACE).append(targetHdfsParameter.getCompressionCodec());
                }

                if (StringUtils.isNotEmpty(targetHdfsParameter.getFileType())) {
                    hdfsTargetSb.append(SPACE).append(targetHdfsParameter.getFileType());
                }

                if (targetHdfsParameter.isDeleteTargetDir()) {
                    hdfsTargetSb.append(SPACE).append(DELETE_TARGET_DIR);
                }

                if (StringUtils.isNotEmpty(targetHdfsParameter.getFieldsTerminated())) {
                    hdfsTargetSb.append(SPACE).append(FIELDS_TERMINATED_BY)
                            .append(SPACE).append(SINGLE_QUOTES).append(targetHdfsParameter.getFieldsTerminated())
                            .append(SINGLE_QUOTES);
                }

                if (StringUtils.isNotEmpty(targetHdfsParameter.getLinesTerminated())) {
                    hdfsTargetSb.append(SPACE).append(LINES_TERMINATED_BY)
                            .append(SPACE).append(SINGLE_QUOTES).append(targetHdfsParameter.getLinesTerminated())
                            .append(SINGLE_QUOTES);
                }

                hdfsTargetSb.append(SPACE).append(FIELD_NULL_PLACEHOLDER);
            }
        } catch (Exception e) {
            logger.error(String.format("Sqoop hdfs target params build failed: [%s]", e.getMessage()));
        }

        return hdfsTargetSb.toString();
    }
}
