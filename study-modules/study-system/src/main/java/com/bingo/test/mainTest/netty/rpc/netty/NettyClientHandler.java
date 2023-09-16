package com.bingo.test.mainTest.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author h-bingo
 * @date 2023/09/16 10:57
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context; // 上下文

    private String result; // 返回的结果

    private String param; // 客户端调用方法时传入的参数

    // 这个方法是第一个被调用的
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    // 需要同步 synchronized
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到消息msg=" + msg);
        result = msg.toString();

        // 唤醒等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    // 被代理对象调用, 发送数据给服务器, wait, 等待被唤醒, 返回结果
    @Override
    public synchronized Object call() throws Exception {

        context.writeAndFlush(param);
        // wait
        wait(); // 等待 channelRead 返回结果后唤醒

        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
