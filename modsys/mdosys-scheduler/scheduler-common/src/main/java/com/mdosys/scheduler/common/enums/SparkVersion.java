package com.mdosys.scheduler.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SparkVersion {

    /**
     * 0 SPARK1
     * 1 SPARK2
     */
    SPARK1(0, "SPARK1"),
    SPARK2(1, "SPARK2");

    SparkVersion(int code, String descp){
        this.code = code;
        this.descp = descp;
    }

    @EnumValue
    private final int code;
    private final String descp;

    public int getCode() {
        return code;
    }

    public String getDescp() {
        return descp;
    }
}
