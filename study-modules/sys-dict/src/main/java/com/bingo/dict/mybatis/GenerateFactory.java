package com.bingo.dict.mybatis;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateFactory {

    public static void main(String[] args) {
        String path = "com.bingo.dict";

        Config build = Config.Builder.getDefaule()
                .clazzs(SysDictCategory.class, SysDictData.class)
                .xmlPackage(path + ".dao.xml")
                .mapperPackage(path + ".dao")
                .hasMethod(false)
                .build();

        GenerateMybatis mybatis = new GenerateMybatis(build);
        mybatis.init().buildContent().writeToFile();

        // createTableDDL(OrderInfo.class);


    }

    public static void createTableDDL(Class<?> clazz) {
        /* 制表符 */
        String TABLE_SEPARATOR = "\t";

        Field pkField = null;
        List<Field> fieldList = new ArrayList<>();


        Set<String> set = new HashSet<>();
        Class<?> cla = clazz;
        while (!cla.getSimpleName().equals("Object")) {
            for (Field field : cla.getDeclaredFields()) {
                if (set.contains(field.getName())) {
                    continue;
                }
                set.add(field.getName());

                if (checkFieldIsPk(field)) {
                    pkField = field;
                } else {
                    fieldList.add(field);
                }
            }

            cla = cla.getSuperclass();
        }

        // CREATE TABLE chat_msg_content
        // (
        //     `fd_id`          VARCHAR(32) NOT NULL COMMENT '主键',
        //     `fd_content`     TEXT COMMENT '聊天内容',
        //     `fd_create_time` DATETIME COMMENT '创建时间',
        //     PRIMARY KEY (fd_id)
        // -- 支持4字节集的字符，默认的utf8只能支持3字节的字符（表情符号4字节）
        // ) COMMENT = '聊天记录内容' charset = utf8mb4
        // 表描述
        ApiModel annotation = clazz.getAnnotation(ApiModel.class);
        ApiModelProperty pkFieldAnnotation = pkField.getAnnotation(ApiModelProperty.class);

        StringBuilder sb = new StringBuilder();
        sb
                .append("create table `").append(humpToUnderline(clazz.getSimpleName())).append("`")
                .append(System.lineSeparator())
                .append("(")
                .append(System.lineSeparator())
                .append(TABLE_SEPARATOR).append("`").append(humpToUnderline(pkField.getName())).append("` ")
                .append("int ").append("not null auto_increment comment '").append(pkFieldAnnotation.value())
                .append("',")
        ;

        for (Field field : fieldList) {
            ApiModelProperty fieldAnnotation = field.getAnnotation(ApiModelProperty.class);
            sb
                    .append(System.lineSeparator())
                    .append(TABLE_SEPARATOR).append("`").append(humpToUnderline(field.getName())).append("` ")
                    .append(buildFieldType(field)).append("not null comment '")
                    .append(fieldAnnotation.value()).append("',");
        }


        sb
                .append(System.lineSeparator())
                .append(") comment = '").append(annotation.value()).append("' ")
                .append("collate = 'utf8mb4_general_ci' engine = innodb;")
        ;
        System.out.println(sb);
    }

    private static String buildFieldType(Field field) {
        String simpleName = field.getType().getSimpleName();
        StringBuilder sb = new StringBuilder();
        if (simpleName.contains("Integer")) {
            sb.append("int ");
        } else if (simpleName.contains("Date")) {
            sb.append("datetime ");
        } else {
            sb.append("varchar() ");
        }
        return sb.toString();
    }

    public static boolean checkFieldIsPk(Field field) {
        // @Id
        Id pk = field.getAnnotation(Id.class);
        return pk != null;
    }

    private static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        // 偏移量，第i个下划线的位置是 当前的位置+ 偏移量（i-1）,第一个下划线偏移量是0
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                if (i != 0) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }
}
