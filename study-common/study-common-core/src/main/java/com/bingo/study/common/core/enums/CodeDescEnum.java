package com.bingo.study.common.core.enums;

import java.util.*;
import java.util.function.Consumer;

/**
 * @Author h-bingo
 * @Date 2023-04-21 17:37
 * @Version 1.0
 */
public interface CodeDescEnum<T> extends DescEnum, CodeEnum<T> {

    static <T> Optional<CodeDescEnum<T>> getEnumOpt(T code, CodeDescEnum<T>[] enums) {
        if (code == null || enums == null) {
            return Optional.empty();
        }

        for (CodeDescEnum<T> anEnum : enums) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
    }

    static <T> void enumTran(T code, CodeDescEnum<T>[] enums, Consumer<? super CodeDescEnum<T>> consumer) {
        getEnumOpt(code, enums).ifPresent(consumer);
    }

    static <T> List<Map<String, Object>> toListMap(CodeDescEnum<T>[] enums) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CodeDescEnum<T> anEnum : enums) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", anEnum.getCode());
            map.put("desc", anEnum.getDesc());
            list.add(map);
        }
        return list;
    }
}
