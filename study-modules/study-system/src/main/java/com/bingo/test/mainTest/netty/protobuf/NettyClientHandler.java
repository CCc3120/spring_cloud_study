package com.bingo.test.mainTest.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

/**
 * 自定义一个handler,继承netty 规定好的 handlerAdapter
 *
 * @author h-bingo
 * @date 2023/08/28 19:21
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道就绪触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用 google protobuf 编解码
        // StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("张三").build();
        // 发送数据
        // ctx.writeAndFlush(student);

        // 随机发送 student 或者 worker 对象

        int i = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;

        if (0 == i) {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(23).setName("李四").build()).build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(111).setName("老王").build()).build();
        }
        ctx.writeAndFlush(myMessage);
    }

    /**
     * 读取数据事件
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
