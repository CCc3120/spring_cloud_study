package com.bingo.test.mainTest.chatDemo;

import scala.collection.mutable.StringBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author h-bingo
 * @date 2023/08/26 11:45
 **/
public class GroupChatServer {

    private int port = 8088;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.bind(new InetSocketAddress(port));

            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("GroupChatServer 初始化失败");
        }
    }

    public void listen() {
        try {
            while (true) {
                int select = selector.select();
                if (select == 0) {
                    System.out.println("没有事件发生");
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();

                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ);

                        System.out.println(socketChannel.getRemoteAddress() + "上线");
                    }

                    if (selectionKey.isReadable()) {

                        readData(selectionKey);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

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

            sendToMsgOtherClient(str.toString(), socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                // 断开了 取消注册 关闭channel
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void sendToMsgOtherClient(String msg, SocketChannel socketChannel) {
        try {
            for (SelectionKey selectionKey : selector.keys()) {
                Channel channel = selectionKey.channel();
                //socketChannel != null && socketChannel != channel &&
                if (socketChannel != null && socketChannel != channel && channel instanceof SocketChannel) {
                    SocketChannel to = (SocketChannel) channel;

                    ByteBuffer allocate = ByteBuffer.allocate(128);
                    byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

                    int count = (bytes.length % 128 == 0) ? (bytes.length / 128) : (bytes.length / 128) + 1;

                    for (int i = 0; i < count; i++) {
                        int begin = i * 128;
                        int end = (((i + 1) * 128) > bytes.length) ? bytes.length - i * 128 : 128;
                        allocate.put(bytes, begin, end);

                        allocate.flip();

                        to.write(allocate);

                        allocate.clear();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        executorService.execute(() -> new GroupChatServer().listen());
    }
}
