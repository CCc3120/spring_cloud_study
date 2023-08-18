package com.bingo.dict.cache;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import com.bingo.study.common.component.dict.service.impl.AbstractDictRedisCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:20
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictCacheService.class)
public class SysDictCacheService extends AbstractDictRedisCacheService<SysDictCategory, SysDictData> {
    @Override
    protected String getDictKeyPrefix() {
        return "sys-dict";
    }
}
