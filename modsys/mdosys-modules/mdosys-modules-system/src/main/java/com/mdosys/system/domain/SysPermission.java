package com.mdosys.system.domain;

import com.mdosys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限表 sys_permission
 *
 * @author
 */
public class SysPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long permissionId;

    /** 权限名称 */
    private String permissionName;

    /** 权限字符串 */
    private String perms;

    /** 子菜单 */
    private List<SysPermission> children = new ArrayList<SysPermission>();

    public Long getPermissionId()
    {
        return permissionId;
    }

    public void setPermissionId(Long permissionId)
    {
        this.permissionId = permissionId;
    }

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getPermissionName()
    {
        return permissionName;
    }

    public void setPermissionName(String permissionName)
    {
        this.permissionName = permissionName;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("permissionId", getPermissionId())
            .append("permissionName", getPermissionName())
            .append("perms", getPerms())
            .toString();
    }
}
