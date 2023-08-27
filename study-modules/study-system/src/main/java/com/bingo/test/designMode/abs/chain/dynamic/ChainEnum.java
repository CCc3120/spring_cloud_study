package com.bingo.test.designMode.abs.chain.dynamic;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:49
 * @Version 1.0
 */
public enum ChainEnum {

    FIRST(new ChainEntity("第一关", "com.bingo.test.designMode.abs.chain.FirstHandler", 1, null, 2)),
    SECOND(new ChainEntity("第二关", "com.bingo.test.designMode.abs.chain.SecondHandler", 2, 1, 3)),
    THIRD(new ChainEntity("第三关", "com.bingo.test.designMode.abs.chain.ThirdHandler", 3, 2, null)),
    ;


    private ChainEntity chain;

    ChainEnum(ChainEntity chain) {
        this.chain = chain;
    }

    public ChainEntity getChain() {
        return chain;
    }
}
