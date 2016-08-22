package com.melinkr.ice.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

//    @Autowired
//    @Qualifier("bossGroup")
    private NioEventLoopGroup bossGroup;

//    @Autowired
//    @Qualifier("workerGroup")
    private NioEventLoopGroup workerGroup;

//    @Autowired
    private InetSocketAddress socketAddress;

    private Channel channel;

    public HttpIceServer(NioEventLoopGroup bossGroup, NioEventLoopGroup wokerGroup, InetSocketAddress socketAddress, IceInitializer iceInitializer) {
        this.bossGroup = bossGroup;
        this.workerGroup = wokerGroup;
        this.socketAddress = socketAddress;
        this.iceInitializer = iceInitializer;
    }

    @Override
    @PostConstruct
    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
//                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
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
