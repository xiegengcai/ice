package com.xiegengcai.ice.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xiegengcai.ice.annotation.HttpAction;
import com.xiegengcai.ice.request.IceHttpRequest;
import com.xiegengcai.ice.server.IceHandler;
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

    @Override
    protected IceHttpRequest builRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        params = JSON.parseObject(request.content().toString(CharsetUtil.UTF_8).trim()
                , new TypeReference<Map<String, String>>(){});
        return new IceHttpRequest(params, clientIP(ctx, request), HttpAction.fromValue(request.method().name()));
    }

    @Override
    protected boolean notSupported(HttpRequest request) {
        return HttpMethod.POST != request.method();
    }

}
