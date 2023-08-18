package com.bingo.dict.service.impl;

import com.bingo.dict.model.SysDictData;
import com.bingo.dict.service.ISysDictDataService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:28
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictDataServiceImpl.class)
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Override
    public List<SysDictData> getSysDictData(String type) {
        return null;
    }

    @Override
    public SysDictData getSysDictData(String code, String type) {
        return null;
    }
}
