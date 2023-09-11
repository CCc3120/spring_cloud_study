package com.bingo.test.mainTest.netty.customDecoder2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author h-bingo
 * @date 2023/09/06 19:10
 **/
public class LongToByteEncoder extends MessageToByteEncoder<Long> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println(this.getClass().getTypeName());
        out.writeLong(msg);
    }
}
