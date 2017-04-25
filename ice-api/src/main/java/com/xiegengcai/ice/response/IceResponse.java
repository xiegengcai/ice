package com.xiegengcai.ice.response;


/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.
 */
public class IceResponse<T> {
    /**
     * 响应码：非0错误
     */
    private int code;
    /**
     * 错误描述
     */
    private String message;
    /**
     * 业务数据
     */
    private T data;

    public IceResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public IceResponse(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}