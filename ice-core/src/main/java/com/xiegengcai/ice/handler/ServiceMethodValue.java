package com.xiegengcai.ice.handler;

import com.xiegengcai.ice.annotation.HttpAction;

/**
 * <pre>
 * 功能说明：注解方法的值
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class ServiceMethodValue {

    /**
     * API的方法
     */
    private String method;

    /**
     * HTTP请求的方法
     */
    private HttpAction[] httpAction;

    /**
     * 过期时间，单位为秒，0或负数表示不过期
     */
    private int timeout = -9999;

    /**
     * 对应的版本号，如果为null或""表示不区分版本
     */
    private String version = null;

    /**
     * 是否需要进行会话校验,默认为要会话
     */
    private boolean needSession;

    /**
     * 默认为需要签名
     */
    private boolean needSign;

    /**
     * 是否开启时间戳验证
     */
    private boolean openTimestamp;

    /**
     * 服务方法是否已经过期,默认不过期
     */
    private boolean obsoleted;

    /**
     * 是否开启开发者验证
     */
    private  boolean openAppKey;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNeedSession() {
        return needSession;
    }

    public void setNeedSession(boolean needSession) {
        this.needSession = needSession;
    }

    public boolean isNeedSign() {
        return needSign;
    }

    public void setNeedSign(boolean needSign) {
        this.needSign = needSign;
    }

    public boolean isObsoleted() {
        return obsoleted;
    }

    public void setObsoleted(boolean obsoleted) {
        this.obsoleted = obsoleted;
    }

    public boolean isOpenTimestamp() {
        return openTimestamp;
    }

    public void setOpenTimestamp(boolean openTimestamp) {
        this.openTimestamp = openTimestamp;
    }

    public boolean isOpenAppKey() {
        return openAppKey;
    }

    public void setOpenAppKey(boolean openAppKey) {
        this.openAppKey = openAppKey;
    }

    public HttpAction[] getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(HttpAction[] httpAction) {
        this.httpAction = httpAction;
    }
}
