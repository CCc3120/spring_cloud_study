package com.bingo.study.common.transactional;

/**
 * @Author h-bingo
 * @Date 2023-05-11 16:55
 * @Version 1.0
 */
public interface DynamicTransactionManager {

    void begin();

    void begin(DynamicParamWrapper wrapper);

    void commit() throws Exception;

    void commit(DynamicParamWrapper wrapper) throws Exception;

    void rollBack() throws Exception;

    void rollBack(DynamicParamWrapper wrapper) throws Exception;

    void close();

    void close(DynamicParamWrapper wrapper);
}
