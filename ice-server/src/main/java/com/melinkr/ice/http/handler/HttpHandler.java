package com.melinkr.ice.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public abstract class HttpHandler {
    protected ChannelHandlerContext context;
    String CONTENT_TYPE_VALUE = "text/plain; charset=UTF-8";
//    void handler();
//    default HttpResponse response(String data){
//        ByteBuf buf = Unpooled.buffer();
//        ByteBufUtil.writeUtf8(buf, data);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
//        response.headers().set(Names.CONTENT_TYPE, CONTENT_TYPE_VALUE);
//        response.headers().setInt(Names.CONTENT_LENGTH, response.content().readableBytes());
//        return response;
//    }
}
