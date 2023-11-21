package com.mdosys.common.core.enums;

/**
 * 用户状态
 * 
 * @author ruoyi
 */
public enum UserStatus
{
    OK("0", "正常"),
    DISABLE("1", "停用"),
    DELETED("2", "删除");
//    USER_NAME_EXIST(10003, "user name already exists", "用户名已存在");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
