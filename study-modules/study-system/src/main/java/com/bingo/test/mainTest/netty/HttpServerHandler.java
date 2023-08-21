package com.bingo.test.mainTest.netty;

import com.bingo.study.common.core.web.page.AjaxResultFactory;
import com.bingo.study.common.core.utils.JsonMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author h-bingo
 * @Date 2023-07-20 17:41
 * @Version 1.0
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String requestData = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        String uri = fullHttpRequest.uri();
        HttpMethod httpMethod = fullHttpRequest.method();
        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);

        System.out.println(requestData);
        System.out.println(uri);
        System.out.println(httpMethod);
        System.out.println(keepAlive);

        String responseJson = JsonMapper.getInstance().toJsonString(AjaxResultFactory.success());
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseJson, CharsetUtil.UTF_8));   //  Unpooled.wrappedBuffer(responseJson)
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");       // HttpHeaderValues.TEXT_PLAIN.toString()
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        channelHandlerContext.writeAndFlush(response);
    }
}
