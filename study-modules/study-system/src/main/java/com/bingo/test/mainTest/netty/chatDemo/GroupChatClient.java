package com.bingo.test.mainTest.netty.chatDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author h-bingo
 * @date 2023/09/02 18:58
 **/
public class GroupChatClient {

    private int port;

    private String host;

    private Channel channel;

    public GroupChatClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringDecoder()) // netty 自带的解码器
                                    .addLast(new StringEncoder()) // netty 自带的编码器
                                    .addLast(new GroupChatClientHandler())
                            ;
                        }
                    })
            ;
            // 启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();

            channel = channelFuture.channel();
            channel.closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatClient groupChatClient = new GroupChatClient(8088, "127.0.0.1");
        new Thread(() -> {
            try {
                groupChatClient.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.next();
            groupChatClient.channel.writeAndFlush(s);
        }
    }
}
