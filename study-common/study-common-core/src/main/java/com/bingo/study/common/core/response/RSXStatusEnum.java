package com.bingo.study.common.core.response;

import com.bingo.study.common.core.enums.CodeDescEnum;

/**
 * @Author h-bingo
 * @Date 2023-08-18 16:01
 * @Version 1.0
 */
public enum RSXStatusEnum implements CodeDescEnum<String> {

    SUCCESS_STATUS("SUCCESS", "操作成功"),

    FAIL_STATUS("FAIL", "操作失败"),

    ERROR_STATUS("ERROR", "系统错误"),
    ;

    private final String code;

    private final String desc;

    RSXStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
