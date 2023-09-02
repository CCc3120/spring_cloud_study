package com.bingo.test.mainTest.netty.demo2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * @author h-bingo
 * @date 2023/08/31 19:36
 **/
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 向管道中加入处理器
        ChannelPipeline pipeline = channel.pipeline();
        pipeline
                // HttpServerCodec netty提供的http编解码器
                .addLast(new HttpServerCodec())
                // .addLast("MyHttpServerCodec", new HttpServerCodec())
                .addLast(new TestHttpServerHandler())
        ;
    }
}
