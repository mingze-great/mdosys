package com.mdosys.scheduler.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * warning message notification method
 */
public enum AlertType {
    /**
     * 0 email; 1 SMS
     */
    EMAIL(0, "email"),
    SMS(1, "SMS");


    AlertType(int code, String descp){
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
