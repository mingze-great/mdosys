package com.mdosys.system.domain;

import com.mdosys.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;

public class SysComponentMenu extends BaseEntity {

    /** 主键 */
    private Long id;

    /** 组件菜单名称 */
    private String name;

    /** 父菜单 */
    private Long parentId;

    /** 图标 */
    private String icon;

    /** 用户 */
    private Long userId;

    /** 是否公共 */
    private Long publicMenu;

    /** 创建日期 */
//    private Date createTime;

    /** 更新日期 */
//    private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("parentId", getParentId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("icon", getIcon())
                .append("userId", getUserId())
                .toString();
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getPublicMenu() {
        return publicMenu;
    }

    public void setPublicMenu(Long publicMenu) {
        this.publicMenu = publicMenu;
    }

//    @Override
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }

//    @Override
//    public Date getUpdateTime() {
//        return updateTime;
//    }

//    @Override
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
}
