package com.bingo.dict.model;

import com.bingo.study.common.core.dict.IDictDataModel;
import com.bingo.study.common.core.model.BaseModel;

/**
 * 字典数据
 *
 * @Author h-bingo
 * @Date 2023-08-17 13:56
 * @Version 1.0
 */
public class SysDictData extends BaseModel implements IDictDataModel {

    private String fdName;

    private String fdCode;

    private String fdType;

    @Override
    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    @Override
    public String getFdCode() {
        return fdCode;
    }

    public void setFdCode(String fdCode) {
        this.fdCode = fdCode;
    }

    @Override
    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }
}
