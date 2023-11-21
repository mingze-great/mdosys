package com.mdosys.knowledgelib.domain;

import com.mdosys.common.core.annotation.Excel;
import com.mdosys.common.core.web.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * component_menu对象 component_menu
 * 
 * @author hwj
 * @date 2022-09-19
 */
public class ComponentMenu extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 组件名称 */
    @Excel(name = "组件名称")
    private String name;

    /** 菜单类型 */
    @Excel(name = "菜单类型")
    private String menuType;

    /** 组件图标 */
    @Excel(name = "组件图标")
    private String icon;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String filePath;

    /** 组件权限，公共组件还是私有组件 */
    @Excel(name = "组件权限，公共组件还是私有组件")
    private Long permission;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setMenuType(String menuType) 
    {
        this.menuType = menuType;
    }

    public String getMenuType() 
    {
        return menuType;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setPermission(Long permission) 
    {
        this.permission = permission;
    }

    public Long getPermission() 
    {
        return permission;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("parentId", getParentId())
            .append("menuType", getMenuType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("icon", getIcon())
            .append("filePath", getFilePath())
            .append("permission", getPermission())
            .append("remark", getRemark())
            .toString();
    }
}
