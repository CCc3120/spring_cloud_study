package com.bingo.study.common.core.dict;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 字典翻译接口
 *
 * @Author h-bingo
 * @Date 2023-06-13 09:32
 * @Version 1.0
 */
public interface IDictTranslateService {

    /**
     * 查询字典
     *
     * @Param [code, type]
     * @Return java.util.Optional<com.bingo.study.common.core.dict.IDictBase>
     * @Date 2023-06-13 09:37
     */
    Optional<IDictTranslateModel> getDictOpt(String code, String type);

    /**
     * 字典翻译
     *
     * @Param [code, type, consumer]
     * @Return void
     * @Date 2023-06-13 09:38
     */
    void dictTran(String code, String type, Consumer<IDictTranslateModel> consumer);
}
