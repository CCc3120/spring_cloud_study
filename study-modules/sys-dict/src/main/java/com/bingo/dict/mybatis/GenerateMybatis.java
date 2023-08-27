package com.bingo.dict.mybatis;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;

public class GenerateMybatis {

    /* 制表符 */
    private static final String TABLE_SEPARATOR = "\t";

    /* 逗号 */
    private static final String COMMA = ",";

    /* 小数点 */
    private static final String DOT = ".";

    /* 等于号 */
    private static final String EQ_SPE = "=";

    /* 参数格式 */
    private static final String PARAM_EG = "#{0}";

    /* mybaties mapper文件后缀 */
    private static final String XML_SUFFER = ".xml";

    /* java mapper接口后缀 */
    private static final String MAPPER_FILE_SUFFER = ".java";

    /* mapper名称后缀 */
    private static final String MAPPER_SUFFER = "Mapper";

    /* 格式：resultMap中property和column之间的间隔 */
    private static final int DEFAULT_NULL_CHAR_LENGTH = 8;

    /* 格式：空字符串 */
    private static final String NULL_CHAR = " ";

    /* 格式：换行符 */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /* 类的详细信息 */
    private final List<ClassDeatilInfo> classDeatilInfoList = new ArrayList<>();

    private final Config config;

    public GenerateMybatis(Config config) {
        this.config = config;
    }

    public GenerateMybatis init() {
        for (Class<?> aClass : config.getClazzs()) {
            classDeatilInfoList.add(new ClassDeatilInfo(config, aClass));
        }
        return this;
    }

    public GenerateMybatis buildContent() {
        for (ClassDeatilInfo classDeatilInfo : classDeatilInfoList) {
            build(classDeatilInfo);
        }
        return this;
    }

    private void build(ClassDeatilInfo deatilInfo) {
        buildXml(deatilInfo);
        buildMapper(deatilInfo);
    }

    private void buildXml(ClassDeatilInfo deatilInfo) {
        buildXmlHeader(deatilInfo);
        buildXmlCommon(deatilInfo);

        if (config.isHasMethod()) {
            buildXmlMethod(deatilInfo);
        }

        buildXmlEnd(deatilInfo);
    }

    private void buildXmlHeader(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" " +
                "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append("<mapper namespace=\"").append(deatilInfo.getMapperFullPath()).append("\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
    }

    private void buildXmlCommon(ClassDeatilInfo deatilInfo) {
        // resultMap
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR).append("<resultMap id=\"BaseResultMap\" type=\"")
                .append(deatilInfo.getClassFullPath()).append("\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("<id     property=\"").append(deatilInfo.getPkField().getName()).append("\"")
                .append(formatNullChar(deatilInfo, deatilInfo.getPkField()))
                .append("column=\"").append(humpToUnderline(deatilInfo.getPkField().getName())).append("\"/>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        for (Field field : deatilInfo.getFieldList()) {
            appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
            deatilInfo.getXmlBuffer().append("<result property=\"").append(field.getName()).append("\"")
                    .append(formatNullChar(deatilInfo, field))
                    .append("column=\"").append(humpToUnderline(field.getName())).append("\"/>");
            deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        }
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR).append("</resultMap>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);

        // sql
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR).append("<sql id=\"BaseSql\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        StringBuilder allColumn = new StringBuilder();
        allColumn.append("${mark}").append(DOT).append(humpToUnderline(deatilInfo.getPkField().getName()));
        for (Field field : deatilInfo.getFieldList()) {
            allColumn.append(COMMA).append(NULL_CHAR).append("${mark}").append(DOT).append(humpToUnderline(field.getName()));
        }
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append(allColumn);
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR).append("</sql>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildXmlMethod(ClassDeatilInfo deatilInfo) {
        buildXmlFind(deatilInfo);
        buildXmlInsert(deatilInfo);
        buildXmlUpdate(deatilInfo);
        buildXmlDelete(deatilInfo);
    }

    private void buildXmlFind(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("<select id=\"findByPrimaryKey\" parameterType=\"")
                .append(deatilInfo.getPkField().getType().getTypeName()).append("\"").append(NULL_CHAR)
                .append("resultMap=\"BaseResultMap\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("select");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("<include refid=\"BaseSql\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 3);
        deatilInfo.getXmlBuffer().append("<property name=\"mark\" value=\"").append(deatilInfo.getTableName()).append("\"/>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("</include>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("from").append(NULL_CHAR).append(deatilInfo.getTableName()).append(NULL_CHAR)
                .append("where").append(NULL_CHAR).append(deatilInfo.getTableName()).append(DOT)
                .append(humpToUnderline(deatilInfo.getPkField().getName())).append(NULL_CHAR).append(EQ_SPE).append(NULL_CHAR)
                .append(paramReplace(deatilInfo.getPkField().getName()));
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("</select>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildXmlInsert(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("<insert id=\"insert\" parameterType=\"").append(deatilInfo.getClassFullPath()).append("\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("insert into").append(NULL_CHAR).append(deatilInfo.getTableName());
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        buildXmlIf(deatilInfo.getXmlBuffer(), deatilInfo.getPkField(),
                stringJoin(humpToUnderline(deatilInfo.getPkField().getName()), COMMA), 3);
        for (Field field : deatilInfo.getFieldList()) {
            buildXmlIf(deatilInfo.getXmlBuffer(), field,
                    stringJoin(humpToUnderline(field.getName()), COMMA), 3);
        }
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("</trim>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("values");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        buildXmlIf(deatilInfo.getXmlBuffer(), deatilInfo.getPkField(),
                stringJoin(paramReplace(deatilInfo.getPkField().getName()), COMMA), 3);
        for (Field field : deatilInfo.getFieldList()) {
            buildXmlIf(deatilInfo.getXmlBuffer(), field,
                    stringJoin(paramReplace(field.getName()), COMMA), 3);
        }
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("</trim>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("</insert>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildXmlUpdate(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("<update id=\"update\" parameterType=\"").append(deatilInfo.getClassFullPath()).append("\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("update").append(NULL_CHAR).append(deatilInfo.getTableName());
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("<set>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        for (Field field : deatilInfo.getFieldList()) {
            buildXmlIf(deatilInfo.getXmlBuffer(), field,
                    stringJoin(humpToUnderline(field.getName()), NULL_CHAR, EQ_SPE, NULL_CHAR,
                            paramReplace(field.getName()), COMMA), 3);
        }
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("</set>");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("where").append(NULL_CHAR).append(humpToUnderline(deatilInfo.getPkField().getName()))
                .append(NULL_CHAR).append(EQ_SPE).append(NULL_CHAR).append(paramReplace(deatilInfo.getPkField().getName()));
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("</update>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildXmlDelete(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("<delete id=\"delete\" parameterType=\"").append(deatilInfo.getPkField().getType().getTypeName()).append("\">");
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        appendChar(deatilInfo.getXmlBuffer(), TABLE_SEPARATOR, 2);
        deatilInfo.getXmlBuffer().append("delete").append(NULL_CHAR).append("from").append(NULL_CHAR)
                .append(deatilInfo.getTableName()).append(NULL_CHAR).append("where").append(NULL_CHAR)
                .append(humpToUnderline(deatilInfo.getPkField().getName())).append(NULL_CHAR).append(EQ_SPE)
                .append(NULL_CHAR).append(paramReplace(deatilInfo.getPkField().getName()));
        deatilInfo.getXmlBuffer().append(LINE_SEPARATOR);
        deatilInfo.getXmlBuffer().append(TABLE_SEPARATOR)
                .append("</delete>");
        appendChar(deatilInfo.getXmlBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildXmlIf(StringBuffer buffer, Field field, String content, int format) {
        appendChar(buffer, TABLE_SEPARATOR, format);

        buffer.append("<if test=\"").append(field.getName()).append(NULL_CHAR).append("!= null");
        if (field.getType().getTypeName().equals(String.class.getTypeName())) {
            buffer.append(NULL_CHAR).append("and").append(NULL_CHAR).append(field.getName()).append(NULL_CHAR).append("!=''");
        }
        buffer.append("\">");
        buffer.append(LINE_SEPARATOR);

        appendChar(buffer, TABLE_SEPARATOR, format + 1);
        buffer.append(content);
        buffer.append(LINE_SEPARATOR);

        appendChar(buffer, TABLE_SEPARATOR, format);
        buffer.append("</if>");
        buffer.append(LINE_SEPARATOR);
    }

    private void appendChar(StringBuffer buffer, String content, int format) {
        for (int i = 0; i < format; i++) {
            buffer.append(content);
        }
    }

    private void buildXmlEnd(ClassDeatilInfo deatilInfo) {
        deatilInfo.getXmlBuffer().append("</mapper>");
    }

    private void buildMapper(ClassDeatilInfo deatilInfo) {
        buildMapperHeader(deatilInfo);
        if (config.isHasMethod()) {
            buildMapperMethod(deatilInfo);
        }
        buildMapperEnd(deatilInfo);
    }

    private void buildMapperHeader(ClassDeatilInfo deatilInfo) {
        deatilInfo.getMapperBuffer().append("package").append(NULL_CHAR).append(deatilInfo.getMapperPath()).append(";");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);
        deatilInfo.getMapperBuffer().append("import").append(NULL_CHAR).append("com.baomidou.mybatisplus.core.mapper.BaseMapper")
                .append(";");
        deatilInfo.getMapperBuffer().append(LINE_SEPARATOR);
        deatilInfo.getMapperBuffer().append("import").append(NULL_CHAR).append(deatilInfo.getClassFullPath())
                .append(";");
        deatilInfo.getMapperBuffer().append(LINE_SEPARATOR);
        deatilInfo.getMapperBuffer().append("import").append(NULL_CHAR).append("org.apache.ibatis.annotations.Mapper")
                .append(";");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);
        deatilInfo.getMapperBuffer().append("@Mapper");
        deatilInfo.getMapperBuffer().append(LINE_SEPARATOR);
        deatilInfo.getMapperBuffer().append("public").append(NULL_CHAR).append("interface").append(NULL_CHAR)
                .append(deatilInfo.getMapperName()).append(NULL_CHAR).append("extends BaseMapper<")
                .append(deatilInfo.getClassName()).append(">").append(NULL_CHAR).append("{");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildMapperMethod(ClassDeatilInfo deatilInfo) {
        // find
        deatilInfo.getMapperBuffer().append(TABLE_SEPARATOR)
                .append(deatilInfo.getClassName()).append(NULL_CHAR).append("findByPrimaryKey(")
                .append(deatilInfo.getPkField().getType().getSimpleName()).append(NULL_CHAR)
                .append(deatilInfo.getPkField().getName()).append(");");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);

        // insert
        deatilInfo.getMapperBuffer().append(TABLE_SEPARATOR)
                .append("int").append(NULL_CHAR).append("insert(").append(deatilInfo.getClassName()).append(NULL_CHAR)
                .append(firstToLower(deatilInfo.getClassName())).append(");");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);

        // update
        deatilInfo.getMapperBuffer().append(TABLE_SEPARATOR)
                .append("int").append(NULL_CHAR).append("update(").append(deatilInfo.getClassName()).append(NULL_CHAR)
                .append(firstToLower(deatilInfo.getClassName())).append(");");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);

        // delete
        deatilInfo.getMapperBuffer().append(TABLE_SEPARATOR)
                .append("int").append(NULL_CHAR).append("delete(").append(deatilInfo.getPkField().getType().getSimpleName())
                .append(NULL_CHAR).append(deatilInfo.getPkField().getName()).append(");");
        appendChar(deatilInfo.getMapperBuffer(), LINE_SEPARATOR, 2);
    }

    private void buildMapperEnd(ClassDeatilInfo deatilInfo) {
        deatilInfo.getMapperBuffer().append("}");
    }

    /* 首字母小写 */
    private String firstToLower(String para) {
        return para.substring(0, 1).toLowerCase() + para.substring(1);
    }

    private String paramReplace(String propName) {
        return PARAM_EG.replace("0", propName);
    }

    private String stringJoin(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    private String formatNullChar(ClassDeatilInfo deatilInfo, Field field) {
        int c = deatilInfo.getFieldMaxLength() - field.getName().length() + DEFAULT_NULL_CHAR_LENGTH;
        StringBuilder nullChar = new StringBuilder();
        for (int i = 0; i < c; i++) {
            nullChar.append(NULL_CHAR);
        }
        return nullChar.toString();
    }

    /* 驼峰命名转为下划线命名 */
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

    public void writeToFile() {
        for (ClassDeatilInfo classDeatilInfo : classDeatilInfoList) {
            writeToFile(classDeatilInfo);
        }
    }

    private void writeToFile(ClassDeatilInfo deatilInfo) {
        // 写入xml文件
        String xmlFileName = deatilInfo.getXmlGeneratePath();
        writeToFile(xmlFileName, deatilInfo.getXmlBuffer().toString());
        // System.out.println(xmlFileName);
        // System.out.println(deatilInfo.getXmlBuffer().toString());

        // 写入mapper文件
        String mapperFileName = deatilInfo.getMapperGeneratePath();
        writeToFile(mapperFileName, deatilInfo.getMapperBuffer().toString());
        // System.out.println(mapperFileName);
        // System.out.println(deatilInfo.getMapperBuffer().toString());
    }

    /* 写入文件 */
    private static void writeToFile(String path, String content) {
        FileWriter fw = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                boolean flag = file.getParentFile().mkdirs();
                System.out.println("文件夹创建成功" + flag);
            }
            fw = new FileWriter(file);
            fw.write(content);
            fw.flush();

            System.out.println(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ignored) {
            }
        }
    }


    static class ClassDeatilInfo {

        private final Config config;

        /* 对应域模型 */
        private final Class<?> clazz;

        /* xml 缓存 */
        private final StringBuffer xmlBuffer = new StringBuffer();

        /* mapper 缓存 */
        private final StringBuffer mapperBuffer = new StringBuffer();

        // 主键字段
        private Field pkField;

        // 非主键字段
        private final List<Field> fieldList = new ArrayList<>();

        // 字段最大长度
        private int fieldMaxLength;

        public ClassDeatilInfo(Config config, Class<?> clazz) {
            this.config = config;
            this.clazz = clazz;
            // 初始化字段
            init();
        }

        public void init() {
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

                    if (field.getName().length() > fieldMaxLength) {
                        fieldMaxLength = field.getName().length();
                    }
                }

                cla = cla.getSuperclass();
            }

            Assert.notNull(pkField, "主键不能为空");
        }

        /* 判断是否是主键 需要@PK注解 */
        public static boolean checkFieldIsPk(Field field) {
            Id pk = field.getAnnotation(Id.class);
            return pk != null;
        }

        public StringBuffer getXmlBuffer() {
            return xmlBuffer;
        }

        public StringBuffer getMapperBuffer() {
            return mapperBuffer;
        }

        public Field getPkField() {
            return pkField;
        }

        public List<Field> getFieldList() {
            return fieldList;
        }

        public int getFieldMaxLength() {
            return fieldMaxLength;
        }

        /* 类名 xxx */
        public String getClassName() {
            return clazz.getSimpleName();
        }

        /* 类的全路径 com.bingo.xxx*/
        public String getClassFullPath() {
            return clazz.getTypeName();
        }

        /* 表名 驼峰转下划线 xxx_xx */
        public String getTableName() {
            return humpToUnderline(clazz.getSimpleName());
        }

        /* 类所在包 com.bingo */
        public String getClassPackage() {
            String classFullPath = getClassFullPath();
            return classFullPath.substring(0, classFullPath.lastIndexOf(DOT));
        }

        /* 类所在的真实路径 C:/idea/xx/src/main/java/com/bingo/*/
        public String getClassRealPath() {
            String path = Objects.requireNonNull(clazz.getResource("/")).getPath();
            path = path.substring(0, path.lastIndexOf("target")) + "src/main/java" + File.separator;
            return path;
        }

        /* xml生成的目录 C:/idea/xx/src/main/java/com/bingo/ */
        public String getXmlRealPath() {
            String path;
            if (config.getXmlPackage() == null || config.getXmlPackage().trim().length() == 0) {
                path = getClassRealPath() + getClassPackage().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            } else {
                path = getClassRealPath() + config.getXmlPackage().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            }
            return path;
        }

        /* xml文件生成路径 路径 + 名称 */
        public String getXmlGeneratePath() {
            return getXmlRealPath() + File.separator + getMapperName() + XML_SUFFER;
        }

        /* mapper生成目录 C:/idea/xx/src/main/java/com/bingo/ */
        public String getMapperRealPath() {
            String path;
            if (config.getMapperPackage() == null || config.getMapperPackage().trim().length() == 0) {
                path = getClassRealPath() + getClassPackage().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            } else {
                path = getClassRealPath() + config.getMapperPackage().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            }
            return path;
        }

        /* mapper文件名称，不带文件后缀 xxxMapper */
        public String getMapperName() {
            return clazz.getSimpleName() + MAPPER_SUFFER;
        }

        /* mapper文件所在目录 com.bingo */
        public String getMapperPath() {
            String path;
            if (config.getMapperPackage() == null || config.getMapperPackage().trim().length() == 0) {
                path = getClassPackage();
            } else {
                path = config.getMapperPackage();
            }
            return path;
        }

        /* mapper文件全路径，不带文件后缀 com.bingo.xxx */
        public String getMapperFullPath() {
            return getMapperPath() + DOT + getMapperName();
        }

        /* Mapper文件生成路径 路径 + 名称 */
        public String getMapperGeneratePath() {
            return getMapperRealPath() + File.separator + getMapperName() + MAPPER_FILE_SUFFER;
        }
    }
}

