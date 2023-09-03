package com.bingo.test.mainTest.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 * <p>
 * SimpleChannelInboundHandler<StudentPOJO.Student>
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyServerHandler2 extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // System.out.println("客户端发送的数据: " + msg.getId());
        // System.out.println("客户端发送的数据: " + msg.getName());
        // ctx.fireChannelRead(msg);

        // 根据dataType 来显示不同信息

        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println(student.getName() + ":" + student.getId());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println(worker.getName() + ":" + worker.getAge());
        } else {
            System.out.println("传输的类型不正确");
        }
    }
}
