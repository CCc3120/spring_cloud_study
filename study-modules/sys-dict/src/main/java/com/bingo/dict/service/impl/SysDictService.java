package com.bingo.dict.service.impl;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import com.bingo.dict.service.ISysDictService;
import com.bingo.study.common.component.dict.service.impl.AbstractDictService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:24
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictService.class)
public class SysDictService extends AbstractDictService<SysDictCategory, SysDictData> implements ISysDictService {

}
