package com.bingo.test.mainTest.netty.customDecoder2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.http.HttpObjectDecoder;

import java.util.List;

/**
 * 解码器
 * <p>
 * {@link LineBasedFrameDecoder} 行尾控制符(\n 或 \r\n)
 * <p>
 * {@link DelimiterBasedFrameDecoder} 使用自定义特殊字符作为消息分隔符
 * <p>
 * {@link HttpObjectDecoder} http 解码器
 * <p>
 * {@link LengthFieldBasedFrameDecoder} 通过指定长度来标识整包消息, 可以自动的处理黏包和半包消息
 *
 * @author h-bingo
 * @date 2023/09/06 19:10
 **/
public class ByteToLongDecoder extends ReplayingDecoder<Void> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder");
        // ReplayingDecoder 不需要判断数据是否足够读取, 内部会进行处理
        out.add(in.readLong());
    }
}
