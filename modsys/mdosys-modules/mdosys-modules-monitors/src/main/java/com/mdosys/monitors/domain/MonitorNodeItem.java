package com.mdosys.monitors.domain;

import java.util.Date;

public class MonitorNodeItem {
    private long nodeId;

    private String taskName;

    private long code;

    private long processInstanceId;

    private String description;

    private String creator;

    private int taskProgress;

    private String taskType;

    private String runTime;

    private String runLog;

    private Date createTime;

    private Date endTime;

    private Date updateTime;

    private int runFlag;

    public int getRunFlag() {
        return runFlag;
    }

    public void setRunFlag(int runFlag) {
        this.runFlag = runFlag;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(int taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getRunLog() {
        return runLog;
    }

    public void setRunLog(String runLog) {
        this.runLog = runLog;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MonitorNodeItem{" +
                "nodeId=" + nodeId +
                ", taskName='" + taskName + '\'' +
                ", code=" + code +
                ", processInstanceId=" + processInstanceId +
                ", description='" + description + '\'' +
                ", creator='" + creator + '\'' +
                ", taskProgress=" + taskProgress +
                ", taskType='" + taskType + '\'' +
                ", runTime='" + runTime + '\'' +
                ", runLog='" + runLog + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", updateTime=" + updateTime +
                ", runFlag=" + runFlag +
                '}';
    }
}
