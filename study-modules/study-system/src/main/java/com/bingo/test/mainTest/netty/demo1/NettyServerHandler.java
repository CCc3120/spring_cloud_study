package com.bingo.test.mainTest.netty.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件
     *
     * @param ctx 上下文对象, 含有 管道pipeline, 通道 channel, 地址 等
     * @param msg 客户端发送的数据, 默认 Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        System.out.println("服务器线程名: " + Thread.currentThread().getName());

        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline(); // 本质是一个双向链表, 出站入站

        // ByteBuf 是 netty 提供的, 不是 nio 的 ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("客户端发送的消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址: " + ctx.channel().remoteAddress());

        // 如果当前业务处理耗时严重(耗时长)会阻塞后面的 handler
        // 此时可以采用异步执行, 提交该 channel 对应的 NioEeventGroup 的 taskQueue
        ctx.channel().eventLoop().submit(() -> {
            // 处理耗时任务

        });
        // ctx.executor().submit(() -> {
        //
        //
        // });

        // 提交定时任务, 该任务提交到 scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(() -> {

        }, 3, TimeUnit.SECONDS);
        // ctx.executor().schedule(() -> {
        //
        // }, 1000, TimeUnit.SECONDS);
    }

    /**
     * 数据读取完毕事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓冲并刷新
        // 一般来说,需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
