package com.bingo.test.mainTest.netty.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @Author h-bingo
 * @Date 2023-08-30 11:35
 * @Version 1.0
 */
public class NettyClient {

    private static Channel channel;

    public static void main(String[] args) throws Exception {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            channel.writeAndFlush(Unpooled.copiedBuffer(next, CharsetUtil.UTF_8));
        }
    }

    private static void start() throws InterruptedException {
        EventLoopGroup event = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    .group(event)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new NettyHandlerClient());
                        }
                    })
            ;
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8088).sync();

            channel = channelFuture.channel();
            channel.closeFuture().sync();
        } finally {
            event.shutdownGracefully();
        }
    }
}
