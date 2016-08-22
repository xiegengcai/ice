package com.melinkr.ice.server.handler;

import com.melinkr.ice.server.IceHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private IceHandler iceHandler;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("serverCodec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(iceHandler.serverConfig().maxContentLength()))
                .addLast("gzip", new HttpContentCompressor())
                .addLast("logger", new LoggingHandler(LogLevel.INFO))
                .addLast("serverHandler", iceHandler)
        ;
    }
}
