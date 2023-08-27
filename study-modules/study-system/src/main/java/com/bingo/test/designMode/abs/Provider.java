package com.bingo.test.designMode.abs;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:21
 * @Version 1.0
 */
public class Provider extends AbstractProvider {

    // 获取请求
    public void get(final String key) {
        executeCommand(key, () -> System.out.println("执行了获取指令, key：" + key), null);
    }

    // 判断是否存在
    public void exists(final String key) {
        executeCommand(key, () -> System.out.println("执行了判断是否存在指令, key：" + key), null);
    }

    public static void main(String[] args) {
        Provider provider = new Provider();
        provider.get("ss");
        provider.exists("sss");
    }
}
