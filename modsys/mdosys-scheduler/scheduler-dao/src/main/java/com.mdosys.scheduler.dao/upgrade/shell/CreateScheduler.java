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

package com.mdosys.scheduler.dao.upgrade.shell;

import com.mdosys.scheduler.dao.upgrade.SchedulerManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@ComponentScan(value = "com.mdosys.scheduler.dao")
@EnableAutoConfiguration(exclude = { QuartzAutoConfiguration.class })
public class CreateScheduler {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CreateScheduler.class)
                .profiles("shell-create", "shell-cli")
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Component
    @Profile("shell-create")
    static class CreateRunner implements CommandLineRunner {
        private static final Logger logger = LoggerFactory.getLogger(CreateRunner.class);

        private final SchedulerManager SchedulerManager;

        CreateRunner(SchedulerManager SchedulerManager) {
            this.SchedulerManager = SchedulerManager;
        }

        @Override
        public void run(String... args) throws Exception {
            if (SchedulerManager.schemaIsInitialized()) {
                SchedulerManager.upgradeScheduler();
                logger.info("upgrade Scheduler finished");
            } else {
                SchedulerManager.initScheduler();
                logger.info("init Scheduler finished");
            }
            logger.info("create Scheduler success");
        }
    }
}