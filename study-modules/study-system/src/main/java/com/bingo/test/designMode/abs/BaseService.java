package com.bingo.test.designMode.abs;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:09
 * @Version 1.0
 */
public abstract class BaseService<Request, Response> {

    public final Response service(Request request) {
        System.out.println("调用公共service");

        // 该方法又子类实现
        validateRequest(request);

        System.out.println("结束调用公共service");

        return null;
    }

    protected abstract void validateRequest(Request request);
}
