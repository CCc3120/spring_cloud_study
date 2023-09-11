package com.bingo.study.common.es.service;


import com.bingo.study.common.core.interfaces.IBaseModel;
import com.bingo.study.common.core.web.page.PageResult;
import com.bingo.study.common.core.utils.FieldUtil;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.utils.SFunction;
import com.bingo.study.common.core.utils.StringUtil;
import com.bingo.study.common.core.web.request.BaseSearchModel;
import com.bingo.study.common.es.constant.ElasticSearchConstant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es7.0以下版本，不允许一个index下有多个type
 */
@Slf4j
@Service
@ConditionalOnMissingBean(ElasticSearchServiceImpl.class)
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean createIndex(String index) throws Exception {
        // 判断索引是否存在
        if (this.existIndex(index)) {
            return true;
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest,
                RequestOptions.DEFAULT);
        log.info("ES创建索引[{}]结果：[{}]", index, createIndexResponse.isAcknowledged());
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean existIndex(String index) throws Exception {
        // 判断索引是否存在
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        log.info("ES索引[{}],存在结果[{}]", index, exists);
        return exists;
    }

    @Override
    public boolean deleteIndex(String index) throws Exception {
        // 判断索引是否存在
        if (!this.existIndex(index)) {
            return true;
        }
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest,
                RequestOptions.DEFAULT);
        log.info("ES删除索引[{}]结果：[{}]", index, acknowledgedResponse.isAcknowledged());
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public boolean addDocument(String index, String type, IBaseModel model) throws Exception {
        if (!this.createIndex(index)) {
            return false;
        }

        String jsonString = JsonMapper.getInstance().toJsonString(model);

        IndexRequest indexRequest = new IndexRequest()
                .index(index)
                // es 7 删除了type
                // .type(type)
                .id(model.getFdId())
                // 设置超时时间
                .timeout(TimeValue.timeValueSeconds(2))
                // 转换为json字符串
                .source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        boolean result = indexResponse.status().getStatus() == ElasticSearchConstant.SUCCESS_CODE;
        log.info("ES新增文档结果[{}],index[{}],type[{}],文档内容：{}", result, index, type, jsonString);
        return result;
    }

    @Override
    public boolean isExistsDocument(String index, String type, String id) throws Exception {
        // 判断是否存在文档
        GetRequest getRequest = new GetRequest()
                .index(index)
                // es 7 删除了type
                // .type(type)
                .id(id)
                // 不获取返回的_source的上下文
                .fetchSourceContext(new FetchSourceContext(false))
                .storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        log.info("ES文档存在结果[{}],index[{}],type[{}],id[{}]", exists, index, type, id);
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public <T> T getDocument(String index, String type, String id, Class<T> tClass) throws Exception {
        // 获取文档
        GetRequest getRequest = new GetRequest()
                .index(index)
                // es 7 删除了type
                // .type(type)
                .id(id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = getResponse.getSourceAsString();
        log.info("ES文档查询index[{}],type[{}],id[{}],结果:{}", index, type, id, sourceAsString);
        return JsonMapper.getInstance().fromJson(sourceAsString, tClass);
    }

    @Override
    public boolean updateDocument(String index, String type, IBaseModel model) throws Exception {
        String jsonString = JsonMapper.getInstance().toJsonString(model);
        // 更新文档
        UpdateRequest updateRequest = new UpdateRequest()
                .index(index)
                // es 7 删除了type
                // .type(type)
                .id(model.getFdId())
                .timeout(TimeValue.timeValueSeconds(1))
                .doc(jsonString, XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        boolean result = updateResponse.status().getStatus() == ElasticSearchConstant.SUCCESS_CODE;
        log.info("ES文档更新结果[{}],index[{}],type[{}],文档内容：{}", result, index, type, jsonString);
        return result;
    }

    @Override
    public boolean deleteDocument(String index, String type, String id) throws Exception {
        if (!this.isExistsDocument(index, type, id)) {
            return true;
        }

        // 删除文档
        DeleteRequest deleteRequest = new DeleteRequest()
                .index(index)
                // es 7 删除了type
                // .type(type)
                .id(id)
                .timeout(TimeValue.timeValueSeconds(1));
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        boolean result = deleteResponse.status().getStatus() == ElasticSearchConstant.SUCCESS_CODE;
        log.info("ES删除文档index[{}],type[{}],id[{}],结果：[{}]", index, type, id, result);
        return result;
    }

    @Override
    public boolean bulkRequest(String index, List<IBaseModel> contents) throws Exception {
        // 批量插入
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(1));
        contents.forEach(model ->
                bulkRequest.add(
                        new IndexRequest(index)
                                .id(model.getFdId())
                                .source(JsonMapper.getInstance().toJsonString(model), XContentType.JSON)
                )
        );
        BulkResponse bulkItemResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkItemResponse.hasFailures();
    }

    @Override
    public <T> PageResult<T> searchRequest(Class<T> clazz, String index, BaseSearchModel model,
            ElasticSearchCondition condition) throws Exception {
        // 搜索请求
        SearchRequest searchRequest;
        if (StringUtil.isNull(index)) {
            searchRequest = new SearchRequest();
        } else {
            searchRequest = new SearchRequest(index);
        }
        // 条件构造
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 创建条件构造器
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 自己实现条件构造
        condition.build(builder);

        // 设置查询条件
        searchSourceBuilder.query(builder);

        // 分页信息
        long from = (model.getPageNum() - 1) * model.getPageSize();
        searchSourceBuilder.from((int) from).size(model.getPageSize().intValue());

        // 排序
        if (StringUtil.isNotNull(model.getOrderField())) {
            searchSourceBuilder.sort(StringUtil.join(model.getOrderField(), ElasticSearchConstant.KEYWORD),
                    SortOrder.fromString(model.getOrderType()));
        }

        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //  总数，分页查询可用
        long totalHits = searchResponse.getHits().getTotalHits().value;
        List<T> data = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            data.add(JsonMapper.getInstance().fromJson(searchHit.getSourceAsString(), clazz));
        }
        return PageResult.of(data, totalHits, model);
    }

    @Override
    public <T, R> List<Map<String, Object>> searchRequest(String index, String keyword, int page, int size,
            boolean isHighlight, SFunction<T, R>... fn) throws Exception {
        String[] fieldNames = new String[fn.length];
        for (int i = 0; i < fn.length; i++) {
            fieldNames[i] = FieldUtil.convertToFieldName(fn[i]);
        }

        // 搜索请求
        SearchRequest searchRequest;
        if (StringUtil.isNull(index)) {
            searchRequest = new SearchRequest();
        } else {
            searchRequest = new SearchRequest(index);
        }
        // 条件构造
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 第几页
        searchSourceBuilder.from(page);
        // 每页多少条数据
        searchSourceBuilder.size(size);
        // 配置高亮
        if (isHighlight) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            for (String fieldName : fieldNames) {
                highlightBuilder.field(fieldName);
            }
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        // String str = "if(doc['fdCreateTime.keyword'].size()>0) {\n" +
        //         "        return doc['fdCreateTime.keyword'][0];\n" +
        //         "      } else { \n" +
        //         "        return '1970-01-01 00:00:00';\n" +
        //         "      }";
        // ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(new Script(str),
        //         ScriptSortBuilder.ScriptSortType.STRING).order(SortOrder.ASC);
        // searchSourceBuilder.sort(scriptSortBuilder);

        // 精确查询
        // QueryBuilders.termQuery();
        // 匹配所有
        // QueryBuilders.matchAllQuery();
        // 最细粒度划分：ik_max_word，最粗粒度划分：ik_smart
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fieldNames).analyzer("ik_max_word"));
        // searchSourceBuilder.query(QueryBuilders.matchQuery("content", keyWord));
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            // 原来的结果
            Map<String, Object> sourceMap = searchHit.getSourceAsMap();

            // 解析高亮字段，替换掉原来的字段
            if (isHighlight) {
                Map<String, HighlightField> highlightFieldMap = searchHit.getHighlightFields();
                for (String s : fieldNames) {
                    HighlightField highlightField = highlightFieldMap.get(s);
                    if (highlightField != null) {
                        Text[] fragments = highlightField.getFragments();
                        StringBuilder n_highlightField = new StringBuilder();
                        for (Text text : fragments) {
                            n_highlightField.append(text);
                        }
                        sourceMap.put(s, n_highlightField.toString());
                    }
                }
            }
            results.add(sourceMap);
        }
        return results;
    }

    @Override
    public List<String> searchAllRequest(String index) throws Exception {
        // 搜索请求
        SearchRequest searchRequest;
        if (StringUtil.isNull(index)) {
            searchRequest = new SearchRequest();
        } else {
            searchRequest = new SearchRequest(index);
        }
        // 条件构造
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 第几页
        searchSourceBuilder.from(0);
        // 每页多少条数据
        searchSourceBuilder.size(1000);
        // 匹配所有
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<String> results = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            results.add(searchHit.getId());
        }
        return results;
    }
}
