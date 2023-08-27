package com.bingo.test.designMode.abs.future;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:34
 * @Version 1.0
 */
public class RequestFuture {
    private List<RequestFutureListener> requestFutureListenerList;

    public RequestFuture() {
        this.requestFutureListenerList = new ArrayList<>();
    }

    public void onComplete(String s) {
        // 假设只执行成功的方法
        fireSuccess(s);
    }

    private void fireSuccess(String s) {
        // 假设只执行成功的方法
        requestFutureListenerList.forEach(requestFutureListener -> requestFutureListener.onSuccess(s));
    }

    public void addListener(RequestFutureListener requestFutureListener) {
        requestFutureListenerList.add(requestFutureListener);
    }


    public RequestFuture compose(ResponseHandleAdapter responseHandleAdapter) {
        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        final RequestFuture future = new RequestFuture();
        this.addListener(new RequestFutureListener() {
            @Override
            public void onSuccess(String s) {
                // 交回给各个请求处理类处理
                responseHandleAdapter.onSuccess(s, future);
            }

            @Override
            public void onFail() {
                // 暂时不处理
            }
        });
        return future;
    }
}
