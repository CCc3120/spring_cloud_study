package com.bingo.test.mainTest.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author h-bingo
 * @Date 2023-07-21 09:10
 * @Version 1.0
 */
public class BIOClient {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 8088;

        Socket socket = new Socket(host, port);

        final OutputStream outputStream = socket.getOutputStream();

        final InputStream inputStream = socket.getInputStream();

        ExecutorService executorService = Executors.newCachedThreadPool();


        executorService.submit((Runnable) () -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {

                String next = scanner.next();

                try {
                    outputStream.write(next.getBytes(StandardCharsets.UTF_8));

                    outputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.submit(() -> {
            byte[] bytes = new byte[1024];

            int read;

            String line;

            // read 阻塞等待数据
            try {
                while ((read = inputStream.read(bytes)) != -1) {

                    line = new String(bytes, 0, read);
                    System.out.println("线程：" + Thread.currentThread().getName() + "接收数据：" + line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
