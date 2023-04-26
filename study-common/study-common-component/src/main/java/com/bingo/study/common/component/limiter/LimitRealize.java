package com.bingo.study.common.component.limiter;

import com.bingo.study.common.core.enums.DescEnum;

/**
 * @Author h-bingo
 * @Date 2023-04-26 11:22
 * @Version 1.0
 */
public enum LimitRealize implements DescEnum {
    TOKEN_BUCKET("令牌桶"),

    SLIDING_WINDOW("滑动窗口"),

    FIXED_WINDOW("固定窗口"),
    ;

    private String desc;

    LimitRealize(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
