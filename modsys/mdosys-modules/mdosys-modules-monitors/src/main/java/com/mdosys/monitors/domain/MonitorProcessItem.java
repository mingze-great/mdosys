package com.mdosys.monitors.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdosys.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 流程监控对象
 */
public class MonitorProcessItem extends BaseEntity {
    private static final long serialVersionUID = 1L;
/*
* interface monitorInfoParam {
    id: string,         //流程id
    taskName: string,       //任务名称
    taskType: string,       //任务类型
    creator: string,        //创建人
    taskProgress: Date,   //任务进度
    createTime: Date,     //创建时间
    runLog: string,         //运行日志
    runTime: number,        //已运行的时长，单位(min)
    runFlag: boolean        //当前任务是否处于运行状态，true表示处于运行状态
}*/

    /**
     * 主键
     */
    private long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 任务进度
     */
    private Integer taskProgress;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String runLog;//存疑，是否有必要把这个放进来

    /**
     * 已运行时长
     */
    private String runTime;

    /**
     * 是否处于运行状态
     */
    private boolean runFlag;

    /**
     * 流程结束时间
     */
    private Date endTime;

    /**
     * 流程开始时间
     */
    private Date startTime;

    /**
     * 输入查询的参数
     */
    private String inputQuery;

    /**
     * 判断是否存在子组件
     */
    private boolean hasChildren;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public String toString() {
        return "MonitorProcessItem{" +
                "id='" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", creator='" + creator + '\'' +
                ", taskProgress=" + taskProgress +
                ", createTime=" + createTime +
                ", runLog='" + runLog + '\'' +
                ", runTime='" + runTime + '\'' +
                ", runFlag=" + runFlag +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", inputQuery='" + inputQuery + '\'' +
                ", hasChildren=" + hasChildren +
                '}';
    }

    public String getInputQuery() {
        return inputQuery;
    }

    public void setInputQuery(String inputQuery) {
        this.inputQuery = inputQuery;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(Integer taskProgress) {
        this.taskProgress = taskProgress;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRunLog() {
        return runLog;
    }

    public void setRunLog(String runLog) {
        this.runLog = runLog;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


}
