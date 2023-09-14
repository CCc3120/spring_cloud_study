package com.bingo.test.translate;

import com.bingo.study.common.component.dict.translate.constant.TranslateType;
import com.bingo.study.common.component.dict.translate.util.TranslateUtil;
import com.bingo.study.common.component.dict.translate.wrapper.TranslateFieldWrapper;
import com.bingo.study.common.component.responseFieldHandler.IgnoreField;
import com.bingo.study.common.core.dict.IDictDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        System.out.println(list);

        // TranslateUtil.translate(student, null);
        // System.out.println(System.currentTimeMillis() - time);

        // TranslateUtil.translate(student1, null);
        TranslateUtil.translate(list);
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(list);

        // DictCommon dictCommon = new DictCommon();

        // TranslateUtil.translate(student, dictCommon);

        // Consumer
        // Supplier
        // translate(student, new BiFunction<String, String, Consumer<IDictDataModel>>() {
        //     @Override
        //     public Consumer<IDictDataModel> apply(String s, String s2) {
        //
        //         return new Consumer<IDictDataModel>() {
        //             @Override
        //             public void accept(IDictDataModel iDictDataModel) {
        //
        //             }
        //         };
        //     }
        // });
    }

    public static Void get() throws InstantiationException, IllegalAccessException {
        return Void.TYPE.newInstance();
    }

    public static <T> void translate(T t, BiFunction<String, String, Function<IDictDataModel, Object>> f) {
        List<TranslateFieldWrapper> list = new ArrayList<>();


        for (TranslateFieldWrapper fieldWrapper : list) {
            if (fieldWrapper.getTranslateType() == TranslateType.ENUM) {
                // 枚举翻译

            } else if (fieldWrapper.getTranslateType() == TranslateType.DICT && f != null) {
                // 字典翻译
                // f.apply((String) ReflectUtil.getFieldValue(t, fieldWrapper.getField()), fieldWrapper.getDictType())
                //         .andThen(iDictDataModel -> ReflectUtil.setFieldValue(t, fieldWrapper.getFullField(), iDictDataModel.getName()));



                // f.accept((String) ReflectUtil.getFieldValue(t, fieldWrapper.getField()), fieldWrapper.getDictType());
                // f.andThen(new BiConsumer<String, String>() {
                //     @Override
                //     public void accept(String s, String s2) {
                //
                //     }
                // }).accept();

                // f.apply((String) ReflectUtil.getFieldValue(t, fieldWrapper.getField()), fieldWrapper.getDictType())
                //         .ifPresent(dictDataModel -> ReflectUtil.setFieldValue(t, fieldWrapper.getFullField(), dictDataModel.getName()));
            }
        }
    }
}
