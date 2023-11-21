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

package com.mdosys.scheduler.server.worker.plugin;

import com.mdosys.scheduler.common.enums.PluginType;
import com.mdosys.scheduler.common.enums.TaskType;
import com.mdosys.scheduler.dao.PluginDao;
import com.mdosys.scheduler.dao.entity.PluginDefine;
import com.mdosys.scheduler.spi.params.PluginParamsTransfer;
import com.mdosys.scheduler.spi.params.base.PluginParams;
import com.mdosys.scheduler.spi.task.TaskChannel;
import com.mdosys.scheduler.spi.task.TaskChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Component
public class TaskPluginManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskPluginManager.class);

    private final Map<String, TaskChannel> taskChannelMap = new ConcurrentHashMap<>();

    private final PluginDao pluginDao;

    public TaskPluginManager(PluginDao pluginDao) {
        this.pluginDao = pluginDao;
    }

    private void loadTaskChannel(TaskChannelFactory taskChannelFactory) {
        TaskChannel taskChannel = taskChannelFactory.create();
        taskChannelMap.put(taskChannelFactory.getName(), taskChannel);
    }

    public Map<String, TaskChannel> getTaskChannelMap() {
        return Collections.unmodifiableMap(taskChannelMap);
    }

    @EventListener
    public void installPlugin(ApplicationReadyEvent readyEvent) {
        final Set<String> names = new HashSet<>();

        ServiceLoader.load(TaskChannelFactory.class).forEach(factory -> {
            final String name = factory.getName();

            logger.info("Registering task plugin: {}", name);

            if (!names.add(name)) {
                throw new IllegalStateException(format("Duplicate task plugins named '%s'", name));
            }

            loadTaskChannel(factory);

            logger.info("Registered task plugin: {}", name);

            List<PluginParams> params = factory.getParams();
            String paramsJson = PluginParamsTransfer.transferParamsToJson(params);

            PluginDefine pluginDefine = new PluginDefine(name, PluginType.TASK.getDesc(), paramsJson);
            int count = pluginDao.addOrUpdatePluginDefine(pluginDefine);
            if (count <= 0) {
                throw new RuntimeException("Failed to update task plugin: " + name);
            }
        });

        // put WATERDROP task
        taskChannelMap.put(TaskType.WATERDROP.getDesc(), taskChannelMap.get(TaskType.SHELL.getDesc()));

    }
}
