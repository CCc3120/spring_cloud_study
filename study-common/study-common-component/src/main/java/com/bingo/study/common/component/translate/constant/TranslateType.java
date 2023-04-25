package com.bingo.study.common.component.translate.constant;

import com.bingo.study.common.core.enums.DescEnum;

/**
 * 翻译类型
 *
 * @Author h-bingo
 * @Date 2023-04-24 13:58
 * @Version 1.0
 */
public enum TranslateType implements DescEnum {

    DICT("数据字典"),

    ENUM("枚举类"),

    ;

    private String desc;

    TranslateType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
