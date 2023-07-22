package com.bingo.study.common.core.utils.validate;

import lombok.Getter;

/**
 * 验证类型
 *
 * @Author h-bingo
 * @Date 2023-04-13 17:11
 * @Version 1.0
 */
public enum ValidateType {

    NOT_NULL("%s[%s]不能为空"),
    NOT_BLANK("%s[%s]不能为空（且不能为空字符串）"),
    MIN_LENGTH("%s[%s]最小长度为%d"),
    MAX_LENGTH("%s[%s]最大长度超过%d"),
    REGEX("%s[%s]不满足匹配规则"),
    RANGE("%s[%s]不满足大小（长度）范围 %s ~ %s"),


    ;

    @Getter
    private String message;

    ValidateType(String message) {
        this.message = message;
    }
}
