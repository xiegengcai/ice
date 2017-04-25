package com.xiegengcai.ice.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 *     使用该注解对服务方法进行标注，。
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceMethod {
    /**
     * 服务的方法名，即由method参数指定的服务方法名
     */
    String method() default "";

    /**
     * 请求方法，默认不限制
     */
    HttpAction[] httpAction() default {HttpAction.POST, HttpAction.GET};


    /**
     * 访问过期时间，单位为毫秒，即大于这个过期时间的链接会结束并返回错误报文，如果
     * 为0或负数则表示不进行过期限制
     *
     * @return
     */
    int timeout() default -1;

    /**
     * 该方法所对应的版本号，对应version请求参数的值，版本为空，表示不进行版本限定
     */
    String version() default "";

    /**
     * 服务方法需要需求会话检查，默认不检查
     */
    SessionType session() default SessionType.NO;

    /**
     * 是否要签名，默认不需要
     */
    SignType sign() default SignType.NO;

    /**
     * 是否开启开发者验证，默认不开启
     *
     * @return
     */
    AppKeyType openAppKey() default AppKeyType.NO;

    /**
     * 是否开启时间戳验证，默认不需要
     */
    TimestampType openTimestamp() default TimestampType.NO;

    /**
     * 服务方法是否已经过期，默认不过期
     */
    ObsoletedType obsoleted() default ObsoletedType.NO;
}

