package com.bingo.test.mainTest.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Author h-bingo
 * @Date 2023-07-21 15:58
 * @Version 1.0
 */
public class AIOServerThread implements Runnable {

    private AsynchronousServerSocketChannel serverChannel = null; // 服务器通道
    private CountDownLatch latch = null; // 做一个同步处理操作

    public AIOServerThread() throws IOException {
        int port = 8088;
        InetSocketAddress endpoint = new InetSocketAddress(port);
        this.latch = new CountDownLatch(1);// 等待线程数量为1
        this.serverChannel = AsynchronousServerSocketChannel.open(); // 打开服务器的通道
        this.serverChannel.bind(endpoint); // 绑定端口
        System.out.println("服务器启动成功，监听端口为：" + port);
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public void run() {
        this.serverChannel.accept(this,new AcceptHandler()) ;
        try {
            this.latch.await(); // 线程等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
