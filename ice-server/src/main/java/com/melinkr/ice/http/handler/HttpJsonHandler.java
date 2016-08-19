package com.melinkr.ice.http.handler;

import com.melinkr.ice.IceHandler;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
@ChannelHandler.Sharable
public class HttpJsonHandler extends IceHandler {

    protected String service(FullHttpRequest request) {
        // TODO
        return request.content().toString(CharsetUtil.UTF_8).trim();
    }

    @Override
    protected boolean notSupported(HttpRequest request) {
        return HttpMethod.POST != request.method();
    }
}
