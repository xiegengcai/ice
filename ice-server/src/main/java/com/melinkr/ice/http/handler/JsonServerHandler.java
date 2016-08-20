package com.melinkr.ice.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.melinkr.ice.Dispatcher;
import com.melinkr.ice.IceHandler;
import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.request.IceHttpRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.util.Map;

/**
 * 不解析querystring和post attribute
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
@ChannelHandler.Sharable
public class JsonServerHandler extends IceHandler {
    public JsonServerHandler(IceServerConfig iceServerConfig, Dispatcher dispatcher) {
        super(iceServerConfig, dispatcher);
    }

    @Override
    protected IceHttpRequest builRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        params.putAll(JSON.parseObject(request.content().toString(CharsetUtil.UTF_8).trim()
                , new TypeReference<Map<String, String>>(){}));
        return new IceHttpRequest(params, clientIP(ctx, request));
    }

    @Override
    protected boolean notSupported(HttpRequest request) {
        return HttpMethod.POST != request.method();
    }

}
