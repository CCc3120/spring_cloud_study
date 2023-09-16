package com.bingo.test.mainTest.netty.rpc.netty;

import com.bingo.test.mainTest.netty.rpc.customer.ClientBootstrap;
import com.bingo.test.mainTest.netty.rpc.provider.RPCServerProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author h-bingo
 * @date 2023/09/16 10:45
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务
        System.out.println("服务端收到消息msg=" + msg);

        // 客户端在调用服务器的api时, 需要定义一个协议 例: "RPCService#getFirstStringDemo#"
        // if (msg.toString().startsWith("RPCService#getFirstStringDemo#")) {
        if (msg.toString().startsWith(ClientBootstrap.providerName)) {
            String s = new RPCServerProvider().getFirstStringDemo(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(s);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
