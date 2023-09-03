package com.bingo.test.mainTest.netty.Heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author h-bingo
 * @date 2023/09/03 10:20
 **/
public class HeartServer {
    public static void main(String[] args) throws InterruptedException {
        start();
    }

    public static void start() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // boss 的日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // IdleStateHandler netty 提供的处理空闲状态处理器
                                    // long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
                                    // long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                                    // long allIdleTime : 表示多长时间没有读和写, 就会发送一个心跳检测包检测是否连接
                                    // 当 IdleStateHandler 触发后, 就会传递给管道 的下一个 Handler 处理,
                                    // 通过调用(触发)下一个 Handler 的 userEventTriggered, 在该方法中去处理 读空闲 写空闲 读写空闲
                                    .addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS))
                                    // 加入一个对空闲检测进一步处理的 Handler (自定义的)
                                    .addLast(new HeartServerHandler())
                            ;
                        }
                    })
            ;
            // 绑定端口并且同步，返回channelFuture对象，启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(8088).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
