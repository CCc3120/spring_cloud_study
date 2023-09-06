package com.bingo.test.mainTest.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-30 11:41
 * @Version 1.0
 */
public class NettyInHandler01 extends ChannelInboundHandlerAdapter {

    static List<Channel> list = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        list.add(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx01: " + ctx);
        ByteBuf byteBuf = (ByteBuf) msg;

        String string = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("server receive msg: " + string);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        for (Channel channel : list) {
            if (channel.isOpen()){
                System.out.println("打开");
            }

            if (channel.isActive()){
                System.out.println("激活");
            }

            if (channel.isRegistered()){
                System.out.println("注册");
            }

            channel.writeAndFlush(Unpooled.copiedBuffer("hello client 01 ~~~", CharsetUtil.UTF_8));
        }

        // super.channelReadComplete(ctx);
    }
}
