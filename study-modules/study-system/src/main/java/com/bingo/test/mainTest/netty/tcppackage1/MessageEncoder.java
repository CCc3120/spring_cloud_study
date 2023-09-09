package com.bingo.test.mainTest.netty.tcppackage1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author h-bingo
 * @date 2023/09/09 10:57
 **/
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MessageEncoder~~~");
        // 将长度和内容分开发送
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
