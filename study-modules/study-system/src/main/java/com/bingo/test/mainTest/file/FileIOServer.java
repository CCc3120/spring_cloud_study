package com.bingo.test.mainTest.file;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author h-bingo
 * @date 2023/08/27 10:43
 **/
public class FileIOServer {

    public void NewIOServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(new InetSocketAddress(8088));

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

        while (true) {

            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;

            while (readCount != -1) {
                readCount = socketChannel.read(byteBuffer);

                byteBuffer.clear(); //
                // byteBuffer.rewind(); // 和clear方法类似
            }
        }
    }

    public void OldIOServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);

        while (true) {
            Socket accept = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());

            byte[] bytes = new byte[2048];

            while (true) {
                int read = dataInputStream.read(bytes, 0, bytes.length);
                if (read == -1) {
                    break;
                }
            }
        }
    }
}
