package com.melinkr.ice.utils;

import com.melinkr.ice.annotation.*;
import com.melinkr.ice.config.SystemParameterNames;
import com.melinkr.ice.context.IceContext;
import com.melinkr.ice.context.IceRequestContext;
import com.melinkr.ice.handler.ServiceMethodValue;
import com.melinkr.ice.request.IceHttpRequest;
import com.melinkr.ice.request.IceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public final class IceBuilder {
    protected static Logger logger = LoggerFactory.getLogger(IceBuilder.class);

    private static FormattingConversionService conversionService;
    private static Validator validator;

    static {
        //conversionService
        FormattingConversionServiceFactoryBean serviceFactoryBean = new FormattingConversionServiceFactoryBean();
        serviceFactoryBean.afterPropertiesSet();
        conversionService = serviceFactoryBean.getObject();

        //validator
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;

    };


    /**
     * 构建请求上下文
     *
     * @param request
     */
    public static IceRequestContext buildIceRequestContext(IceContext iceContext, IceHttpRequest request) {

        IceRequestContext requestCtx = new IceRequestContext();
        //毫秒
        requestCtx.setServiceBeginTime(Calendar.getInstance().getTimeInMillis());

        String method = request.getParameter(SystemParameterNames.getMehod());
        String version = request.getParameter(SystemParameterNames.getVersion());
        requestCtx.setHttpAction(request.getHttpAction());
        //1
        requestCtx.setTimestamp(request.getParameter(SystemParameterNames.getTimestamp()));
        //2  xx1

        //除去 sign
        Map<String, String> allParams = RequestUtil.getRequestParams(request);
        allParams.remove(SystemParameterNames.getSign());
        //4
        requestCtx.setAllParams(allParams);
        //5
        requestCtx.setAppKey(request.getParameter(SystemParameterNames.getAppKey()));

        //requestCtx.setIceResponse(iceResponse);返回结果，执行的时候设置
        //9 xx4
        requestCtx.setIp(request.getClientIp());
        //10
        requestCtx.setMethod(method);
        //11 xx5
        requestCtx.setRequestId(RequestUtil.getUuid());

        // requestCtx.setServiceEndTime(serviceEndTime);//执行完成后设置
        //12
        requestCtx.setServiceMethodHandler(iceContext.getServiceMethodHandler(method, version));
        // requestCtx.setSession(session);//验证session的时候设置
        //13
        requestCtx.setSessionId(request.getParameter(SystemParameterNames.getSessionId()));
        //14
        requestCtx.setSign(request.getParameter(SystemParameterNames.getSign()));
        //15
        requestCtx.setVersion(version);

        return requestCtx;
    }


    /**
     * 构建请求参数JSR校验
     * @param iceRequestContext
     */
    public static IceRequest buildIceRequest(IceRequestContext iceRequestContext) {
        IceRequest iceRequest = null;
        if (iceRequestContext.getServiceMethodHandler().isHasParameter()) {

            Class<? extends IceRequest> classType = iceRequestContext.getServiceMethodHandler().getRequestType();
            IceRequest bindObject = BeanUtils.instantiateClass(classType);
            DataBinder dataBinder = new DataBinder(bindObject, "bindObject");
            dataBinder.setConversionService(conversionService);
            dataBinder.setValidator(validator);
            dataBinder.bind(new MutablePropertyValues(iceRequestContext.getAllParams()));
            dataBinder.validate();
            BindingResult bindingResult = dataBinder.getBindingResult();

            iceRequest = (IceRequest)bindingResult.getTarget();

            iceRequest.setIceRequestContext(iceRequestContext);
            //错误处理
            List<ObjectError> paramErrors = bindingResult.getAllErrors();
            iceRequestContext.setParamErrors(paramErrors);
        }
        return iceRequest;
    }


    /**
     * 构建注解值
     *
     * @param serviceMethod
     * @return {@link ServiceMethodValue}
     */
    public static ServiceMethodValue buildServiceMethodValue(ServiceMethod serviceMethod) {

        ServiceMethodValue service = new ServiceMethodValue();
        // 方法体名称
        service.setMethod(serviceMethod.method());
        //是否需要session验证
        service.setNeedSession(SessionType.isNeedInSession(serviceMethod.session()));
        // 是否需要签名验证
        service.setNeedSign(SignType.isNoSign(serviceMethod.sign()));
        // 开发者验证
        if (service.isNeedSign()) {
            service.setOpenAppKey(Boolean.TRUE);
        } else {
            service.setOpenAppKey(AppKeyType.isOpenAppKey(serviceMethod.openAppKey()));
        }
        // 是否过期
        service.setObsoleted(ObsoletedType.isObsoleted(serviceMethod.obsoleted()));
        //是否开启时间戳验证
        service.setOpenTimestamp(TimestampType.isOpenTimestamp(serviceMethod.openTimestamp()));
        // 超时
        service.setTimeout(serviceMethod.timeout());
        // 版本
        service.setVersion(serviceMethod.version());
        service.setHttpAction(serviceMethod.httpAction());
        return service;
    }

    /**
     * 构建不需要签名的属性列表
     * @param requestType
     * @return
     */
    public static List<String> buildNoSignFieldNames(
            Class<? extends IceRequest> requestType) {
        final ArrayList<String> igoreSignFieldNames = new ArrayList<>();
        if (requestType != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("获取" + requestType.getCanonicalName() + "不需要签名的属性");
            }
            ReflectionUtils.doWithFields(requestType,
                    field -> igoreSignFieldNames.add(field.getName()), field -> {
                        // 属性类标注了@NoSign
                        NoSign typeNo = AnnotationUtils.findAnnotation(field.getType(), NoSign.class);
                        if (typeNo != null)
                        {
                            return typeNo != null;
                        }
                        // 属性定义处标注了@NoSign
                        NoSign varNoSign = field.getAnnotation(NoSign.class);
                        // 属性定义处标注了@Temporary
                        Temporary varTemporary = field.getAnnotation(Temporary.class);
                        return varNoSign != null|| varTemporary != null;
                    });
            if (logger.isDebugEnabled() && igoreSignFieldNames.size() > 1) {
                logger.debug("{}不需要签名的属性：{}", requestType.getCanonicalName(), igoreSignFieldNames.toString());
            }
        }
        return igoreSignFieldNames;
    }

}
