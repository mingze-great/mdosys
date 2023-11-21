package com.mdosys.system.domain.Vo;

import com.mdosys.system.domain.SysComponent;

public class ComponentInfo extends SysComponent {

    String executePath;

    public ComponentInfo(SysComponent component) {
        super(component.getId(), component.getName(), component.getClassId(), component.getCreateTime(), component.getUpdateTime(), component.getIcon(), component.getUserId(), component.getPublicComp(), component.getParam(), component.getDescription(), component.getCode(), component.getType());
    }

    public void setExecutePath(String executePath) {
        this.executePath = executePath;
    }

    public String getExecutePath() {
        return executePath;
    }
}
