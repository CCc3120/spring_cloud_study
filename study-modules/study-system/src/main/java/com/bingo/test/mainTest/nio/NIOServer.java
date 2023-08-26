package com.bingo.test.mainTest.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author h-bingo
 * @Date 2023-07-21 09:43
 * @Version 1.0
 */
public class NIOServer {


    public static void main(String[] args) throws Exception {
        // demo();

        // demoSelector();

        studySelector();
    }

    public static void studySelector() throws Exception {
        int port = 8088;

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(port));

        // 非阻塞
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        // 必须设置非阻塞,不然会报错
        SelectionKey register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            int select = selector.select();
            if (select == 0) {
                System.out.println("没有事件发生");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // Selector selector1 = selectionKey.selector();

                if (selectionKey.isAcceptable()) { // 如果是OP_ACCEPT,有新的客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞 需要注册到selector上的channel,都要设置非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("有新客户端连接");
                    // 新的连接也注册到当前selector上，关注的事件为 读
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(128));
                }

                if (selectionKey.isReadable()) {
                    // 获取到channel, 其实是新连接注册的channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 新连接注册的时候附带的参数
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // 读取数据
                    socketChannel.read(buffer);

                    System.out.println(new String(buffer.array()));

                    buffer.flip();

                    socketChannel.write(buffer);

                    buffer.clear();
                }

                // 手动移除,防止重复操作
                iterator.remove();
            }
        }
    }

    /**
     * SelectionKey.OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
     * SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
     * SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
     * SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
     * <p>
     * 1.当向通道中注册SelectionKey.OP_READ事件后，如果客户端有向缓存中write数据，下次轮询时，则会 isReadable()=true；
     * <p>
     * 2.当向通道中注册SelectionKey.OP_WRITE事件后，这时你会发现当前轮询线程中isWritable()一直为true，如果不设置为其他事件
     *
     * @throws Exception
     */
    public static void demoSelector() throws Exception {
        int port = 8088;

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress endpoint = new InetSocketAddress(port);
        serverSocketChannel.socket().bind(endpoint);
        // 设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 打开选择器
        Selector selector = Selector.open();

        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞一定时间，不得为负数，0表示无限阻塞 selector.select(0)
            int select = selector.select();
            // selector.select(2);
            // selector.selectedKeys();
            // 多线程建议用这个 selector.selectNow();
            // 因为select方法执行的是lockAndDoSelect方法，里面是用的synchronized锁住的SelectorImpl类的publicKeys变量 那么selector
            // .select就不能阻塞，因为一旦selector阻塞，而根据锁的可重入性，当前线程可以直接执行但是别的线程需要等待锁的释放select先执行，如果selector
            // .select查询不到可用的连接，那么就不会释放锁，而只有向selector注册连接才会有可用连接即socketChannel.register(selector)
            // ，这个方法也需要获取publicKeys对象锁，又因为是其他线程，这样就会造成死锁。 所以要selector.selectNow方法，查询到或者查不到都会释放锁，不会在获取到锁后阻塞
            // selector.selectNow();

            // System.out.println(select);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();

                if (next.isAcceptable()) { // 连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);  // 设置为非阻塞
                    System.out.println("连接成功");

                    socketChannel.register(selector, SelectionKey.OP_READ); // 注册客户端读取事件到selector
                    // socketChannel.register(selector, SelectionKey.OP_WRITE); // 注册客户端写取事件到selector
                } else if (next.isReadable()) { // 读
                    SocketChannel channel = (SocketChannel) next.channel();

                    // ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    //
                    // int read = channel.read(byteBuffer);
                    //
                    // if (read > 0) {
                    //     String s = new String(byteBuffer.array());
                    //     System.out.println("接收到消息:" + s);
                    //
                    //     channel.write(ByteBuffer.wrap(s.getBytes()));
                    // }

                    // ByteBuffer 分散和聚集

                    ByteBuffer[] byteBuffers = new ByteBuffer[2];
                    byteBuffers[0] = ByteBuffer.allocate(3);
                    byteBuffers[1] = ByteBuffer.allocate(3);

                    long read = channel.read(byteBuffers);
                    // System.out.println(read);

                    Arrays.stream(byteBuffers).forEach(byteBuffer -> System.out.print(new String(byteBuffer.array())));

                    Arrays.stream(byteBuffers).forEach(Buffer::flip);

                    channel.write(byteBuffers);
                }

                iterator.remove();
            }
        }
    }

    static List<SocketChannel> channelList = new ArrayList<>();

    public static void demo() throws Exception {
        int port = 8088;

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(port));

        // 设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);

        while (true) {

            // 非阻塞模式accept方法不会阻塞
            // NIO的非阻塞是由操作系统内部实现的，底层调用了linux内核的accept函数
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel != null) {
                System.out.println("连接成功");
                socketChannel.configureBlocking(false);
                channelList.add(socketChannel);
            }

            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                SocketChannel channel = iterator.next();
                // 缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);

                int read = channel.read(byteBuffer);

                if (read > 0) {
                    String s = new String(byteBuffer.array());
                    System.out.println("接收到消息:" + s);

                    channel.write(ByteBuffer.wrap(s.getBytes()));
                }
            }
        }
    }
}
