package com.bingo.test.translate;

import com.bingo.study.common.core.enums.CodeDescEnum;

/**
 * @Author h-bingo
 * @Date 2023-04-24 15:38
 * @Version 1.0
 */
public enum StatusEnum implements CodeDescEnum<Integer> {
    one(1, "xx"),
    one_(2, "ss"),
    ;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
