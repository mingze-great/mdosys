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
package com.mdosys.scheduler.remote.command;

import java.io.Serializable;

/**
 *  command header
 */
public class CommandHeader implements Serializable {

    /**
     * type
     */
    private byte type;

    /**
     * request unique identification
     */
    private long opaque;

    /**
     *  context length
     */
    private int contextLength;

    /**
     *  context
     */
    private byte[] context;

    /**
     *  body length
     */
    private int bodyLength;

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public long getOpaque() {
        return opaque;
    }

    public void setOpaque(long opaque) {
        this.opaque = opaque;
    }

    public int getContextLength() {
        return contextLength;
    }

    public void setContextLength(int contextLength) {
        this.contextLength = contextLength;
    }

    public byte[] getContext() {
        return context;
    }

    public void setContext(byte[] context) {
        this.context = context;
    }
}
