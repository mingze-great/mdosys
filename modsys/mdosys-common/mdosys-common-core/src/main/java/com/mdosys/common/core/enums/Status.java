package com.mdosys.common.core.enums;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

public enum Status {
    SUCCESS(200, "success", "操作成功"),
    CREATED(201,"created success","对象创建成功"),
    ACCEPTED(202,"request accepted","请求已经被接受"),
    SUCCESS_NO_CONTENT(204,"request success,but no content","操作已经执行成功，但是没有返回数据"),
    TOKEN_FAILED(401,"token failed","用户令牌已过期"),
    UNAUTHORIZED(402,"request unauthorized","未授权"),
    FORBIDDEN(403,"forbidden","访问受限，授权过期"),
    NOT_FOUND(404,"{0} not found","{0} 资源，服务未找到"),

    NOT_IMPLEMENTED(501,"api not implemented:{0}","接口未实现:{0}"),
    ERROR(500,"error","错误"),


    INTERNAL_SERVER_ERROR_ARGS(10000, "Internal Server Error: {0}", "服务端异常: {0}"),
    REQUEST_PARAMS_NOT_VALID_ERROR(10001, "request parameter {0} is not valid", "请求参数[{0}]无效"),
    CODE_GENERATE_ERROR(10002,"[{0}] code generator error","实体[{0}]全局唯一标识码生成错误"),
    CREATE_PROJECT_ERROR(10048, "create project error", "创建项目错误"),
    PROJECT_NOT_FOUNT(10018, "project {0} not found ", "项目[{0}]不存在"),
    PROJECT_ALREADY_EXISTS(10019, "project {0} already exists", "项目名称[{0}]已存在"),
    PROJECT_UNAUTHORIZED_ERROR(10051, "unauthorized project error", "项目未授权错误");

    private final int code;
    private final String enMsg;
    private final String zhMsg;

    Status(int code, String enMsg, String zhMsg) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage())) {
            return this.zhMsg;
        } else {
            return this.enMsg;
        }
    }

    /**
     * Retrieve Status enum entity by status code.
     * @param code 状态码
     * @return 找到的status枚举
     */
    public static Optional<Status> findStatusBy(int code) {
        for (Status status : Status.values()) {
            if (code == status.getCode()) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
