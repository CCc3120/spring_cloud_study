package com.bingo.test.designMode.abs.chain.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:55
 * @Version 1.0
 */
public class HandlerImpl implements HandlerDao {

    private static Map<Integer, ChainEntity> map = new HashMap<>();

    static {
        ChainEnum[] values = ChainEnum.values();
        for (ChainEnum value : values) {
            ChainEntity chain = value.getChain();
            map.put(chain.getHandlerId(), chain);
        }
    }

    @Override
    public ChainEntity getChainEntity(Integer handlerId) {
        return map.get(handlerId);
    }

    @Override
    public ChainEntity getFirst() {
        Set<Map.Entry<Integer, ChainEntity>> entries = map.entrySet();
        for (Map.Entry<Integer, ChainEntity> entry : entries) {
            if (entry.getValue().getPreHandlerId() == null){
                return entry.getValue();
            }
        }
        return null;
    }
}
