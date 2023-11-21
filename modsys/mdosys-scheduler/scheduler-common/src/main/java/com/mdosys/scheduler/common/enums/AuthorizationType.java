package com.mdosys.scheduler.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * Authorization type
 */
public enum AuthorizationType {
    /**
     * 0 RESOURCE_FILE_ID;
     * 0 RESOURCE_FILE_NAME;
     * 1 UDF_FILE;
     * 1 DATASOURCE;
     * 2 UDF;
     */
    RESOURCE_FILE_ID(0, "resource file id"),
    RESOURCE_FILE_NAME(1, "resource file name"),
    UDF_FILE(2, "udf file"),
    DATASOURCE(3, "data source"),
    UDF(4, "udf function");

    AuthorizationType(int code, String descp){
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
