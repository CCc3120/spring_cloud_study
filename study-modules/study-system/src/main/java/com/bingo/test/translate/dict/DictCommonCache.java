package com.bingo.test.translate.dict;

import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.component.dict.service.impl.DictCacheServiceImpl;
import org.springframework.stereotype.Component;

/**
 * @Author h-bingo
 * @Date 2023-08-11 10:40
 * @Version 1.0
 */
@Component
public class DictCommonCache extends DictCacheServiceImpl<DictType, DictData>
        implements IDictCacheService<DictType, DictData> {

    private final String dictType = "common";

    @Override
    protected String getDictCategoryKey() {
        return dictType;
    }

    @Override
    protected String getDictDataKey() {
        return dictType;
    }
}
