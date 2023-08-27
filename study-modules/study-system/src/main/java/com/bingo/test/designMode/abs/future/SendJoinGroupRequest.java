package com.bingo.test.designMode.abs.future;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:37
 * @Version 1.0
 */
public class SendJoinGroupRequest extends AbstractRequestSend implements ResponseHandleAdapter {
    @Override
    public void onSuccess(String s, RequestFuture future) {
        System.out.println("SendJoinGroupRequest执行结果: " + s);
        future.onComplete(s);
    }

    public RequestFuture sendJoinGroupRequest(String s) {
        return send(s).compose(this);
    }

    public static void main(String[] args) {
        SendJoinGroupRequest sendJoinGroupRequest = new SendJoinGroupRequest();
        RequestFuture future = sendJoinGroupRequest.sendJoinGroupRequest("测试发送JoinGroup检测请求");
        future.addListener(new RequestFutureListener() {
            @Override
            public void onSuccess(String s) {
                System.out.println("我是SendJoinGroupRequest传播出来的，请求为:" + s);
            }

            @Override
            public void onFail() {

            }
        });
        // 写SendHeartbeatRequest 类似的
       /* SendHeartbeatRequest sendHeartbeatRequest = new SendHeartbeatRequest();
        sendHeartbeatRequest.sendHeartbeat("测试发送心跳检测请求");*/
    }
}
