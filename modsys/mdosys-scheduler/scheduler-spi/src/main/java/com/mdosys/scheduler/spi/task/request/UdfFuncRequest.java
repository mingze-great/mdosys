package com.mdosys.scheduler.spi.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.mdosys.scheduler.spi.utils.JSONUtils;
import com.mdosys.scheduler.spi.utils.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * udf function
 */
public class UdfFuncRequest {
    /**
     * id
     */
    private int id;
    /**
     * user id
     */
    private int userId;

    /**
     * udf function name
     */
    private String funcName;

    /**
     * udf class name
     */
    private String className;

    /**
     * udf argument types
     */
    private String argTypes;

    /**
     * udf data base
     */
    private String database;

    /**
     * udf description
     */
    private String description;

    /**
     * resource id
     */
    private int resourceId;

    /**
     * resource name
     */
    private String resourceName;

    /**
     * udf function type: hive / spark
     */
    private UdfType type;

    /**
     * create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(String argTypes) {
        this.argTypes = argTypes;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public UdfType getType() {
        return type;
    }

    public void setType(UdfType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UdfFuncRequest udfFuncRequest = (UdfFuncRequest) o;

        if (id != udfFuncRequest.id) {
            return false;
        }
        return !(funcName != null ? !funcName.equals(udfFuncRequest.funcName) : udfFuncRequest.funcName != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (funcName != null ? funcName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return JSONUtils.toJsonString(this);
    }

    public static class UdfFuncDeserializer extends KeyDeserializer {

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            return JSONUtils.parseObject(key, UdfFuncRequest.class);
        }
    }
}
