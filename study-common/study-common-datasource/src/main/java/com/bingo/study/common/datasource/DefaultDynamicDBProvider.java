package com.bingo.study.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.datasource.properties.DynamicDBProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载配置文件中所有数据源
 *
 * @Author h-bingo
 * @Date 2023-04-26 17:45
 * @Version 1.0
 */
@Slf4j
@EnableConfigurationProperties(DynamicDBProperties.class)
@ConditionalOnMissingBean({DefaultDynamicDBProvider.class})
public class DefaultDynamicDBProvider implements DynamicDBProvider {

    @Autowired
    private DynamicDBProperties dynamicDBProperties;

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSource> dataSourcesMap = new HashMap<>();

        Map<String, Map<String, String>> ds = dynamicDBProperties.getDs();
        for (Map.Entry<String, Map<String, String>> stringMapEntry : ds.entrySet()) {
            try {
                dataSourcesMap.put(stringMapEntry.getKey(),
                        dynamicDBProperties.dataSource((DruidDataSource) DruidDataSourceFactory.createDataSource(stringMapEntry.getValue())));
            } catch (Exception e) {
                log.warn("数据源创建异常，Name: {}，params: {}", stringMapEntry.getKey(),
                        JsonMapper.getInstance().toJsonString(stringMapEntry.getValue()), e);
            }
        }

        return dataSourcesMap;
    }
}
