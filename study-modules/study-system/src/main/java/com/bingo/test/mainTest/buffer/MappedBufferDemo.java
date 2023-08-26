package com.bingo.test.mainTest.buffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可以让文件直接在内存(堆外内存)修改,操作系统不需要拷贝一次
 *
 * @author h-bingo
 * @date 2023/07/29 21:47
 **/
public class MappedBufferDemo {
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Development\\uploadFile\\channel.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数一：读写模式
         * 参数二：修改的起始位置
         * 参数三：可以修改的内容大小（映射内存的大小）(单位字节)
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 3);

        mappedByteBuffer.put(0, (byte) 'A');
        mappedByteBuffer.put(1, (byte) 'B');
        mappedByteBuffer.put(2, (byte) 'C');

        // mappedByteBuffer.put((byte) 'A');
        // mappedByteBuffer.put((byte) 'B');
        // mappedByteBuffer.put((byte) 'C');
        // mappedByteBuffer.put((byte) 'D');

        randomAccessFile.close();
    }
}
