package com.bingo.test.mainTest.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 同步阻塞IO，适用连接数比较小的架构，要求服务器资源高，编程简单
 *
 * @Author h-bingo
 * @Date 2023-07-21 09:10
 * @Version 1.0
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(8088);

        while (true) {

            // 阻塞等待连接
            final Socket accept = serverSocket.accept();

            executorService.submit(() -> handler(accept));
        }
    }

    private static void handler(Socket socket) {

        try {
            byte[] bytes = new byte[1024];

            InputStream inputStream = socket.getInputStream();

            OutputStream outputStream = socket.getOutputStream();

            int read;

            String line;

            // read 阻塞等待数据
            while ((read = inputStream.read(bytes)) != -1) {

                line = new String(bytes, 0, read);
                System.out.println("线程：" + Thread.currentThread().getName() + "接收数据：" + line);

                // 把接收到的回写
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
