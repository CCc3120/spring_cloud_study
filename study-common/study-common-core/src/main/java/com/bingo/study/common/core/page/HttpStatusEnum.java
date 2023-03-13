package com.bingo.study.common.core.page;

/**
 * @author bingo
 * @date 2022-03-24 11:36
 */
public enum HttpStatusEnum {
    SUCCESS("200", "操作成功"),
    FAIL("201", "操作失败"),
    ERROR("500","系统错误");

    private String code;
    private String desc;

    HttpStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
