package com.bingo.test.mainTest.netty.chatDemo;

import com.bingo.study.common.core.utils.DateUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * @author h-bingo
 * @date 2023/09/02 19:02
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /*
        定义一个 ChannelGroup 组, 管理所有的 channel
        GlobalEventExecutor.INSTANCE 是全局的事件执行器, 是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // handlerAdded 表示连接建立, 一旦连接, 第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String dateToString = DateUtil.convertDateToString(new Date());
        Channel channel = ctx.channel();
        // 将该客户加人聊天的信息推送给其他在线的客户端
        // 该方法会将 ChannelGroup 中所有的 channel 遍历, 并发送该消息
        channelGroup.writeAndFlush("[客户端]" + dateToString + " " + channel.remoteAddress() + " 加入聊天");
        channelGroup.add(channel);
    }

    // 表示 channel 处于活动状态, 提示 xx 上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String dateToString = DateUtil.convertDateToString(new Date());
        System.out.println(ctx.channel().remoteAddress() + dateToString + " " + " 上线了");
    }

    // 表示 channel 处于非活动状态, 提示 xx 下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String dateToString = DateUtil.convertDateToString(new Date());
        System.out.println(ctx.channel().remoteAddress() + dateToString + " " + " 离线了");
    }

    // 表示断开连接, 将 xx 客户端离开信息推送给当前在线的用户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String dateToString = DateUtil.convertDateToString(new Date());
        Channel channel = ctx.channel();
        // channelGroup.remove(channel);
        // handlerRemoved 方法执行后, ChannelGroup 会自动移除掉 channel
        channelGroup.writeAndFlush("[客户端]" + dateToString + " " + channel.remoteAddress() + " 离开了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String dateToString = DateUtil.convertDateToString(new Date());

        Channel channel = ctx.channel();

        for (Channel c : channelGroup) {
            if (c == channel) {
                // 如果是当前的 channel
                c.writeAndFlush("[自己]发送了消息" + dateToString + " " + msg);
            } else {
                // 如果不是当前的 channel
                c.writeAndFlush("[客户]" + dateToString + " " + channel.remoteAddress() + " 发送了消息" + msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常关闭通道
        ctx.close();
    }
}
