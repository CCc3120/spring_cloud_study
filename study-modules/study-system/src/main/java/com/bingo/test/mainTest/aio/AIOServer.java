package com.bingo.test.mainTest.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * @Author h-bingo
 * @Date 2023-07-21 11:33
 * @Version 1.0
 */
public class AIOServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // ExecutorService executorService = Executors.newCachedThreadPool();
        //
        // executorService.submit(() -> {
        //     try {
        //         demo2();
        //     } catch (IOException e) {
        //         throw new RuntimeException(e);
        //     }
        // });

        // demo2();
        //
        // System.in.read();

        new Thread(new AIOServerThread()).start();
    }

    public static void demo2() throws IOException {
        int port = 8088;
        InetSocketAddress endpoint = new InetSocketAddress(port);
        AsynchronousServerSocketChannel asynchronousServerSocketChannel =
                AsynchronousServerSocketChannel.open().bind(endpoint);

        asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                System.out.println("当前线程1：" + Thread.currentThread().getName());

                asynchronousServerSocketChannel.accept(attachment, this);

                ByteBuffer buffer = ByteBuffer.allocate(128);

                socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        System.out.println("当前线程2：" + Thread.currentThread().getName());

                        attachment.flip();

                        byte[] data = new byte[buffer.remaining()];

                        attachment.get(data);

                        String s = new String(data, StandardCharsets.UTF_8);

                        System.out.println("当前线程2接收：" + s);


                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        buffer.put(s.getBytes(StandardCharsets.UTF_8));
                        buffer.flip();

                        socketChannel.write(buffer, buffer, this);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        exc.printStackTrace();
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });
    }

    public static void demo() throws IOException {
        int port = 8088;
        InetSocketAddress endpoint = new InetSocketAddress(port);
        AsynchronousServerSocketChannel asynchronousServerSocketChannel =
                AsynchronousServerSocketChannel.open().bind(endpoint);

        asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {

                System.out.println("当前线程1：" + Thread.currentThread().getName());

                // 接收客户端连接
                asynchronousServerSocketChannel.accept(attachment, this);

                try {
                    System.out.println("客户端" + socketChannel.getRemoteAddress() + "已连接");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ByteBuffer buffer = ByteBuffer.allocate(128);

                socketChannel.read(buffer, null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        System.out.println("当前线程2：" + Thread.currentThread().getName());

                        buffer.flip();

                        String x = new String(buffer.array(), 0, result);
                        System.out.println("当前线程2接收：" + x);

                        socketChannel.write(ByteBuffer.wrap(x.getBytes()));
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        exc.printStackTrace();
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
    }
}
