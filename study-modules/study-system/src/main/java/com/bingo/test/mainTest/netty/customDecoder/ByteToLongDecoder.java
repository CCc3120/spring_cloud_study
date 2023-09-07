package com.bingo.test.mainTest.netty.customDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author h-bingo
 * @date 2023/09/06 19:10
 **/
public class ByteToLongDecoder extends ByteToMessageDecoder {


    /**
     * decoder 会根据接受的数据, 被多次调用, 直到确定没有新的元素被添加到 list
     * 或者是 bytebuf 没有更多的可读字节为止
     * <p>
     * 如果 list 不为空, 就会将 list 的内容传递到下一个 ChannelInboundHandler 处理,
     * 该处理器的方法也会被多次调用
     *
     * @param ctx 上下文对象
     * @param in  入站的 butebuf
     * @param out 集合, 将解码后的数据传递给下一个 handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(this.getClass().getTypeName());
        // Long 是 8 个字节
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
