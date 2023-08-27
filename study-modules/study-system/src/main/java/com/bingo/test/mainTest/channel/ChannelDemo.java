package com.bingo.test.mainTest.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author h-bingo
 * @date 2023/07/28 19:50
 **/
public class ChannelDemo {

    private static int SIZE = 2;

    public static void main(String[] args) throws Exception {
        copyFile();
    }

    // channel 文件拷贝
    public static void copyFile() throws Exception {
        File file = new File("C:\\Development\\uploadFile\\channel.txt");
        File copyFile = new File("C:\\Development\\uploadFile\\channel_copy.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(copyFile);
        FileChannel copyChannel = fileOutputStream.getChannel();

        // 方式一
        // ByteBuffer buffer = ByteBuffer.allocate(SIZE);
        // while (channel.read(buffer) > 0) {
        //
        //     buffer.flip();
        //
        //     copyChannel.write(buffer);
        //
        //     buffer.clear();
        // }

        // 方式二
        // long l = copyChannel.transferFrom(channel, 0, channel.size());
        // System.out.println(l);

        // 方式二 1
        // int begin = 0;
        // while (copyChannel.transferFrom(channel, begin, SIZE) > 0) {
        //     begin += SIZE;
        // }

        // 方式三
        // long l = channel.transferTo(0, channel.size(), copyChannel);
        // System.out.println(l);
        // 方式三 1
        // int begin = 0;
        // while (channel.transferTo(begin, SIZE, copyChannel) > 0) {
        //     begin += SIZE;
        // }

        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void read() throws IOException, InterruptedException {
        File file = new File("C:\\Development\\uploadFile\\channel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(SIZE);

        while (channel.read(buffer) > 0) {
            String s = new String(buffer.array());
            buffer.clear();
            System.out.print(s);
        }

        fileInputStream.close();
    }

    public static void write() throws IOException {
        String str = "hello channel";

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Development\\uploadFile\\channel.txt");

        FileChannel channel = fileOutputStream.getChannel();

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        int count = bytes.length % SIZE == 0 ? bytes.length / SIZE : (bytes.length / SIZE) + 1;

        ByteBuffer buffer = ByteBuffer.allocate(SIZE);

        for (int i = 0; i < count; i++) {

            buffer.put(bytes, i * SIZE, (i + 1) * SIZE > bytes.length ? bytes.length - i * SIZE : SIZE);

            buffer.flip();

            channel.write(buffer);

            buffer.clear();
        }

        fileOutputStream.close();
    }
}
