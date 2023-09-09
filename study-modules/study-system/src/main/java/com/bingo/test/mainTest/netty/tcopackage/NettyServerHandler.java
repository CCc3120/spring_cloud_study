package com.bingo.test.mainTest.netty.tcopackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int i = msg.readableBytes();
        byte[] bytes = new byte[i];
        msg.readBytes(bytes);
        // 转成字符串
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("server msg = " + s);
        count++;
        System.out.println("服务器接受到消息量=" + count);

        // 服务器回送数据给客户端, 回送一个随机id
        ByteBuf byteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString() + " ", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
