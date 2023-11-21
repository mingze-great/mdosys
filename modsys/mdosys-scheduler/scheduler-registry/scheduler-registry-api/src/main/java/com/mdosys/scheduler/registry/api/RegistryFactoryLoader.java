/*
 * Licensed to Apache Software Foundation (ASF) under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Apache Software Foundation (ASF) licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.mdosys.scheduler.registry.api;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

public final class RegistryFactoryLoader {
    public static Map<String, com.mdosys.scheduler.registry.api.RegistryFactory> load() {
        final ServiceLoader<com.mdosys.scheduler.registry.api.RegistryFactory> factories = ServiceLoader
                .load(com.mdosys.scheduler.registry.api.RegistryFactory.class);
        return StreamSupport.stream(factories.spliterator(), false)
                .collect(toMap(com.mdosys.scheduler.registry.api.RegistryFactory::name, Function.identity()));
    }
}
