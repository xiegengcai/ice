package com.xiegengcai.ice.service;

/**
 * 密钥管理接口，需要实现类
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface AppKeyService {
    /**
     * 获取密钥
     * @param appKey
     */
    String getSecretByAppKey(String appKey);

    /**
     * 设置密钥
     * @param appKey
     * @param secret
     */
    void setSecretByAppKey(String appKey,String secret);
}
