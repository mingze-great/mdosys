package com.mdosys.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;

/**
 * 环境对象 sys_environment
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
public class SysEnvironment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 环境ID */
    private Long envirId;

    /** 环境名称 */
    @Excel(name = "环境名称")
    private String envirName;

    /** worker分组ID */
    @Excel(name = "worker分组ID")
    private Long workerId;

    /** 编码 */
    @Excel(name = "编码")
    private Long code;

    /** 环境配置 */
    @Excel(name = "环境配置")
    private String config;

    /** 详细描述 */
    @Excel(name = "详细描述")
    private String description;

    public void setEnvirId(Long envirId)
    {
        this.envirId = envirId;
    }

    public Long getEnvirId()
    {
        return envirId;
    }
    public void setEnvirName(String envirName)
    {
        this.envirName = envirName;
    }

    public String getEnvirName()
    {
        return envirName;
    }
    public void setWorkerId(Long workerId)
    {
        this.workerId = workerId;
    }

    public Long getWorkerId()
    {
        return workerId;
    }
    public void setCode(Long code)
    {
        this.code = code;
    }

    public Long getCode()
    {
        return code;
    }
    public void setConfig(String config)
    {
        this.config = config;
    }

    public String getConfig()
    {
        return config;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("envirId", getEnvirId())
            .append("envirName", getEnvirName())
            .append("workerId", getWorkerId())
            .append("code", getCode())
            .append("config", getConfig())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
