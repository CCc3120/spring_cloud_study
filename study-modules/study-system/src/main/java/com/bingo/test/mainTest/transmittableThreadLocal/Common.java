package com.bingo.test.mainTest.transmittableThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @Author h-bingo
 * @Date 2023-09-20 16:33
 * @Version 1.0
 */
public class Common {
    static ThreadLocal<String> name = new ThreadLocal<>();
    static InheritableThreadLocal<String> key = new InheritableThreadLocal<>();
    static TransmittableThreadLocal<String> codec = new TransmittableThreadLocal<>();

    public static String getName() {
        return name.get();
    }

    public static String getKey() {
        return key.get();
    }

    public static String getCodec() {
        return codec.get();
    }

    public static void setName(String s) {
        name.set(s);
    }

    public static void setKey(String s) {
        key.set(s);
    }

    public static void setCodec(String s) {
        codec.set(s);
    }
}