package com.bingo.test.mainTest.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author h-bingo
 * @Date 2023-07-21 11:33
 * @Version 1.0
 */
public class AIOClient {
    public static void main(String[] args) throws Exception {
        // demo();

        AIOClientThread client = new AIOClientThread();
        new Thread(client).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();

            client.sendMessage(next);
        }
    }

    public static void demo() throws Exception {
        String host = "127.0.0.1";
        int port = 8088;

        ExecutorService executorService = Executors.newCachedThreadPool();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();

        asynchronousSocketChannel.connect(inetSocketAddress).get();

        executorService.submit((Runnable) () -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String next = scanner.next();

                asynchronousSocketChannel.write(ByteBuffer.wrap(next.getBytes(StandardCharsets.UTF_8)));
            }
        });

        executorService.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(128);

            try {
                Integer integer = asynchronousSocketChannel.read(buffer).get();

                if (integer > 0) {
                    System.out.println("客户端收到消息:" + new String(buffer.array(), 0, integer));
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
