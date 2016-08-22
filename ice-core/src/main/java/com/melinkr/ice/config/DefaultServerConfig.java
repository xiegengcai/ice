package com.melinkr.ice.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
@Configuration
@PropertySource("conf/iceServer.properties")
public class DefaultServerConfig implements IceServerConfig {
    @Value("${server.port:8080}")
    private int port;
    @Value("${server.maxContentLength:67108864}")
    private int maxContentLength;
    @Value("#{'${server.black.ip}'.split(';')}")
    private Set<String> ipBlacklist;

    @Value("${boss.thread.bossThreadSize}")
    private int bossThreadSize;

    @Value("${boss.thread.wokerThreadSize}")
    private int wokerThreadSize;
    @Value("${ice.service.openSign:true}")
    private boolean openSign;
    @Value("${ice.service.openSession:false}")
    private boolean openSession;
    @Value("${ice.service.openTimestamp:false}")
    private boolean openTimestamp;
    @Value("${ice.service.timestampRange:60}")
    private int timestampRange;
    @Value("${ice.service.maxTimeout:60}")
    private int maxTimeout;
    @Value("${ice.service.defaultTimeout:30}")
    private int defaultTimeout;

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
        return new InetSocketAddress(this.port());
    }

    @Override
    public boolean isOpenSign() {
        return this.openSign;
    }

    @Override
    public boolean isOpenSession() {
        return this.openSession;
    }

    @Override
    public boolean isOpenTimestamp() {
        return this.openTimestamp;
    }

    @Override
    public long timestampRange() {
        return TimeUnit.SECONDS.toMillis(this.timestampRange);
    }

    @Override
    public int maxTimeout() {
        return this.maxTimeout;
    }

    @Override
    public int defaultTimeout() {
        return this.defaultTimeout;
    }
}
