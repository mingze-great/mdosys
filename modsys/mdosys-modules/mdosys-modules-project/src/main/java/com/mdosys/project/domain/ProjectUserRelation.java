package com.mdosys.project.domain;

import java.util.Date;

public class ProjectUserRelation {

    private long id;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 项目id
     */
    private long projectId;

    /**
     * 用户对此项目所拥有的权限
     */
    private int permission;

    /**
     * 关系建立时间
     */
    private Date createTime;
    /**
     * 关系更新时间
     */
    private Date updateTime;


    public long getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProjectId() {
        return projectId;
    }


    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "ProjectUserRelation{" +
                "userId=" + userId +
                ", projectId=" + projectId +
                ", permission=" + permission +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
