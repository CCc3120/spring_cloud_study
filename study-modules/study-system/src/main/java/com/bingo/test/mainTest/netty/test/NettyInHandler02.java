package com.bingo.test.mainTest.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author h-bingo
 * @Date 2023-08-30 11:41
 * @Version 1.0
 */
public class NettyInHandler02 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx02: " + ctx);
        ByteBuf byteBuf = (ByteBuf) msg;

        String string = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("server receive msg: " + string);

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 02 ~~~", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }
}
