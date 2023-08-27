package com.bingo.test.mainTest.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author h-bingo
 * @date 2023/08/27 10:46
 **/
public class FileIOClient {

    public void NewIOClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8088));

        String fileName = "";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        // linux系统下,transferTo方法直接可以完成传输
        // window系统下,transferTo一次只能发送8M大小文件,需要分段传输
        // transferTo 底层用的零拷贝
        long l = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数: " + l);
    }

    public void OldIOClient() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8088);

        String fileName = "";

        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[2048];
        long readCount;

        long total = 0;

        while ((readCount = inputStream.read(bytes)) >= 0) {
            total += readCount;
            dataOutputStream.write(bytes);
        }

        System.out.println("发送总字节数: " + total);

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
