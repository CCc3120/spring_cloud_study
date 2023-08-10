package com.bingo.test.translate;

import com.bingo.study.common.component.responseBodyHandle.IgnoreField;
import com.bingo.study.common.component.translate.util.TranslateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-04-24 16:08
 * @Version 1.0
 */
public class TestMain {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();

        Student student = new Student();
        student.setIgnore(IgnoreField.CREATE_TIME.getCode());
        student.setSex(SexEnum.MAN.getCode());
        student.setStatus(StatusEnum.one.getCode());

        Student student1 = new Student();
        student1.setIgnore(IgnoreField.UPDATE_TIME.getCode());
        student1.setSex(SexEnum.MAN_.getCode());
        student1.setStatus(StatusEnum.one_.getCode());

        list.add(student);
        list.add(student1);
        long time = System.currentTimeMillis();
        // System.out.println(list);

        TranslateUtil.translate(student);
        System.out.println(System.currentTimeMillis() - time);

        TranslateUtil.translate(student1);
        // TranslateUtil.translate(list);
        System.out.println(System.currentTimeMillis() - time);
        // System.out.println(list);
    }
}
