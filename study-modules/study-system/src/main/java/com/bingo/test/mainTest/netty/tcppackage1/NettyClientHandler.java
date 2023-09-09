package com.bingo.test.mainTest.netty.tcppackage1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送 10 条数据
        for (int i = 0; i < 5; i++) {
            String message = "学习Netty, 进大厂";
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            int length = bytes.length;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(bytes);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收数据, 并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();

        count++;
        System.out.println("client receive data: ");
        System.out.println("length: " + len);
        System.out.println("content: " + new String(content, CharsetUtil.UTF_8));
        System.out.println("client receive package number: " + count);
        System.out.println();
    }
}
