package com.melinkr.ice.dispatcher;

import com.google.common.base.Throwables;
import com.melinkr.ice.*;
import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.config.SystemParameterNames;
import com.melinkr.ice.context.IceContext;
import com.melinkr.ice.context.IceRequestContext;
import com.melinkr.ice.exception.IceException;
import com.melinkr.ice.handler.ServiceMethodHandler;
import com.melinkr.ice.request.IceHttpRequest;
import com.melinkr.ice.request.IceRequest;
import com.melinkr.ice.response.IceError;
import com.melinkr.ice.response.IceResponse;
import com.melinkr.ice.utils.IceBuilder;
import com.melinkr.ice.utils.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Component
public class IceDispatcher implements Dispatcher{
    private Logger logger = LoggerFactory.getLogger(IceDispatcher.class);
    @Autowired
    private IceContext iceContext;

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private InvokeService invokeService;

    @Autowired
    private IceServerConfig iceServerConfig;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    @Override
    public IceResponse dispatch(IceHttpRequest request) {
        final String method = request.getParameter(SystemParameterNames.getMehod());
        final String version = request
                .getParameter(SystemParameterNames.getVersion());
        ServiceMethodHandler serviceMethodHandler = iceContext.getServiceMethodHandler(method, version);
        if (serviceMethodHandler == null) {
            logger.info("不存在的服务方法:{}#{}", method, version);
            return new IceError(IceErrorCode.METHOD_ERROR);
        }
        // 执行超时时间
        int timeout = serviceMethodHandler.getServiceMethodValue()
                .getTimeout();
        if (timeout < 0) {
            // 默认
            timeout = iceServerConfig.defaultTimeout();
        } else if (timeout > iceServerConfig.maxTimeout()) {
            // 最大超时时间
            timeout = iceServerConfig.maxTimeout();
        }
//        return doInvoke(request);
        Future<IceResponse> future = executorService.submit(() -> doInvoke(request));
        IceErrorCode errorCode = IceErrorCode.UNKNOW_ERROR;
        try {
            future.get(timeout, TimeUnit.SECONDS);
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.info("调用服务方法:{}#{}，产生异常", method, version, e.getCause());
        } catch (ExecutionException e) {
            logger.info("调用服务方法:{}#{}，产生异常", method, version, e.getCause());
        } catch (TimeoutException e) {
            logger.info("调用服务方法:{}#{}，服务调用超时。", method, version);
            errorCode = IceErrorCode.EXECUTE_TIMEOUT;
        }
        return new IceError(errorCode);
    }

    private IceError validateSystemParameters(IceRequestContext requestContext) {
        //1方法名 2是否废弃 3 时间戳   4 开发者验证 5 会话验证 6签名验证 7调用次数验证 8权限验证 9拦截器  10 请求参数格式验证

        //2是否被废弃
        if(requestContext.getServiceMethodHandler().getServiceMethodValue().isObsoleted()){
            return new IceError(IceErrorCode.OBSOLETED_METHOD);
        }

        //4验证时间戳
        //是否开启时间戳
        if (iceServerConfig.isOpenTimestamp() && requestContext.getServiceMethodHandler().getServiceMethodValue().isOpenTimestamp()) {
            String timestamp = requestContext.getTimestamp();
            if(StringUtils.isEmpty(timestamp)){
                return  new IceError(IceErrorCode.TIMESTAMP_ERROR);
            }else{
                long urlTimestamp = Long.parseLong(timestamp);
                if(Math.abs(Calendar.getInstance().getTimeInMillis() - urlTimestamp) > iceServerConfig.timestampRange()){
                    return  new IceError(IceErrorCode.TIMESTAMP_ERROR);
                }
            }
        }
        //5session
        if(iceServerConfig.isOpenSession() && requestContext.getServiceMethodHandler().getServiceMethodValue().isNeedSession()){
            Session session =  sessionService.getSession(requestContext.getSessionId());
            if(session == null){
                return  new IceError(IceErrorCode.SESSION_TIMEOUT);
            }else{
                //往回写，防止多次调用 缓存中 getSession();
                requestContext.setSession(session);
            }
        }
        //6 开发者验证
        if(iceServerConfig.isOpenSign()){
            String secret = null;
            String appKey = requestContext.getAppKey();
            if(StringUtils.isEmpty(appKey)){
                return  new IceError(IceErrorCode.INVALID_APPKEY);
            }else{
                secret  = appKeyService.getSecretByAppKey(requestContext.getAppKey());
                if(StringUtils.isEmpty(secret)){
                    return  new IceError(IceErrorCode.INVALID_APPKEY);
                }
            }
            //7签名验证
            if (requestContext.getServiceMethodHandler().getServiceMethodValue().isNeedSign()) {
                List<String> ignoreSignFieldNames = requestContext.getServiceMethodHandler().getIgnoreSignFieldNames();
                Map<String, String> needSignParams = new HashMap<>();
                requestContext.getAllParams().entrySet().stream().filter(entry -> !ignoreSignFieldNames.contains(entry.getKey())).forEach(entry -> {
                    needSignParams.put(entry.getKey(), entry.getValue());
                });
                //7签名验证
                String signValue = SignUtils.sign(needSignParams, secret);
                if(!signValue.equals(requestContext.getSign())){
                    logger.info("server sign = {}, client sign = {}", signValue, requestContext.getSign());
                    //签名有误
                    return  new IceError(IceErrorCode.SIGN_ERROR);
                }
            }
        }

        //8调用次数
        invokeService.countTimes(requestContext.getAppKey(), requestContext.getMethod(),
                requestContext.getVersion(), requestContext.getIp(), requestContext.getSessionId());

        IceError invokeError = invokeService.invokeLimit(requestContext.getAppKey(), requestContext.getMethod(),
                requestContext.getVersion(), requestContext.getIp(), requestContext.getSessionId());
        if(invokeError !=null ){
            return invokeError;
        }

        return null;
    }

    private IceError validateMethodParameters(IceRequestContext requestContext) {
        List<ObjectError> errors = requestContext.getParamErrors();
        IceError error = null;
        if(errors != null && errors.size() > 0){
            error = new IceError(IceErrorCode.ILLEGAL_PARAMTERS);
            StringBuilder message = new StringBuilder();
            errors.stream().filter(objectError -> objectError instanceof FieldError).forEach(objectError -> {
                FieldError fieldError = (FieldError) objectError;
                message.append("filed:").append(fieldError.getField()).append(", errorCode:")
                        .append(fieldError.getCode()).append(", rejectedValue:")
                        .append(fieldError.getRejectedValue());
            });
            error.setMessage(message.toString());
        }
        return  error;
    }

    private Object invoke(IceRequest iceRequest) throws InvocationTargetException, IllegalAccessException{
        IceRequestContext  requestContext = iceRequest.getIceRequestContext();
        //有参数
        if(iceRequest.getIceRequestContext().getServiceMethodHandler().isHasParameter()){
            return requestContext.getServiceMethodHandler().getHandlerMethod().invoke (requestContext.getServiceMethodHandler().getHandler(), iceRequest);
            //无参数
        }else{
            return requestContext.getServiceMethodHandler().getHandlerMethod().invoke( requestContext.getServiceMethodHandler().getHandler());
        }
    }

    private IceResponse doInvoke(IceHttpRequest request) {
        try {
            // 1构建请求上下文
            IceRequestContext iceRequestContext = IceBuilder.buildIceRequestContext(iceContext, request);
            // 校验系统参数
            IceError error = validateSystemParameters(iceRequestContext);
            if (error != null) {
                return error;
            }
            IceRequest iceRequest = IceBuilder.buildIceRequest(iceRequestContext);
            // 校验方法参数
            error = validateMethodParameters(iceRequestContext);
            if (error != null) {
                return error;
            }
            // 正常业务
            return new IceResponse(invoke(iceRequest));
        } catch (IceException e) {
            logger.error("{}", Throwables.getStackTraceAsString(e));
            return new IceError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("{}", Throwables.getStackTraceAsString(e));
            IceError iceError= new IceError(IceErrorCode.UNKNOW_ERROR);
            iceError.setMessage(e.getMessage());
            return iceError;
        }
    }
}