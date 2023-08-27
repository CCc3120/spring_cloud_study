package com.bingo.test.designMode.abs;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:12
 * @Version 1.0
 */
public class TestService extends BaseService<String, String> {

    public Object getResp(String req) {
        return service(req);
    }

    @Override
    protected void validateRequest(String s) {
        System.out.println("Test service 的验证");
    }
}
