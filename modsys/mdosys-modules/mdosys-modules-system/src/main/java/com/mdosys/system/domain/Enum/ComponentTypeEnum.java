package com.mdosys.system.domain.Enum;

public enum ComponentTypeEnum {
    COMPONENT("COMPONENT"),
    SHELL("SHELL"),
    PYTHON("PYTHON"),
    MATLAB("MATLAB"),
    ;

    /** 名称 */
    private final String name;

    ComponentTypeEnum(String name) {
        this.name = name;
    }
}
