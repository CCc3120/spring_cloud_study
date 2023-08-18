package com.bingo.dict.model;

import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.model.BaseModel;

/**
 * 字典类别
 *
 * @Author h-bingo
 * @Date 2023-08-17 13:57
 * @Version 1.0
 */

public class SysDictCategory extends BaseModel implements IDictCategoryModel {

    private String fdType;

    @Override
    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }
}
