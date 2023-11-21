package com.mdosys.scheduler.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum StateEventType {

    PROCESS_STATE_CHANGE(0, "process statechange"),
    TASK_STATE_CHANGE(1, "task state change"),
    PROCESS_TIMEOUT(2, "process timeout"),
    TASK_TIMEOUT(3, "task timeout");

    StateEventType(int code, String descp) {
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
