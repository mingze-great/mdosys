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

package com.mdosys.scheduler.plugin.task.sqoop;

import com.mdosys.scheduler.plugin.task.api.AbstractYarnTask;
import com.mdosys.scheduler.plugin.task.sqoop.generator.SqoopJobGenerator;
import com.mdosys.scheduler.plugin.task.sqoop.parameter.SqoopParameters;
import com.mdosys.scheduler.plugin.task.util.MapUtils;
import com.mdosys.scheduler.spi.task.AbstractParameters;
import com.mdosys.scheduler.spi.task.Property;
import com.mdosys.scheduler.spi.task.paramparser.ParamUtils;
import com.mdosys.scheduler.spi.task.paramparser.ParameterUtils;
import com.mdosys.scheduler.spi.task.request.TaskRequest;
import com.mdosys.scheduler.spi.utils.JSONUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * sqoop task extends the shell task
 */
public class SqoopTask extends AbstractYarnTask {

    /**
     * sqoop task params
     */
    private SqoopParameters sqoopParameters;

    /**
     * taskExecutionContext
     */
    private final TaskRequest taskExecutionContext;

    public SqoopTask(TaskRequest taskExecutionContext) {
        super(taskExecutionContext);
        this.taskExecutionContext = taskExecutionContext;
    }

    @Override
    public void init() {
        logger.info("sqoop task params {}", taskExecutionContext.getTaskParams());
        sqoopParameters = JSONUtils.parseObject(taskExecutionContext.getTaskParams(), SqoopParameters.class);
        // check sqoop task params
        if (null == sqoopParameters) {
            throw new IllegalArgumentException("Sqoop Task params is null");
        }

        if (!sqoopParameters.checkParameters()) {
            throw new IllegalArgumentException("Sqoop Task params check fail");
        }
    }

    @Override
    protected String buildCommand() {
        // get sqoop scripts
        SqoopJobGenerator generator = new SqoopJobGenerator();
        String script = generator.generateSqoopJob(sqoopParameters, taskExecutionContext);

        // combining local and global parameters
        Map<String, Property> paramsMap = ParamUtils.convert(taskExecutionContext, getParameters());

        if (MapUtils.isEmpty(paramsMap)) {
            paramsMap = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(taskExecutionContext.getParamsMap())) {
            paramsMap.putAll(taskExecutionContext.getParamsMap());
        }

        String resultScripts = ParameterUtils.convertParameterPlaceholders(script, ParamUtils.convert(paramsMap));
        logger.info("sqoop script: {}", resultScripts);
        return resultScripts;

    }

    @Override
    protected void setMainJarName() {
    }

    @Override
    public AbstractParameters getParameters() {
        return sqoopParameters;
    }
}
