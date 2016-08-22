package com.melinkr.ice.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.base.Throwables;
import com.melinkr.ice.Dispatcher;
import com.melinkr.ice.IceErrorCode;
import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.request.IceHttpRequest;
import com.melinkr.ice.response.IceError;
import com.melinkr.ice.response.IceResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public abstract class IceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final static String CONTENT_TYPE = "Content-Type";
    protected final static String X_FORWARDED_FOR = "X-Forwarded-For";
    protected final static String X_REAL_IP = "X-Real-IP";
    protected Map<String, String> params;
    @Autowired
    protected IceServerConfig iceServerConfig;
    @Autowired
    protected Dispatcher dispatcher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        logger.info("Received RamoteAddress[{}] {} request, uri={}", ctx.channel().remoteAddress(), request.method(), request.uri());
        IceResponse iceResponse = null;
        if (notSupported(request)) {
            logger.error("Not Supported HTTP Method.");
            iceResponse = new IceError(IceErrorCode.NOT_SUPPORTED_ACTION);
        } else {
            iceResponse = dispatcher.dispatch(builRequest(ctx, request));
        }
       httpResponse(ctx, iceResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("{}", Throwables.getStackTraceAsString(cause));
        if (cause instanceof JSONException) {
            httpResponse(ctx, new IceError(IceErrorCode.JSON_FORMAT_ERROR));
        } else if (cause instanceof TimeoutException){
            httpResponse(ctx, new IceError(IceErrorCode.EXECUTE_TIMEOUT));
        } else {
            httpResponse(ctx, new IceError(IceErrorCode.UNKNOW_ERROR));
        }
//        ctx.close();
    }

    private void httpResponse(ChannelHandlerContext ctx,IceResponse iceResponse) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        ByteBufUtil.writeUtf8(buf, JSON.toJSONString(iceResponse));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        ctx.channel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    public IceServerConfig serverConfig(){
        return this.iceServerConfig;
    }

    /**
     * 不支持的HTTP Method
     * @return
     */
    protected boolean notSupported(HttpRequest request) {
        return HttpMethod.POST != request.method() && HttpMethod.GET != request.method();
    }

    protected abstract IceHttpRequest builRequest(ChannelHandlerContext ctx, FullHttpRequest request);

    protected MediaType mediaType(FullHttpRequest request) {
        String contentType =  request.headers().get(CONTENT_TYPE);
        logger.info("{}:{}", CONTENT_TYPE, contentType);
        return MediaType.valueOf(contentType);
    }

    protected boolean needParseJson(FullHttpRequest request) {
        MediaType mediaType = mediaType(request);
        return HttpMethod.POST == request.method()
                && (MediaType.APPLICATION_JSON.equals(mediaType) || MediaType.APPLICATION_JSON_UTF8 .equals(mediaType));
    }

    protected String clientIP(ChannelHandlerContext ctx, FullHttpRequest request) {
        // nginx反向代理
        String remoteIp = request.headers().get(X_REAL_IP);
        if (StringUtils.hasText(remoteIp)) {
            return remoteIp;
        } else  {
            // apache反射代理
            remoteIp = request.headers().get(X_FORWARDED_FOR);
            if (StringUtils.hasText(remoteIp)) {
                String[] ips = remoteIp.split(",");
                for (String ip : ips) {
                    if (!"null".equalsIgnoreCase(ip)) {
                        return ip;
                    }
                }
            }
        }
        return ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
    }
}
