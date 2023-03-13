package com.bingo.study.common.component.retry;

public interface RetryService {

    Object exec() throws Throwable;
}
