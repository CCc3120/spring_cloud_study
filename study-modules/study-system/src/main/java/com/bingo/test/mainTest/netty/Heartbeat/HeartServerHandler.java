package com.bingo.test.mainTest.netty.Heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author h-bingo
 * @date 2023/09/03 10:36
 **/
public class HeartServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理心跳检测事件
        // 三种事件 按照配置的时间触发, 没有其他关联逻辑 例如 8 , 10, 5, 则 优先触发 读写事件, 然后 读 然后 写
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    // 读空闲
                    System.out.println("读空闲");
                    break;
                case WRITER_IDLE:
                    // 写空闲
                    System.out.println("写空闲");
                    break;
                case ALL_IDLE:
                    // 读写空闲
                    System.out.println("读写空闲");
                    break;
            }
            // ctx.close(); // 关闭后就不会有相关事件了

            System.out.println(ctx.channel().remoteAddress() + " 超时事件 " + event.state());
        }
    }
}
