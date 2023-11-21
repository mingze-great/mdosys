package com.mdosys.system.domain;

import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件对象 sys_file
 *
 * @author hwj
 * @date 2022-10-11
 */
public class SysSpaceFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long userId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String name;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String type;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private String size;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String path;

    /** 父级目录 */
    @Excel(name = "父级目录")
    private Long fatherId;

    /** $column.columnComment */
    @Excel(name = "文件类型")
    private String fileType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setSize(String size)
    {
        this.size = size;
    }

    public String getSize()
    {
        return size;
    }
    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
    public void setFatherId(Long fatherId)
    {
        this.fatherId = fatherId;
    }

    public Long getFatherId()
    {
        return fatherId;
    }
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("name", getName())
                .append("type", getType())
                .append("createTime", getCreateTime())
                .append("size", getSize())
                .append("path", getPath())
                .append("fatherId", getFatherId())
                .append("fileType", getFileType())
                .toString();
    }
}
