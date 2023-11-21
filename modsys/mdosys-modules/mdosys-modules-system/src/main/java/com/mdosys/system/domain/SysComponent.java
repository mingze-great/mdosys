package com.mdosys.system.domain;

import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;
import com.mdosys.system.domain.Enum.ComponentTypeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 组件对象 sys_component
 * 
 * @author hwj
 * @date 2022-10-25
 */
public class SysComponent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 组件名称 */
    private String name;

    /** 菜单 */
    private String classId;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 组件图标 */
    private String icon;

    /** 用户 */
    private Long userId;

    /** 公共组件 */
    private String publicComp;

    /** 参数 */
    private String param;

    /** 组件描述 */
    private String description;

    private Long code;

    private ComponentTypeEnum type;

    public ComponentTypeEnum getType() {
        return type;
    }

    public void setType(ComponentTypeEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("classId", getClassId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("icon", getIcon())
                .append("userId", getUserId())
                .append("publicComp", getPublicComp())
                .append("param", getParam())
                .append("description", getDescription())
                .append("code", getCode())
                .append("type", getType())
                .toString();
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPublicComp() {
        return publicComp;
    }

    public void setPublicComp(String publicComp) {
        this.publicComp = publicComp;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SysComponent(Long id, String name, String classId, Date createTime, Date updateTime, String icon, Long userId, String publicComp, String param, String description, Long code, ComponentTypeEnum type) {
        this.id = id;
        this.name = name;
        this.classId = classId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.icon = icon;
        this.userId = userId;
        this.publicComp = publicComp;
        this.param = param;
        this.description = description;
        this.code = code;
        this.type = type;
    }

    public SysComponent() {
    }
}
