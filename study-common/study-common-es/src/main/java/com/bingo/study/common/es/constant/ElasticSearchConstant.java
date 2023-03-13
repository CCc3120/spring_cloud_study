package com.bingo.study.common.es.constant;


import com.bingo.study.common.core.utils.StringUtil;

/**
 * es 索引和type配置
 */
public interface ElasticSearchConstant {

    int SUCCESS_CODE = 200;

    String KEYWORD = StringUtil.SEPARATOR_POINT + "keyword";

    String INDEX_STUDENT = "bntang";

    String INDEX_TEACHER = "index_teacher";

    String TYPE_STUDENT = "student";
}
