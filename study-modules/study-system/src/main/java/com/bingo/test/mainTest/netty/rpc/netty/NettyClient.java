package com.bingo.test.mainTest.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author h-bingo
 * @date 2023/09/16 11:12
 **/
public class NettyClient {

    // 创建线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler nettyClientHandler;

    // 获取一个代理对象
    public Object getBean(Class<?> serviceClass, String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    if (nettyClientHandler == null) {
                        initClient("127.0.0.1", 8088);
                    }
                    // 设置要发送给服务器端的消息
                    // providerName 协议头, args[0] 是客户端调用 api 的参数
                    nettyClientHandler.setParam(providerName + args[0]);

                    return executorService.submit(nettyClientHandler).get();
                });
    }

    private static void initClient(String hostname, int port) {
        nettyClientHandler = new NettyClientHandler();

        EventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new StringEncoder())
                                            .addLast(new StringDecoder())
                                            .addLast(nettyClientHandler)
                                    ;
                                }
                            }
                    )
            ;

            ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();

            // channelFuture.channel().closeFuture().sync();

            System.out.println("客户端初始化完成~~~");
        } catch (Exception e) {
            loopGroup.shutdownGracefully();

            e.printStackTrace();
        }
    }
}
