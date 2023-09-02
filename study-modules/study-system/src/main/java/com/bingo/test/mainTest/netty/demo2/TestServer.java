package com.bingo.test.mainTest.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author h-bingo
 * @date 2023/08/31 19:35
 **/
public class TestServer {
    public static void main(String[] args) throws Exception {
        start();
    }

    private static void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                    // 设置 boss 和 worker 线程组
                    .group(boss, worker)
                    // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 线程队列连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new TestServerInitializer())
            ;

            // 绑定端口并且同步，返回channelFuture对象，启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(8088).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
