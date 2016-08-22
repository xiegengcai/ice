package com.melinkr.ice.config;

import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 服务配置接口定义
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public interface IceServerConfig {

    /**
     * 端口
     * @return
     */
    int port();

    int maxContentLength();
    /**
     * IP黑名单列表
     * @return
     */
    Set<String> ipBlacklist();

    NioEventLoopGroup bossGroup();

    NioEventLoopGroup workerGroup();

    InetSocketAddress socketAddress();

    /**
     * 系统是否开启签名验证
     */
    default boolean isOpenSign() {
        return Boolean.TRUE;
    }

    /**
     * 系统是否开启签名验证
     */
    default boolean isOpenAppKey() {
        return Boolean.TRUE;
    }

    /**
     * 系统是否开会话
     */
    default boolean isOpenSession() {
        return Boolean.FALSE;
    }

    /**
     * 系统是否开启时间戳验证
     */
    default boolean isOpenTimestamp() {
        return Boolean.FALSE;
    }

    /**
     * 请求时间与服务收到时间允许最大误差。单位：毫秒
     */
    default long getTimestampValue() {
        return TimeUnit.SECONDS.toMillis(60);
    }

    /**
     * 最大超时时间60s,单位秒
     */
    default int getMaxTimeout() {
        return 60;
    }

    /**
     * 缺省超时时间 30s,单位秒
     */
    default int getDefaultTimeout() {
        return 30;
    }
}
