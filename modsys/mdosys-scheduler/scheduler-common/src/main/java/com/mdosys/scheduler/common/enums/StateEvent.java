package com.mdosys.scheduler.common.enums;

import io.netty.channel.Channel;

/**
 * state event
 */
public class StateEvent {

    /**
     * origin_pid-origin_task_id-process_instance_id-task_instance_id
     */
    private String key;

    private StateEventType type;

    private ExecutionStatus executionStatus;

    private int taskInstanceId;

    private int processInstanceId;

    private String context;

    private Channel channel;

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public int getTaskInstanceId() {
        return taskInstanceId;
    }

    public int getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(int processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setTaskInstanceId(int taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "State Event :"
                + "key: " + key
                + " type: " + type.toString()
                + " executeStatus: " + executionStatus
                + " task instance id: " + taskInstanceId
                + " process instance id: " + processInstanceId
                + " context: " + context
                ;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(StateEventType type) {
        this.type=type;
    }

    public StateEventType getType() {
        return this.type;
    }
}
