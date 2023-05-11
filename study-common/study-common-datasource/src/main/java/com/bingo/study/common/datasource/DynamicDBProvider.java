package com.bingo.study.common.datasource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-04-26 17:43
 * @Version 1.0
 */
public interface DynamicDBProvider {
    /**
     * 加载所有的数据源
     *
     * @return
     */
    Map<String, DataSource> loadDataSources();
}
