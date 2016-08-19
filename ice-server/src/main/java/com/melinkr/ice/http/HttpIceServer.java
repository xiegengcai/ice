package com.melinkr.ice.http;

import com.melinkr.ice.IceInitializer;
import com.melinkr.ice.IceServer;
import com.melinkr.ice.http.handler.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * <pre>HTTP服务实现</pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
//@Component("httpServer")
public class HttpIceServer implements IceServer {

    private final Logger logger = LoggerFactory.getLogger(HttpIceServer.class);

    private IceInitializer iceInitializer;

    @Autowired
    @Qualifier("bossGroup")
    private NioEventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workerGroup")
    private NioEventLoopGroup workerGroup;

    @Autowired
    private InetSocketAddress socketAddress;

    private Channel channel;

    public HttpIceServer(IceInitializer iceInitializer) {
        this.iceInitializer = iceInitializer;
    }

    @PostConstruct
    @Override
    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(iceInitializer);

        try {
            channel = b.bind(socketAddress).sync().channel().closeFuture().channel();
            logger.info("HttpIceServer started at port {}", socketAddress.getPort());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    @PreDestroy
    public void stop() {
        if (this.channel != null) {
            this.channel.close().addListener(ChannelFutureListener.CLOSE);
        }
    }
}
