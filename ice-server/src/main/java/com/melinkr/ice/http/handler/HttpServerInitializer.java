package com.melinkr.ice.http.handler;

import com.melinkr.ice.IceHandler;
import com.melinkr.ice.IceInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
//@Component("serverInitializer")
public class HttpServerInitializer extends IceInitializer {

    public HttpServerInitializer(IceHandler iceHandler) {
        super(iceHandler);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("serverCodec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(iceHandler().serverConfig().maxContentLength()))
                .addLast("gzip", new HttpContentCompressor())
                .addLast("serverHandler", iceHandler());
    }
}
