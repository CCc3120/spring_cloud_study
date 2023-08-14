package com.bingo.test.translate.dict;

import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.component.dict.service.impl.AbstractDictRedisCacheService;
import org.springframework.stereotype.Component;

/**
 * @Author h-bingo
 * @Date 2023-08-11 10:40
 * @Version 1.0
 */
@Component
public class DictCommonCache extends AbstractDictRedisCacheService<DictType, DictData>
        implements IDictCacheService<DictType, DictData> {

    @Override
    protected String getDictKeyPrefix() {
        return "common";
    }
}
