package com.melinkr.ice.exception;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class IceException extends RuntimeException {

    private static final long serialVersionUID = 4380345178425283103L;

    private int code;

    public IceException() {
    }

    public IceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public IceException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
