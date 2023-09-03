package com.bingo.test.mainTest.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.net.InetSocketAddress;

/**
 * @author h-bingo
 * @date 2023/08/28 19:36
 **/
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        start();
    }

    private static void start() throws InterruptedException {

        // 客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            // 客户端使用的 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    // 设置线程组
                    .group(eventExecutors)
                    // 设置客户端通道实现类
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // 加入 protobufEncoder 编码器
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new NettyClientHandler())
                            ;
                        }
                    })
            ;

            // 启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8088)).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
