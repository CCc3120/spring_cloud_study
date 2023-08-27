package com.bingo.test.designMode.abs.chain.dynamic;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:54
 * @Version 1.0
 */
public interface HandlerDao {

    ChainEntity getChainEntity(Integer handlerId);

    ChainEntity getFirst();
}
