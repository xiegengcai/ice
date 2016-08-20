package com.melinkr.ice;

/**
 * 框架错误码定义
 *
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.
 */
public enum IceErrorCode {
    UNKNOW_ERROR(-1, "未知错误")
    , SUCCESS(0,"SUCCESS")
    , METHOD_ERROR(1, "方法或版本错误")
    , TIMESTAMP_ERROR(2, "时间戳错误")
    , OBSOLETED_METHOD(3, "方法已过期")
    , NOT_SUPPORTED_ACTION(4, "未支持的HTTP Method")
    , INVALID_APPKEY(5, "appKey不存在")
    , SIGN_ERROR(6, "签名错误")
    , ILLEGAL_PARAMTERS(7, "非法参数")
    , SESSION_TIMEOUT(8, "会话超时")
    , EXECUTE_TIMEOUT(9, "执行超时")
    , JSON_FORMAT_ERROR(10, "JSON数据格式错误")
    , FORBIDDEN(11, "IP受限")
    ;

    private int code;
    private String message;

    IceErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}