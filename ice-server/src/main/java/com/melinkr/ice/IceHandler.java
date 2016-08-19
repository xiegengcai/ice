package com.melinkr.ice;

import com.alibaba.fastjson.JSON;
import com.melinkr.ice.config.IceServerConfig;
import com.melinkr.ice.response.IceError;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/19.
 */
public abstract class IceHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IceServerConfig iceServerConfig;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        logger.info("Received RamoteAddress[{}] {} request, uri={}", ctx.channel().remoteAddress(), request.method(), request.uri());

        ByteBuf buf = Unpooled.buffer();
        if (notSupported(request)) {
            logger.error("Not Supported HTTP Method.");
            ByteBufUtil.writeUtf8(buf, JSON.toJSONString(new IceError(IceErrorCode.NOT_SUPPORTED_ACTION)));
        } else {
            ByteBufUtil.writeUtf8(buf, service(request));
        }
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

    protected abstract String service(FullHttpRequest request);
}
