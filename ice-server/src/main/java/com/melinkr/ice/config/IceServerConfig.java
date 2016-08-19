package com.melinkr.ice.config;

import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Set;

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

}
