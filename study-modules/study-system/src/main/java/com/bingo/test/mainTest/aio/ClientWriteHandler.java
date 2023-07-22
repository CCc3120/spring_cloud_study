package com.bingo.test.mainTest.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @Author h-bingo
 * @Date 2023-07-21 16:20
 * @Version 1.0
 */
public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public ClientWriteHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            this.clientChannel.write(attachment, attachment, this);
        } else {
            ByteBuffer readBuffer = ByteBuffer.allocate(100); // 读取服务端回应
            this.clientChannel.read(readBuffer, readBuffer, new ClientReadHandler(this.clientChannel, this.latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.latch.countDown();
    }
}
