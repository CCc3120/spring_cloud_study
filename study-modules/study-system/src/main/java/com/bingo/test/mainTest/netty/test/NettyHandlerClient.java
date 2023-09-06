package com.bingo.test.mainTest.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author h-bingo
 * @Date 2023-08-30 11:41
 * @Version 1.0
 */
public class NettyHandlerClient extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("netty client is ready");

        // ctx.writeAndFlush(Unpooled.copiedBuffer("hello server ~~~", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx client: " + ctx);
        ByteBuf byteBuf = (ByteBuf) msg;

        String string = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("client receive msg: " + string);
        // Unpooled.copiedBuffer()
    }
}
