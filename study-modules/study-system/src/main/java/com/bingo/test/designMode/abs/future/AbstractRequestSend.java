package com.bingo.test.designMode.abs.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:23
 * @Version 1.0
 */
public abstract class AbstractRequestSend {
    /**
     * 模拟异步执行任务
     */
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RequestFuture send(String s) {
        // 这里是Future模式
        RequestFuture requestFuture = new RequestFuture();
        executorService.execute(new AsyncExeRemoteInvoke(requestFuture, s));
        return requestFuture;
    }

    private static class AsyncExeRemoteInvoke implements Runnable {
        private RequestFuture requestFuture;
        private String s;

        public AsyncExeRemoteInvoke(RequestFuture requestFuture, String s) {
            this.requestFuture = requestFuture;
            this.s = s;
        }

        @Override
        public void run() {
            // 模拟远程调用耗时
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String res = s + "远程调用结束";
            requestFuture.onComplete(res);
        }
    }
}
