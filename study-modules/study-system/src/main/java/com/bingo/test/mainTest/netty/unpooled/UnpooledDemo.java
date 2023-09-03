package com.bingo.test.mainTest.netty.unpooled;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author h-bingo
 * @date 2023/09/02 15:22
 **/
public class UnpooledDemo {
    public static void main(String[] args) {
        // test();
        test2();
    }

    static void test() {
        // 创建一个bytebuf
        // 1.创建 对象, 该对象包含一个数组 arr , 是一个 byte[10]
        // 2.在 netty 的 ByteBuf 中, 不需要使 NIO byteBuffer 中的 flip (反转)
        //   底层维护了 readerIndex (读下标) 和 writeIndex (写下标)
        // 3.通过 readerIndex 和 writeIndex 和 capacity, 将 buf 分成了三个区
        // 0 --- readerIndex --- writeIndex --- capacity
        //  已经读取的区域    可读区         可写区
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        // 输出
        for (int i = 0; i < byteBuf.capacity(); i++) {
            // System.out.println(byteBuf.getByte(i)); // 读下标不变化
            System.out.println(byteBuf.readByte()); // 读下标会变化
        }
    }

    static void test2() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!加上中文", CharsetUtil.UTF_8);

        if (byteBuf.hasArray()) { //
            byte[] array = byteBuf.array();

            // 将数据转成字符转
            System.out.println(new String(array, CharsetUtil.UTF_8));

            System.out.println("byteBuf=" + byteBuf);

            System.out.println("偏移量: " + byteBuf.arrayOffset());

            System.out.println("读下标: " + byteBuf.readerIndex());

            System.out.println("写下标: " + byteBuf.writerIndex());

            System.out.println("容量: " + byteBuf.capacity());

            System.out.println("可读取字节数: " + byteBuf.readableBytes());

            // 注意 getByte(i) 不会改变可读字节数大小
            // System.out.println(byteBuf.readByte());

            System.out.println("可读取字节数: " + byteBuf.readableBytes());

            // for (int i = 0; i < byteBuf.readableBytes(); i++) {
            //     System.out.println((char) byteBuf.getByte(i));
            // }

            // 从 index 读 length 长度的字节内容
            CharSequence charSequence = byteBuf.getCharSequence(3, 3, CharsetUtil.UTF_8);
            System.out.println(charSequence);
        }
    }
}
