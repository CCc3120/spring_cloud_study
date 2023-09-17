package com.bingo.test.mainTest.juc.demo2;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author h-bingo
 * @date 2023/09/17 13:50
 **/
@Data
@Accessors(chain = true)
public class Student {

    private String name;

    private String sex;
}
