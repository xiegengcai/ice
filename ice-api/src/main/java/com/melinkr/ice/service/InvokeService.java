package com.melinkr.ice.service;

import com.melinkr.ice.response.IceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 访问次数、频率服务,可以针对 appKey, sessionId, method ,IP进行限制
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface InvokeService {
    Logger logger = LoggerFactory.getLogger(InvokeService.class);
    /**
     * 计算应用、会话及用户服务调度总数
     * @param appKey
     * @param sessionId
     */
    default void countTimes(String appKey,String method,String version,String ip, String sessionId) {
        logger.info("系统调用 {}#{}, appKey:{}, version:{}, ip:{}, sessionId:{}", method, version, appKey,
                version, ip, sessionId);
    }

    /**
     * 用户服务访问超限
     * @param sessionId
     * @return
     */
    IceError invokeLimit(String appKey, String method, String version, String clientIP, String sessionId);
}
