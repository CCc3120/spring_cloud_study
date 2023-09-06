package com.bingo.test.mainTest.netty.webSocker;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;

/**
 * @Author h-bingo
 * @Date 2023-09-04 13:58
 * @Version 1.0
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest msg1 = (FullHttpRequest) msg;
            // 根据请求头的 auth-token 进行鉴权操作
            String authToken = msg1.headers().get("auth-token");
            System.out.println(">>>>>>>>>>>>鉴权操作");
            if (StrUtil.isEmpty(authToken)) {
                refuseChannel(ctx);
                return;
            }

            // 附加认证信息，用户身份信息
            // ctx.channel().attr(AttributeKey.valueOf("UserId")).get()
            AttributeKey<Object> attributeKey = AttributeKey.valueOf("UserId");
            ctx.channel().attr(attributeKey).setIfAbsent(authToken);
            System.out.println("鉴权成功");
        }
        // 继续调用其他处理器（链式）
        super.channelRead(ctx, msg);
    }

    private void refuseChannel(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
        ctx.channel().close();
    }
}
