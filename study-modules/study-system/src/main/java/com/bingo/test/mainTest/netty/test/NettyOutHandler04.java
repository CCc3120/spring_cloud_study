package com.bingo.test.mainTest.netty.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * @Author h-bingo
 * @Date 2023-08-30 11:41
 * @Version 1.0
 */
public class NettyOutHandler04 extends ChannelOutboundHandlerAdapter {
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ctx04: " + ctx);
        super.read(ctx);
    }
}
