package com.bingo.study.common.component.limiter;

import com.bingo.study.common.core.enums.DescEnum;

/**
 * @Author h-bingo
 * @Date 2023-04-26 09:28
 * @Version 1.0
 */
public enum LimitType implements DescEnum {

    DEFAULT("全局默认"),
    USER("根据用户限流"),
    IP("根据IP限流"),


    ;

    private String desc;

    LimitType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
