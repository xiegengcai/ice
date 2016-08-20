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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Component
public class IceContext implements ApplicationContextAware {
    protected Logger logger = LoggerFactory.getLogger(IceContext.class);
    //    / Spring应用上下文环境
    private static ApplicationContext context;
    private Map<String, ServiceMethodHandler> serviceHandlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 初始化注解等
     */
    @PostConstruct
    public void init() {
        logger.info("对Spring上下文中的Bean进行扫描，查找ice服务方法: {}", context);
        String[] beanNames = context.getBeanNamesForType(Object.class);
        for (final String beanName : beanNames) {
            Class<?> handlerType = context.getType(beanName);
            if (AnnotationUtils.findAnnotation(handlerType, WebService.class) != null) {
                ReflectionUtils.doWithMethods(handlerType,
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
                            serviceMethodHandler.setHandler(context.getBean(beanName));
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
                            //存储
                            ServiceMethodHandler oldHandler = serviceHandlerMap.put(serviceMethodValue.getMethod()+"#"+serviceMethodValue.getVersion(), serviceMethodHandler);
                            if(oldHandler != null){
                                logger.error("定义了重复的方法名+版本");
                                throw new IceException("定义了重复的方法名+版本数");
                            }
                        },
                        method -> !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ServiceMethod.class) != null
                );
            }
        }
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static <T>T getBean(String beanName , Class<T>clazz) {
        return context.getBean(beanName , clazz);
    }

    public ServiceMethodHandler getServiceMethodHandler(String method,String version){
        return serviceHandlerMap.get(method+"#"+version);
    }
}
