package com.melinkr.ice.annotation;

/**
 * <pre>
 *   请求类型的方法
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
public enum  HttpAction {
    GET, POST;

    public static HttpAction fromValue(String value) {
        if (GET.name().equalsIgnoreCase(value)) {
            return GET;
        }
        return POST;
    }
}
