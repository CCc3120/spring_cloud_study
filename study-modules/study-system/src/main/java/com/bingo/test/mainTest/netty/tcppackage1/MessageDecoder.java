package com.bingo.test.mainTest.netty.tcppackage1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author h-bingo
 * @date 2023/09/09 11:00
 **/
public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder~~~");
        // 将得到的二进制字节码 转成 MessageProtocol 数据包(对象)

        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        // 封装成 MessageProtocol 对象, 放入out, 传递给下一个 Handler 处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(bytes);
        messageProtocol.setLen(length);
        out.add(messageProtocol);
    }
}
