package com.bingo.test.mainTest.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Author h-bingo
 * @Date 2023-07-21 15:58
 * @Version 1.0
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServerThread> {
    @Override
    public void completed(AsynchronousSocketChannel result, AIOServerThread attachment) {
        attachment.getServerChannel().accept(attachment, this); // 接收连接
        ByteBuffer buffer = ByteBuffer.allocate(100);
        result.read(buffer, buffer, new EchoHandler(result));
    }

    @Override
    public void failed(Throwable exc, AIOServerThread attachment) {
        System.err.println("客户端连接创建失败....");
        attachment.getLatch().countDown();
    }
}
