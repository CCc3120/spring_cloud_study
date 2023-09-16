package com.bingo.test.mainTest.netty.rpc.customer;

import com.bingo.test.mainTest.netty.rpc.common.RPCService;
import com.bingo.test.mainTest.netty.rpc.netty.NettyClient;

/**
 * @author h-bingo
 * @date 2023/09/16 11:31
 **/
public class ClientBootstrap {

    public static final String providerName = "RPCService#getFirstStringDemo#";

    public static void main(String[] args) {

        NettyClient nettyClient = new NettyClient();

        // 创建代理对象
        RPCService service = (RPCService) nettyClient.getBean(RPCService.class, providerName);

        // 通过代理对象调用服务提供这的方法
        String stringDemo = service.getFirstStringDemo("你好");

        System.out.println("client 收到: " + stringDemo);
    }
}
