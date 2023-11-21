package com.mdosys.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;

/**
 * worker分组对象 sys_worker
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
public class SysWorker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** worker分组ID */
    private Long workerId;

    /** worker分组名称 */
    @Excel(name = "worker分组名称")
    private String workerName;

    /** 地址列表 */
    @Excel(name = "地址列表")
    private String addrList;

    public void setWorkerId(Long workerId)
    {
        this.workerId = workerId;
    }

    public Long getWorkerId()
    {
        return workerId;
    }
    public void setWorkerName(String workerName)
    {
        this.workerName = workerName;
    }

    public String getWorkerName()
    {
        return workerName;
    }
    public void setAddrList(String addrList)
    {
        this.addrList = addrList;
    }

    public String getAddrList()
    {
        return addrList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("workerId", getWorkerId())
            .append("workerName", getWorkerName())
            .append("addrList", getAddrList())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
