package com.bingo.test.mainTest.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 * <p>
 * SimpleChannelInboundHandler<StudentPOJO.Student>
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
        // 读取从客户端发送的 StudentPOJO.Student
        // StudentPOJO.Student student = (StudentPOJO.Student) msg;
        //
        // System.out.println("客户端发送的数据: " + student.getId());
        // System.out.println("客户端发送的数据: " + student.getName());

        MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = myMessage.getStudent();
            System.out.println(student.getName() + ":" + student.getId());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = myMessage.getWorker();
            System.out.println(worker.getName() + ":" + worker.getAge());
        } else {
            System.out.println("传输的类型不正确");
        }
    }

    /**
     * 数据读取完毕事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

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
