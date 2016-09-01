package com.melinkr.ice.context;

import com.melinkr.ice.Session;
import com.melinkr.ice.annotation.HttpAction;
import com.melinkr.ice.handler.ServiceMethodHandler;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;

/**
 * 接到服务请求后，将产生一个{@link IceRequestContext}上下文对象，它被本次请求直到返回响应的这个线程共享。
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class IceRequestContext {

    //处理方法Handler
    private ServiceMethodHandler serviceMethodHandler;

    //请求的方法
    private String method;

    //请求的版本
    private String version;

    //请求的开发者账号
    private String appKey;

    //会话id
    private String sessionId;

    //签名
    private String sign;

    //时间戳
    private String timestamp;

    //请求ip
    private String ip;

    //请求id
    private String requestId;

    //session
    private Session session;
    private long serviceBeginTime = -1;
    private long serviceEndTime = -1;

    //请求有参数
    private Map<String, String> allParams;

    private List<ObjectError> paramErrors;
    private HttpAction httpAction;

    public ServiceMethodHandler getServiceMethodHandler() {
        return serviceMethodHandler;
    }

    public void setServiceMethodHandler(ServiceMethodHandler serviceMethodHandler) {
        this.serviceMethodHandler = serviceMethodHandler;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public long getServiceBeginTime() {
        return serviceBeginTime;
    }

    public void setServiceBeginTime(long serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    public long getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(long serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public List<ObjectError> getParamErrors() {
        return paramErrors;
    }

    public void setParamErrors(List<ObjectError> paramErrors) {
        this.paramErrors = paramErrors;
    }

    public Map<String, String> getAllParams() {
        return allParams;
    }

    public void setAllParams(Map<String, String> allParams) {
        this.allParams = allParams;
    }

    public String getParam(String name){
        return this.allParams.get(name);
    }

    public HttpAction getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(HttpAction httpAction) {
        this.httpAction = httpAction;
    }
}
