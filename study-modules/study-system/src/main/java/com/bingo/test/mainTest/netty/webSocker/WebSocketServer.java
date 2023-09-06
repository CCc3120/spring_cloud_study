package com.bingo.test.mainTest.netty.webSocker;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author h-bingo
 * @date 2023/09/03 10:55
 **/
public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        start();
    }

    static void start() throws InterruptedException {
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
                                    // 因为基于 http 协议, 使用 http 的编解码器
                                    .addLast(new HttpServerCodec())
                                    // 以块方式写的, 添加 ChunkedWrite 处理器
                                    .addLast(new ChunkedWriteHandler())
                                    // http 在数据传输过程中是分段的, HttpObjectAggregator 可以将多个段聚合起来
                                    // 这就是为什么 浏览器在发送大量数据时, 就会发出多次 http 请求
                                    .addLast(new HttpObjectAggregator(8 * 1024))
                                    // 鉴权
                                    .addLast(new AuthHandler())
                                    // 对于 websocker 是以 帧(frame) 形式传递
                                    // 可以看到 WebSocketFrame 下面有六个子类
                                    // 浏览器请求时 ws://localhost:8088/xxx 表示请求的 uri (websocket 请求地址)
                                    // WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议, 保持长连接
                                    // http 升级到 ws 是用过一个状态码 101
                                    .addLast(new WebSocketServerProtocolHandler("/netty/websocket"))
                                    // 处理 自定义的 Handler
                                    .addLast(new WebSocketServerHandler())
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
