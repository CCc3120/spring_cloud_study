package com.bingo.test.mainTest.chatDemo;

import scala.collection.mutable.StringBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author h-bingo
 * @date 2023/08/26 16:11
 **/
public class GroupChatClient {

    private int port = 8088;

    private String host = "127.0.0.1";

    private SocketChannel socketChannel;

    private Selector selector;

    public static void main(String[] args) {
        new GroupChatClient().connect();
    }

    public GroupChatClient() {
        try {
            socketChannel = SocketChannel.open();

            socketChannel.configureBlocking(false);

            selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            socketChannel.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            // while (!socketChannel.finishConnect()) {
            //     System.out.println("客户端正在连接中，请耐心等待");
            // }
            // System.out.println(socketChannel.getLocalAddress() + "连接成功");

            while (true) {
                // select()方法会阻塞
                // select(time)方法不会阻塞
                int select = selector.select();

                if (select == 0) {
                    System.out.println("没有事件发生");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isConnectable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();

                        if (channel.finishConnect()) {
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getLocalAddress() + "连接成功");

                            executorService.submit(() -> sendMsg(channel));

                            // 修改监听模式
                            // selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    }
                    if (selectionKey.isReadable()) {
                        readData(selectionKey);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SocketChannel channel) {
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
    }

    public void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            // 获取到channel, 其实是新连接注册的channel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 新连接注册的时候附带的参数
            // ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            ByteBuffer allocate = ByteBuffer.allocate(128);
            // 读取数据
            StringBuilder str = new StringBuilder();

            while (socketChannel.read(allocate) > 0) {
                str.append(new String(allocate.array()));

                allocate.clear();
            }

            System.out.println(socketChannel.getRemoteAddress() + "说：" + str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
