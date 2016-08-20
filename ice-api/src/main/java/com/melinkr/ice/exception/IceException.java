package com.melinkr.ice.exception;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class IceException extends RuntimeException {

    private static final long serialVersionUID = 4380345178425283103L;

    public IceException() {
    }

    public IceException(String message) {
        super(message);
    }

    public IceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IceException(Throwable cause) {
        super(cause);
    }
}
