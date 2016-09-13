package com.melinkr.ice.context;

import com.melinkr.ice.annotation.ServiceMethod;
import com.melinkr.ice.annotation.WebService;
import com.melinkr.ice.exception.IceException;
import com.melinkr.ice.handler.ServiceMethodHandler;
import com.melinkr.ice.handler.ServiceMethodValue;
import com.melinkr.ice.request.IceRequest;
import com.melinkr.ice.utils.IceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Component
public class IceContext implements BeanPostProcessor {
    protected Logger logger = LoggerFactory.getLogger(IceContext.class);
    private Map<String, ServiceMethodHandler> serviceHandlerMap = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (AnnotationUtils.findAnnotation(bean.getClass(), WebService.class) != null) {
            ReflectionUtils.doWithMethods(bean.getClass(),
                    method -> {
                        //webservice 方法的注解
                        ServiceMethod serviceMethod = AnnotationUtils.findAnnotation(method, ServiceMethod.class);
                        //方法注解上的值
                        ServiceMethodValue serviceMethodValue = IceBuilder.buildServiceMethodValue(serviceMethod);
                        //处理方法的类
                        ServiceMethodHandler serviceMethodHandler = new ServiceMethodHandler();
                        //serviceMethodValue
                        serviceMethodHandler.setServiceMethodValue(serviceMethodValue);
                        //handler
                        serviceMethodHandler.setHandler(bean);
                        //method
                        serviceMethodHandler.setHandlerMethod(method);

                        if (method.getParameterTypes().length > 1) {
                            logger.error("参数必须是一个参数或者无参数");
                            throw new IceException("参数必须是一个参数或者无参数");
                        } else if (method.getParameterTypes().length == 1) {
                            Class<?> paramType = method.getParameterTypes()[0];
                            if (ClassUtils.isAssignable(IceRequest.class, paramType)) {
                                @SuppressWarnings("unchecked")
                                Class<? extends IceRequest> classType = (Class<? extends IceRequest>)paramType;
                                serviceMethodHandler.setRequestType(classType);
                                //忽略签名的列表
                                List<String> ignoreSignFieldNames = IceBuilder.buildNoSignFieldNames(classType);
                                serviceMethodHandler.setIgnoreSignFieldNames(ignoreSignFieldNames);
                            }else{
                                logger.error("参数必须继承IceRequest");
                                throw new IceException("参数必须继承IceRequest");
                            }
                        }
                        String serviceMethodKey = new StringBuilder(serviceMethodValue.getMethod()).append("#").append(serviceMethodValue.getVersion()).toString();
                        //存储
                        ServiceMethodHandler oldHandler = serviceHandlerMap.put(serviceMethodKey, serviceMethodHandler);
                        if(oldHandler != null){
                            logger.error("定义了重复的方法名+版本");
                            throw new IceException("定义了重复的方法名+版本数");
                        }
                        logger.info("注册ice服务{}", serviceMethodKey);
                    },
                    method -> !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ServiceMethod.class) != null
            );
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public ServiceMethodHandler getServiceMethodHandler(String method, String version){
        return serviceHandlerMap.get(method+"#"+version);
    }
}
