package com.bingo.test.mainTest.netty.rpc.provider;

import com.bingo.test.mainTest.netty.rpc.netty.NettyServer;

/**
 * 启动服务提供者
 *
 * @author h-bingo
 * @date 2023/09/14 19:12
 **/
public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 8088);
    }
}
