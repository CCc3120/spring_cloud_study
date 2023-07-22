package com.bingo.test.mainTest.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author h-bingo
 * @Date 2023-07-21 09:43
 * @Version 1.0
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        // demo();

        demoSelector();
    }

    public static void demoSelector() throws Exception {
        String host = "127.0.0.1";
        int port = 8088;

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        socketChannel.connect(inetSocketAddress);

        // if (!socketChannel.connect(inetSocketAddress)) {
        //     while (!socketChannel.finishConnect()) {
        //         System.out.println("客户端正在连接中，请耐心等待");
        //     }
        // }

        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true) {
            int select = selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();

                if (next.isConnectable()) {
                    SocketChannel channel = (SocketChannel) next.channel();

                    if (channel.finishConnect()) {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("服务器连接成功");


                        executorService.submit((Runnable) () -> {

                            Scanner scanner = new Scanner(System.in);
                            while (true) {

                                String s = scanner.next();

                                try {
                                    ByteBuffer writeBuffer = ByteBuffer.wrap(s.getBytes());

                                    channel.write(writeBuffer);

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                } else if (next.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);

                    SocketChannel channel = (SocketChannel) next.channel();
                    int read = channel.read(byteBuffer);

                    if (read > 0) {
                        String s = new String(byteBuffer.array());
                        System.out.println("接收到消息:" + s);
                    }
                }

                iterator.remove();
            }
        }
    }

    public static void demo() throws Exception {
        String host = "127.0.0.1";
        int port = 8088;

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port));

        socketChannel.configureBlocking(false);

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit((Runnable) () -> {

            Scanner scanner = new Scanner(System.in);
            while (true) {

                String next = scanner.next();

                try {
                    ByteBuffer writeBuffer = ByteBuffer.wrap(next.getBytes());

                    socketChannel.write(writeBuffer);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        executorService.submit(() -> {
            while (true) {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);

                    int read = socketChannel.read(byteBuffer);

                    if (read > 0) {
                        System.out.println("接收到消息:" + new String(byteBuffer.array()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
