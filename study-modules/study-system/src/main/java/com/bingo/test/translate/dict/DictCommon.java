package com.bingo.test.translate.dict;

import com.bingo.study.common.component.dict.service.IDictService;
import com.bingo.study.common.component.dict.service.impl.DictServiceImpl;
import org.springframework.stereotype.Component;

/**
 * @Author h-bingo
 * @Date 2023-08-11 10:42
 * @Version 1.0
 */
@Component
public class DictCommon extends DictServiceImpl<DictType, DictData> implements IDictService<DictType, DictData> {
}
