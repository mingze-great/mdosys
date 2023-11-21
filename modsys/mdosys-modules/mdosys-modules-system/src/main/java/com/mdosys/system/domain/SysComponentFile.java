package com.mdosys.system.domain;

import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 组件文件对象 sys_component_file
 * 
 * @author hwj
 * @date 2022-10-25
 */
public class SysComponentFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组件文件主键 */
    private Long id;

    /** 组件id */
    @Excel(name = "组件id")
    private Long compId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String name;

    /** 保存路径 */
    @Excel(name = "保存路径")
    private String path;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private String size;

    /** 解析后的字符串 */
    @Excel(name = "解析后的字符串")
    private String analysisStr;

    @Excel(name = "文件类型")
    private  String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompId(Long compId)
    {
        this.compId = compId;
    }

    public Long getCompId()
    {
        return compId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
    public void setSize(String size)
    {
        this.size = size;
    }

    public String getSize()
    {
        return size;
    }
    public void setAnalysisStr(String analysisStr)
    {
        this.analysisStr = analysisStr;
    }

    public String getAnalysisStr()
    {
        return analysisStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("compId", getCompId())
            .append("name", getName())
            .append("path", getPath())
            .append("createTime", getCreateTime())
            .append("size", getSize())
            .append("analysisStr", getAnalysisStr())
            .append("type", getType())
            .toString();
    }
}
