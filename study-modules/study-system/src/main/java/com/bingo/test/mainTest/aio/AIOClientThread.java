package com.bingo.test.mainTest.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Author h-bingo
 * @Date 2023-07-21 16:21
 * @Version 1.0
 */
public class AIOClientThread implements Runnable {

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public AIOClientThread() {
        String host = "127.0.0.1";
        int port = 8088;
        try {
            this.clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clientChannel.connect(new InetSocketAddress(host, port));
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void run() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String msg) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put(msg.getBytes());
        buffer.flip();
        this.clientChannel.write(buffer, buffer, new ClientWriteHandler(this.clientChannel, this.latch));
        if ("byebye".equalsIgnoreCase(msg)) {
            return false;
        }
        return true;
    }
}
