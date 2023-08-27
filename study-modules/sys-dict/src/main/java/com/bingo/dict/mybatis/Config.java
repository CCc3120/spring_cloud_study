package com.bingo.dict.mybatis;

import org.springframework.util.Assert;

public class Config {
    /* model */
    private Class<?>[] clazzs;

    /* xml包路径 */
    private String xmlPackage;

    /* mapper接口包路径 */
    private String mapperPackage;

    /* 是否生成基本方法 */
    private boolean hasMethod = true;

    private Config(Class<?>... clazzs) {
        this.clazzs = clazzs;
    }

    private Config(String xmlPackage, String mapperPackage, boolean hasMethod, Class<?>... clazzs) {
        this.clazzs = clazzs;
        this.xmlPackage = xmlPackage;
        this.mapperPackage = mapperPackage;
        this.hasMethod = hasMethod;
    }

    public static Config defaultConfig(Class<?>... clazzs) {
        Assert.notNull(clazzs, "域模型不能为空");
        return new Config(clazzs);
    }

    public Class<?>[] getClazzs() {
        return clazzs;
    }

    public void setClazzs(Class<?>[] clazzs) {
        this.clazzs = clazzs;
    }

    public String getXmlPackage() {
        return xmlPackage;
    }

    public void setXmlPackage(String xmlPackage) {
        this.xmlPackage = xmlPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public boolean isHasMethod() {
        return hasMethod;
    }

    public void setHasMethod(boolean hasMethod) {
        this.hasMethod = hasMethod;
    }

    public static class Builder {

        private Class<?>[] clazzs;

        private String xmlPackage;

        private String mapperPackage;

        private boolean hasMethod = true;

        public static Builder getDefaule() {
            return new Builder();
        }

        public Builder clazzs(Class<?>... clazzs) {
            this.clazzs = clazzs;
            return this;
        }

        public Builder xmlPackage(String xmlPackage) {
            this.xmlPackage = xmlPackage;
            return this;
        }

        public Builder mapperPackage(String mapperPackage) {
            this.mapperPackage = mapperPackage;
            return this;
        }

        public Builder hasMethod(boolean hasMethod) {
            this.hasMethod = hasMethod;
            return this;
        }

        public Config build() {
            return new Config(xmlPackage, mapperPackage, hasMethod, clazzs);
        }
    }
}
