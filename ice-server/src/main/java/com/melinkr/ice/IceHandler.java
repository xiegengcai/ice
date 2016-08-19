package com.melinkr.ice;

import com.melinkr.ice.config.IceServerConfig;
import io.netty.channel.ChannelInboundHandler;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public interface IceHandler extends ChannelInboundHandler {

    IceServerConfig serverConfig();
}
