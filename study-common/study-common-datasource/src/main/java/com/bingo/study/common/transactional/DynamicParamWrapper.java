package com.bingo.study.common.transactional;

import lombok.Data;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author h-bingo
 * @Date 2023-04-28 15:49
 * @Version 1.0
 */
@Data
public class DynamicParamWrapper {

    /**
     * 事物id
     */
    private String transactionId;
    /**
     * 事物隔离级别
     */
    private Isolation isolation;
    /**
     * 是否只读
     */
    private boolean readOnly;
    /**
     * 传播机制
     */
    private Propagation propagation;
}
