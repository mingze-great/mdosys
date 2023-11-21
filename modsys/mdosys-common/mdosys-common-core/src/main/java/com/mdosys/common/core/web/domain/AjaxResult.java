package com.mdosys.common.core.web.domain;

import com.github.pagehelper.PageInfo;
import com.mdosys.common.core.enums.Status;
import com.mdosys.common.core.utils.PageUtils;
import com.mdosys.common.core.utils.StringUtils;
import com.mdosys.common.core.web.page.TableData;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author ruoyi
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";
    /**
     * 请求成功状态
     */
    private final String SUCCESS_TAG = "success";


    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(SUCCESS_TAG, this.isSuccess());
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(SUCCESS_TAG, this.isSuccess());
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success(Status.SUCCESS.getMsg());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success(Status.SUCCESS.getMsg(), data);
    }

    /**
     * 返回分页数据
     * @see TableData
     * @param data 分页数据内容
     * @return 成功消息
     */
    public static AjaxResult success(TableData<?> data) {
        return AjaxResult.success(Status.SUCCESS.getMsg(), data);
    }

    /**
     * 返回分页数据
     * @see TableData
     * @param pageInfo 分页数据内容,之后会被封装成TableData对象返回
     * @return 成功消息 内部封装了TableData
     */
    public static AjaxResult success(PageInfo<?> pageInfo) {
        TableData<?> data = new TableData<>(pageInfo.getTotal(),
                pageInfo.getList(),
                pageInfo.isHasNextPage(),
                pageInfo.getPages(),
                pageInfo.getPageNum()
        );
        PageUtils.clearPage();
        return AjaxResult.success(Status.SUCCESS.getMsg(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(Status.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param status       状态枚举
     * @param statusParams 状态枚举内部，返回msg文本的格式化参数
     * @return 成功消息
     */
    public static AjaxResult success(Status status, Object... statusParams) {
        return success(null, status, statusParams);
    }

    /**
     * 返回成功消息
     *
     * @param data         要返回的数据体对象
     * @param status       状态枚举
     * @param statusParams 状态枚举内部，返回msg文本的格式化参数
     * @return 成功消息
     */
    public static AjaxResult success(Object data, Status status, Object... statusParams) {
        if (status.getCode() > 300) {
            throw new RuntimeException(StringUtils.format("不要将错误状态码用与返回成功信息:{0}", status.toString()));
        }
        if (statusParams != null && statusParams.length > 0) {
            return new AjaxResult(status.getCode(), MessageFormat.format(status.getMsg(), statusParams), data);
        } else {
            return new AjaxResult(status.getCode(), status.getMsg(), data);
        }
    }


    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(Status.ERROR.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    @Deprecated
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param status       状态枚举
     * @param statusParams 状态枚举内部，返回msg文本的格式化参数
     * @return 警告信息
     */
    public static AjaxResult error(Status status, Object... statusParams) {
        return error(null, status, statusParams);
    }

    /**
     * 返回错误消息
     *
     * @param data         要返回的数据体对象
     * @param status       状态枚举
     * @param statusParams 要返回msg文本的格式化参数
     * @return 警告消息
     */
    public static AjaxResult error(Object data, Status status, Object... statusParams) {
        if (status.getCode() < 300) {
            throw new RuntimeException(StringUtils.format("不要将正确状态码用与返回error信息:{0}", status.toString()));
        }
        if (statusParams != null && statusParams.length > 0) {
            return new AjaxResult(status.getCode(), MessageFormat.format(status.getMsg(), statusParams), data);
        } else {
            return new AjaxResult(status.getCode(), status.getMsg(), data);
        }
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        int tCode = (int) this.get(CODE_TAG);
        return tCode >= 200 && tCode < 300;
    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 自身
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
