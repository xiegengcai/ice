package com.melinkr.ice.handler;

import com.melinkr.ice.request.IceRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     服务处理器的相关信息
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class ServiceMethodHandler {
    //处理器对象
    private Object handler;

    //处理器的处理方法
    private Method handlerMethod;

    //处理方法的注解值
    private ServiceMethodValue serviceMethodValue;

    //处理方法的请求对象类
    private Class<? extends IceRequest> requestType = IceRequest.class;

    //无需签名的字段列表
    private List<String> ignoreSignFieldNames = new ArrayList<>();

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public ServiceMethodValue getServiceMethodValue() {
        return serviceMethodValue;
    }

    public void setServiceMethodValue(ServiceMethodValue serviceMethodValue) {
        this.serviceMethodValue = serviceMethodValue;
    }

    public Class<? extends IceRequest> getRequestType() {
        return requestType;
    }

    public void setRequestType(Class<? extends IceRequest> requestType) {
        this.requestType = requestType;
    }

    public List<String> getIgnoreSignFieldNames() {
        return ignoreSignFieldNames;
    }

    public void setIgnoreSignFieldNames(List<String> ignoreSignFieldNames) {
        if(ignoreSignFieldNames !=null ){
            this.ignoreSignFieldNames.addAll(ignoreSignFieldNames);
        }
    }

    /**
     * 该方法名是否有请求参数
     */
    public boolean isHasParameter(){
        if(requestType != null){
            return true;
        }
        return false;
    }

}
