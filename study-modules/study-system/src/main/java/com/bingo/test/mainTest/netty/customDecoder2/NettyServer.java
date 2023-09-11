package com.bingo.test.mainTest.netty.customDecoder2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author h-bingo
 * @date 2023/08/28 19:03
 **/
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        start();
    }

    private static void start() throws InterruptedException {
        // bossLoopGroup 只处理连接请求
        // workerLoopGroup 处理客户端业务
        // bossLoopGroup 和 workerLoopGroup 含有的子线程个数 默认是 cpu 核数 * 2
        // boss组线程用不到8,可以自己设置线程数
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            // 服务启动器
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                    // 设置 boss 和 worker 线程组
                    .group(bossLoopGroup, workerLoopGroup)
                    // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 线程队列连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置相应的管道处理器 Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ByteToLongDecoder())
                                    .addLast(new LongToByteEncoder())
                                    // 添加自己的处理器
                                    .addLast(new NettyServerHandler());
                        }
                    })
            ;

            // 绑定端口并且同步，返回channelFuture对象，启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(8088).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }
    }
}
