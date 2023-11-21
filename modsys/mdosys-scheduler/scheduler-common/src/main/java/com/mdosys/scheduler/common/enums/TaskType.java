package com.mdosys.scheduler.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * task node type
 */
public enum TaskType {
    /**
     * 0 SHELL
     * 1 SQL
     * 2 SUB_PROCESS
     * 3 PROCEDURE
     * 4 MR
     * 5 SPARK
     * 6 PYTHON
     * 7 DEPENDENT
     * 8 FLINK
     * 9 HTTP
     * 10 DATAX
     * 11 CONDITIONS
     * 12 SQOOP
     * 13 WATERDROP
     * 15 PIGEON
     */
    SHELL(0, "SHELL"),
    SQL(1, "SQL"),
    SUB_PROCESS(2, "SUB_PROCESS"),
    PROCEDURE(3, "PROCEDURE"),
    MR(4, "MR"),
    SPARK(5, "SPARK"),
    PYTHON(6, "PYTHON"),
    DEPENDENT(7, "DEPENDENT"),
    FLINK(8, "FLINK"),
    HTTP(9, "HTTP"),
    DATAX(10, "DATAX"),
    CONDITIONS(11, "CONDITIONS"),
    SQOOP(12, "SQOOP"),
    WATERDROP(13, "WATERDROP"),
    SWITCH(14, "SWITCH"),
    PIGEON(15, "PIGEON");

    TaskType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final int code;
    private final String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
