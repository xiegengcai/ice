package com.melinkr.ice.response;


import com.melinkr.ice.IceErrorCode;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class IceError extends IceResponse<Void> {
    public IceError(IceErrorCode errorCode){
        this(errorCode.getCode(), errorCode.getMessage());
    }
    public IceError(int code, String message) {
        super(code, message, null);
    }
}
