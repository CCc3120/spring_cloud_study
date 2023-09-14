package com.bingo.test.mainTest.netty.customDecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        long l = 123123123123L;
        ctx.writeAndFlush(l);

        /**
         * MessageToByteEncoder 编码器的父类中, write 方法会判断当前 msg 是不是应该处理的类型, 如果是就执行编码处理
         * 如果不是就跳过
         *
         * {@link MessageToByteEncoder#write(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.channel.ChannelPromise)}
         */
        // 通过 Unpooled.copiedBuffer 发送 bytebuf 不会调用自定义编码器
        // ctx.writeAndFlush(Unpooled.copiedBuffer("abababababababab", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("msg= " + msg);

        // ctx.writeAndFlush(984651L);
    }
}
