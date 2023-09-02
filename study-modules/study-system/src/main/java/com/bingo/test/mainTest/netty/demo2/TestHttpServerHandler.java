package com.bingo.test.mainTest.netty.demo2;

import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.web.response.RSXFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * HttpObject 客户端和服务区端通讯的数据封装成 HttpObject
 *
 * @author h-bingo
 * @date 2023/08/31 19:36
 **/
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // channelRead0读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断类型
        if (msg instanceof HttpRequest) {

            // ctx.read();

            System.out.println("msg类型: " + msg.getClass());
            System.out.println("客户端地址: " + ctx.channel().remoteAddress());

            // 浏览器刷新每次都会生成新的连接,即新的pipeline
            System.out.println("pipeline: " + ctx.channel().pipeline().hashCode() + ", handler: " + ctx.handler().hashCode());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                return;
            }

            // 回复消息给浏览器 [http协议]
            ByteBuf byteBuf = Unpooled.copiedBuffer(JsonMapper.getInstance().toJsonString(RSXFactory.success("你好")), CharsetUtil.UTF_8);

            // 构造 http 响应
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            ctx.channel().writeAndFlush(httpResponse);
        }
    }

    // 处理器添加
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    // 处理器移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
