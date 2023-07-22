package com.bingo.test.mainTest.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Author h-bingo
 * @Date 2023-07-21 15:58
 * @Version 1.0
 */
public class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel clientChannel;
    // 是否结束交互过程，exit = true表示结束，exit = false表示继续
    private boolean exit = false;

    public EchoHandler(AsynchronousSocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip(); // 读取之前需要执行重置处理
        // String readMessage = new String(attachment.array(), 0, attachment.remaining()).trim();
        String readMessage = new String(attachment.array(), 0, attachment.remaining());
        System.out.println("接收消息：" + readMessage);

        // String writeMessage = "【ECHO】" + readMessage;  // 回应的数据信息
        String writeMessage = readMessage;  // 回应的数据信息
        if ("byebye".equalsIgnoreCase(readMessage)) {
            writeMessage = "【EXIT】拜拜，下次再见！";
            this.exit = true; // 结束后期的交互
        }
        this.echoWrite(writeMessage);
    }

    private void echoWrite(String content) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put(content.getBytes());// 向缓存中保存数据
        buffer.flip();
        this.clientChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buf) {
                if (buf.hasRemaining()) {   // 缓存中是否有数据
                    EchoHandler.this.clientChannel.write(buffer, buffer, this);
                } else {
                    if (EchoHandler.this.exit == false) {    // 还没有结束
                        ByteBuffer readBuffer = ByteBuffer.allocate(100);
                        EchoHandler.this.clientChannel.read(readBuffer, readBuffer,
                                new EchoHandler(EchoHandler.this.clientChannel));
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    EchoHandler.this.clientChannel.close();
                } catch (IOException e) {
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
        }
    }
}
