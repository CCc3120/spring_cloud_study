package com.bingo.dict.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bingo.dict.dao.SysDictDataMapper;
import com.bingo.dict.model.SysDictData;
import com.bingo.dict.service.ISysDictDataService;
import com.bingo.study.common.component.dict.service.IDictDataDbService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:28
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictDataServiceImpl.class)
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData>
        implements ISysDictDataService, IDictDataDbService<SysDictData> {

    @Override
    public List<SysDictData> getDictDataFromDb(String type) {
        return this.list(
                this.lambdaQuery()
                        .eq(SysDictData::getFdType, type)
        );
    }

    @Override
    public SysDictData getDictDataFromDb(String code, String type) {
        return this.getOne(
                this.lambdaQuery()
                        .eq(SysDictData::getFdType, type)
                        .eq(SysDictData::getFdCode, code)
        );
    }
}
