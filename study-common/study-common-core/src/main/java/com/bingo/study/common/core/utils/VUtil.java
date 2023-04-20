package com.bingo.study.common.core.utils;


import com.bingo.study.common.core.utils.function.BooleanHandlerFunction;
import com.bingo.study.common.core.utils.function.PresentOrElseHandlerFunction;
import com.bingo.study.common.core.utils.function.ThrowExceptionFunction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @Author h-bingo
 * @Date 2023-04-13 13:43
 * @Version 1.0
 */
public class VUtil {

    // public static ThrowExceptionFunction isTrue(Supplier<Boolean> consumer) {
    //     Objects.requireNonNull(consumer);
    //     return (message) -> {
    //         if (consumer.get()) {
    //             throw new RuntimeException(message);
    //         }
    //     };
    // }
    //
    // public static Runnable isTrue(boolean bool) {
    //      return () -> {
    //          if(bool){
    //
    //          }
    //      };
    // }

    public static ThrowExceptionFunction isTrue(Supplier<Boolean> consumer) {
        Objects.requireNonNull(consumer);
        return (message) -> {
            if (consumer.get()) {
                throw new RuntimeException(message);
            }
        };
    }

    public static ThrowExceptionFunction isTrue(boolean bool) {
        return (message) -> {
            if (bool) {
                throw new RuntimeException(message);
            }
        };
    }

    public static BooleanHandlerFunction isTureOrFalse(Supplier<Boolean> consumer) {
        Objects.requireNonNull(consumer);
        return (trueHandler, falseHandler) -> {
            if (consumer.get()) {
                Objects.requireNonNull(trueHandler);
                trueHandler.run();
            } else {
                Objects.requireNonNull(falseHandler);
                falseHandler.run();
            }
        };
    }

    public static BooleanHandlerFunction isTureOrFalse(boolean bool) {
        return (trueHandler, falseHandler) -> {
            if (bool) {
                Objects.requireNonNull(trueHandler);
                trueHandler.run();
            } else {
                Objects.requireNonNull(falseHandler);
                falseHandler.run();
            }
        };
    }

    public static PresentOrElseHandlerFunction<String> isBlankOrNotBlank(String str) {
        return (action, emptyAction) -> {
            if (str == null || str.length() == 0) {
                Objects.requireNonNull(emptyAction);
                emptyAction.run();
            } else {
                Objects.requireNonNull(action);
                action.accept(str);
            }
        };
    }

    public static <T> PresentOrElseHandlerFunction<T> isNullOrNotNull(T t) {
        return (action, emptyAction) -> {
            if (t == null) {
                Objects.requireNonNull(emptyAction);
                emptyAction.run();
            } else {
                Objects.requireNonNull(action);
                action.accept(t);
            }
        };
    }


    public static void main(String[] args) {
        VUtil.isTrue(false).throwMessage("结果异常");
        VUtil.isTrue(() -> args == null).throwMessage("");

        VUtil.isTureOrFalse(true)
                .booleanHandler(() -> System.out.println("true"), () -> System.out.println("false"));

        VUtil.isBlankOrNotBlank("").presentOrElseHandler(s -> {

        }, () -> {

        });

        VUtil.isNullOrNotNull("").presentOrElseHandler(s -> {

        }, () -> {

        });

        Optional<?> optional = Optional.empty();

    }
}
