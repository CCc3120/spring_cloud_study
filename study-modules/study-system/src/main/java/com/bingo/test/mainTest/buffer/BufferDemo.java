package com.bingo.test.mainTest.buffer;

import java.nio.ByteBuffer;

/**
 * @author h-bingo
 * @date 2023/07/22 18:22
 **/
public class BufferDemo {
    public static void main(String[] args) {

    }

    public static void sort() {
        ByteBuffer buffer = ByteBuffer.allocate(128);

        buffer.putInt(100);
        buffer.putShort((short) 1);
        buffer.putChar('黄');
        buffer.putLong(3L);

        // 写转换读
        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());

        // 只读
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(buffer.isReadOnly());
        System.out.println(readOnlyBuffer.isReadOnly());
    }
}