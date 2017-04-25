package com.xiegengcai.ice.utils;

import com.xiegengcai.ice.request.IceHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public final class RequestUtil {

    /**
     * 构建所有的请求参数封装成 map
     * @param request
     */
    public static Map<String, String> getRequestParams(IceHttpRequest request) {
        Map<String, String> destParamMap = new HashMap<>(request.getParams().size());
        for (Map.Entry<String, String> entry:request.getParams().entrySet()) {
            destParamMap.put(entry.getKey(), entry.getValue());
        }
        return destParamMap;
    }

    /**
     * 获取uuid for String
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
