package com.mdosys.project.domain;

import java.util.Date;

/**
 * project
 */
public class Project {

    /**
     * id
     */

    private long id;

    /**
     * user id
     */

    private long userId;

    /**
     * user name
     */

    private String username;

    /**
     * project code
     */
    private long code;

    /**
     * project name
     */
    private String name;

    /**
     * project description
     */
    private String description;

    /**
     * create time
     */

    private Date createTime;

    /**
     * update time
     */

    private Date updateTime;

    /**
     * permission
     */

    private int perm;

    /**
     * process define count
     */
    private int defCount;

    /**
     * process instance running count
     */
    private int instRunningCount;

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getDefCount() {
        return this.defCount;
    }

    public void setDefCount(int defCount) {
        this.defCount = defCount;
    }

    public int getInstRunningCount() {
        return this.instRunningCount;
    }

    public void setInstRunningCount(int instRunningCount) {
        this.instRunningCount = instRunningCount;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPerm() {
        return this.perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    @Override
    public String toString() {
        return "Project{"
                + "id=" + id
                + ", userId=" + userId
                + ", userName='" + username + '\''
                + ", code=" + code
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", createTime=" + createTime
                + ", updateTime=" + updateTime
                + ", perm=" + perm
                + ", defCount=" + defCount
                + ", instRunningCount=" + instRunningCount
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if (id != project.id) {
            return false;
        }
        return name.equals(project.name);

    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + name.hashCode();
        return result;
    }



    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private long id;
        private long userId;
        private String userName;
        private long code;
        private String name;
        private String description;
        private Date createTime;
        private Date updateTime;
        private int perm;
        private int defCount;
        private int instRunningCount;

        private Builder() {
        }

        public Builder code(long code) {
            this.code = code;
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createTime(Date date){
            this.createTime=date;
            return  this;
        }

        public Builder updateTime(Date date){
            this.updateTime=date;
            return  this;
        }

        public Builder perm(int perm) {
            this.perm = perm;
            return this;
        }

        public Builder defCount(int defCount) {
            this.defCount = defCount;
            return this;
        }

        public Builder instRunningCount(int instRunningCount) {
            this.instRunningCount = instRunningCount;
            return this;
        }


        public Project build() {
            Project project = new Project();
            project.setId(id);
            project.setUserId(userId);
            project.setCode(code);
            project.setUsername(userName);
            project.setName(name);
            project.setCreateTime(createTime);
            project.setUpdateTime(updateTime);
            project.setDescription(description);
            project.setPerm(perm);
            project.setDefCount(defCount);
            project.setInstRunningCount(instRunningCount);
            return project;
        }
    }
}
