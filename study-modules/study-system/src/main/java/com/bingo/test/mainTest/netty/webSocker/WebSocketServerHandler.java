package com.bingo.test.mainTest.netty.webSocker;

import com.bingo.study.common.core.utils.DateUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;

/**
 * TextWebSocketFrame 标识一个文本 帧
 *
 * @author h-bingo
 * @date 2023/09/03 10:58
 **/
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息: " + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + DateUtil.convertDateToString(new Date())));
    }

    // 当 web 客户端连接后, 触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // channel id 表示唯一的值, LongText 是唯一的 ShortText 不是唯一的
        System.out.println("handlerAdded调用 " + ctx.channel().id().asShortText());
        System.out.println("handlerAdded调用 " + ctx.channel().id().asLongText());
    }

    // 当 web 客户端连接断开后, 触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // channel id 表示唯一的值, LongText 是唯一的 ShortText 不是唯一的
        System.out.println("handlerRemoved调用 " + ctx.channel().id().asShortText());
        System.out.println("handlerRemoved调用 " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close();
    }
}
