package com.melinkr.ice.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public abstract class IceInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private IceHandler iceHandler;

//    public IceInitializer(IceHandler iceHandler) {
//        this.iceHandler = iceHandler;
//    }

    public IceHandler iceHandler() {
        return this.iceHandler;
    }
}
