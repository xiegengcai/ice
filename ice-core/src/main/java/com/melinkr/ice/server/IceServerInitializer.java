package com.melinkr.ice.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public abstract class IceServerInitializer extends ChannelInitializer<SocketChannel> {

    protected IceHandler iceHandler;

    public IceServerInitializer(IceHandler iceHandler) {
        this.iceHandler = iceHandler;
    }
}