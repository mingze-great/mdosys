package com.mdosys.knowledgelib.domain;

import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 组件对象 component
 *
 * @author brz
 * @date 2022-09-01
 */
public class Component extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组件id */
    private Long compId;

    /** 类型id */
    @Excel(name = "类型id")
    private Long typeId;

    /** 组件名称 */
    @Excel(name = "组件名称")
    private String compName;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 图标地址 */
    @Excel(name = "图标地址")
    private String icon;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String file;

    /** 描述 */
    @Excel(name = "描述")
    private String desc;

    /** 授权 */
    private int auth;

    public void setAuth(int auth) {this.auth = auth; }
    public int getAuth() {return auth; }

    public void setCompId(Long compId)
    {
        this.compId = compId;
    }

    public Long getCompId()
    {
        return compId;
    }
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }
    public void setCompName(String compName)
    {
        this.compName = compName;
    }

    public String getCompName()
    {
        return compName;
    }
    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getIcon()
    {
        return icon;
    }
    public void setFile(String file)
    {
        this.file = file;
    }

    public String getFile()
    {
        return file;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("compId", getCompId())
            .append("typeId", getTypeId())
            .append("compName", getCompName())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("icon", getIcon())
            .append("file", getFile())
            .append("desc", getDesc())
            .append("auth", getAuth())
            .toString();
    }
}
