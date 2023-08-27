package com.bingo.test.designMode.abs.chain.dynamic;

import com.bingo.test.designMode.abs.chain.AbstractHandler;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:57
 * @Version 1.0
 */
public class HandlerFactory {

    public static AbstractHandler getFirst() {
        HandlerImpl handler = new HandlerImpl();

        ChainEntity first = handler.getFirst();

        AbstractHandler abstractHandler = newAbstractHandler(first);

        if (abstractHandler == null) {
            return null;
        }

        ChainEntity tmp = first;

        AbstractHandler tmph = abstractHandler;

        while (tmp.getNextHandlerId() != null) {
            ChainEntity chainEntity = handler.getChainEntity(tmp.getNextHandlerId());
            AbstractHandler abstractHandler1 = newAbstractHandler(chainEntity);
            tmph.setNext(abstractHandler1);

            tmp = chainEntity;
            tmph = abstractHandler1;
        }

        return abstractHandler;
    }


    public static AbstractHandler newAbstractHandler(ChainEntity chain) {
        String conference = chain.getConference();
        try {
            AbstractHandler abstractHandler = (AbstractHandler) Class.forName(conference).newInstance();
            return abstractHandler;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        AbstractHandler first = getFirst();
        first.handler();
    }
}
