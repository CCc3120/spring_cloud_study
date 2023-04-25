package com.bingo.test.translate;

import com.bingo.study.common.core.enums.CodeDescEnum;

/**
 * @Author h-bingo
 * @Date 2023-04-24 15:36
 * @Version 1.0
 */
public enum SexEnum implements CodeDescEnum<String> {
    MAN("1", "男"),
    MAN_("2", "女"),
    ;


    SexEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
