package com.bingo.study.common.es.service;


import com.bingo.study.common.core.interfaces.IBaseModel;
import com.bingo.study.common.core.web.request.BaseSearchModel;
import com.bingo.study.common.core.web.page.PageResult;
import com.bingo.study.common.core.utils.SFunction;

import java.util.List;
import java.util.Map;

/**
 * es 7.4 中，可以移除 type 参数
 */
public interface ElasticSearchService {

    /**
     * 创建索引
     *
     * @param index
     */
    boolean createIndex(String index) throws Exception;

    /**
     * 判断索引是否存在
     *
     * @param index
     */
    boolean existIndex(String index) throws Exception;

    /**
     * 删除索引
     *
     * @param index
     */
    boolean deleteIndex(String index) throws Exception;

    /**
     * 新增文档
     *
     * @param index
     * @param model
     */
    boolean addDocument(String index, String type, IBaseModel model) throws Exception;

    /**
     * 判断是否存在文档
     *
     * @param index
     * @param type
     * @param id
     */
    boolean isExistsDocument(String index, String type, String id) throws Exception;

    /**
     * 获取文档
     *
     * @param index
     * @param type
     * @param id
     * @param tClass
     */
    <T> T getDocument(String index, String type, String id, Class<T> tClass) throws Exception;

    /**
     * 更新文档
     *
     * @param index
     * @param type
     * @param model
     */
    boolean updateDocument(String index, String type, IBaseModel model) throws Exception;

    /**
     * 删除文档
     *
     * @param index
     * @param type
     * @param id
     */
    boolean deleteDocument(String index, String type, String id) throws Exception;

    /**
     * 批量插入
     *
     * @param index
     * @param contents
     */
    boolean bulkRequest(String index, List<IBaseModel> contents) throws Exception;

    /**
     * @MethodName searchRequest
     * @Description 简单的分页查询
     * @Param [clazz, index, model, condition]
     * @Return com.bingo.spring_bingo.system.core.util.page.PageResult<T>
     * @Date 2022-12-13 15:11
     */
    <T> PageResult<T> searchRequest(Class<T> clazz, String index, BaseSearchModel model,
            ElasticSearchCondition condition) throws Exception;

    /**
     * 搜索请求
     *
     * @param index       索引
     * @param keyword     搜索关键字
     * @param page        第多少页
     * @param size        页大小
     * @param isHighlight 是否高亮
     * @param fn          搜索字段引用
     */
    <T, R> List<Map<String, Object>> searchRequest(String index, String keyword, int page, int size,
            boolean isHighlight, SFunction<T, R>... fn) throws Exception;

    /**
     * 搜索所有id
     *
     * @param index
     */
    List<String> searchAllRequest(String index) throws Exception;
}
