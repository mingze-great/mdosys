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
package com.mdosys.scheduler.api.dto;

import com.mdosys.scheduler.common.enums.ExecutionStatus;

/**
 * task state count
 */
public class TaskStateCount {

    private int count;
    private ExecutionStatus taskStateType;

    public TaskStateCount(ExecutionStatus taskStateType, int count) {
        this.taskStateType = taskStateType;
        this.count = count;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ExecutionStatus getTaskStateType() {
        return taskStateType;
    }

    public void setTaskStateType(ExecutionStatus taskStateType) {
        this.taskStateType = taskStateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskStateCount that = (TaskStateCount) o;

        if (count != that.count) {
            return false;
        }
        return taskStateType == that.taskStateType;
    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (taskStateType != null ? taskStateType.hashCode() : 0);
        return result;
    }
}
