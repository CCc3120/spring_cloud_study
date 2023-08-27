package com.bingo.test.designMode.abs.chain.dynamic;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:49
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class ChainEntity {

    private String name;

    private String conference;

    private Integer handlerId;

    private Integer preHandlerId;

    private Integer nextHandlerId;
}
