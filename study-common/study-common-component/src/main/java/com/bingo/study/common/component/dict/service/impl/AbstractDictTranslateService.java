package com.bingo.study.common.component.dict.service.impl;

import com.bingo.study.common.component.dict.service.IDictTranslateService;
import com.bingo.study.common.core.dict.IDictDataModel;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author h-bingo
 * @Date 2023-08-14 13:55
 * @Version 1.0
 */
public abstract class AbstractDictTranslateService<D extends IDictDataModel> implements IDictTranslateService<D> {
    @Override
    public abstract D getDict(String code, String type);

    @Override
    public Optional<D> getDictOpt(String code, String type) {
        return Optional.ofNullable(this.getDict(code, type));
    }

    @Override
    public void dictTran(String code, String type, Consumer<D> consumer) {
        this.getDictOpt(code, type).ifPresent(consumer);
    }
}
