package com.melinkr.ice.http.config;

import com.melinkr.ice.config.IceServerConfig;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
@Configuration
@ComponentScan("com.melinkr.ice")
@PropertySource("conf/iceServer.properties")
public class HttpIceServerConfig implements IceServerConfig {
    @Value("${server.port:8080}")
    private int port;
    @Value("${server.maxContentLength:67108864}")
    private int maxContentLength;
    @Value("#{'${black.ip}'.split(';')}")
    private Set<String> ipBlacklist;

    @Value("${boss.thread.bossThreadSize}")
    private int bossThreadSize;

    @Value("${boss.thread.wokerThreadSize}")
    private int wokerThreadSize;

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public int maxContentLength() {
        return this.maxContentLength;
    }

    @Override
    public Set<String> ipBlacklist() {
        return this.ipBlacklist;
    }


    @Override
    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossThreadSize);
    }

    @Override
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(wokerThreadSize);
    }

    @Override
    @Bean(name = "socketAddress")
    public InetSocketAddress socketAddress() {
        return new InetSocketAddress(this.port);
    }
}
