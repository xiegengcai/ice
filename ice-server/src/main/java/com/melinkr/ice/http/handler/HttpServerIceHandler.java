package com.melinkr.ice.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.melinkr.ice.IceHandler;
import com.melinkr.ice.codec.SimpleQueryStringDecoder;
import com.melinkr.ice.config.IceServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
//@Component("serverHandler")
@ChannelHandler.Sharable
public class HttpServerIceHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements IceHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IceServerConfig iceServerConfig;

    private Map<String, Object> params;

    private final DefaultHttpDataFactory httpDataFactory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        logger.info("Received RamoteAddress[{}] {} request, uri={}", ctx.channel().remoteAddress(), request.method(), request.uri());
        params = new SimpleQueryStringDecoder(request.uri()).parameters();
        if (params.size() == 0) { // Collections.EMPTY_MAP
            params = new HashMap<>();
        }
        if (HttpMethod.POST != request.method() && HttpMethod.GET != request.method()) {
            logger.error("Not Supported HTTP Method.");
        }
        // 处理POST数据
       if (HttpMethod.POST == request.method()) {
           HttpPostRequestDecoder  decoder = new HttpPostRequestDecoder (httpDataFactory, request);
           for (InterfaceHttpData httpData : decoder.getBodyHttpDatas()) {
               if (httpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                   Attribute attribute = (Attribute) httpData;
                   params.put(attribute.getName(),  attribute.getValue());
               }
           }
        }
        String httpContent = request.content().toString(CharsetUtil.UTF_8).trim();
        // POST JSON DATA
        if (StringUtils.hasText(httpContent)) {
            if (httpContent.startsWith("{") && httpContent.endsWith("}")) {
                params.putAll(JSON.parseObject(httpContent, new TypeReference<Map<String, Object>>(){}));
            }
        }
        ByteBuf buf = Unpooled.buffer();
        ByteBufUtil.writeUtf8(buf, JSON.toJSONString(params));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        ctx.channel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    public IceServerConfig serverConfig() {
        return iceServerConfig;
    }

}
