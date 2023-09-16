package com.bingo.test.mainTest.netty.rpc.provider;

import com.bingo.test.mainTest.netty.rpc.common.RPCService;

/**
 * @author h-bingo
 * @date 2023/09/14 19:08
 **/
public class RPCServerProvider implements RPCService {
    @Override
    public String getFirstStringDemo(String str) {
        System.out.println("收到客户端消息: " + str);
        if (str != null) {
            return "服务端已收到消息:" + str;
        } else {
            return "服务端已收到消息";
        }
    }
}
