package com.bingo.test.mainTest.netty.tcppackage1;

import lombok.Data;

/**
 *  协议包
 *
 * @author h-bingo
 * @date 2023/09/09 10:51
 **/
@Data
public class MessageProtocol {

    private int len;

    private byte[] content;
}
