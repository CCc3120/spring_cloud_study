package com.bingo.dict.enums;

import com.bingo.dict.controller.SysDictCategoryController;
import com.bingo.dict.controller.SysDictDataController;
import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 映射注册配置
 *
 * @Author h-bingo
 * @Date 2023-08-17 16:12
 * @Version 1.0
 */
@Getter
public enum MappingRegisterEnum {

    // ***************************SysDictDataController***************************** //
    SYSDICTDATA_SAVE("保存/新增", "/sysDictData/save", "save", RequestMethod.POST,
            SysDictDataController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class,
            SysDictData.class}),
    SYSDICTDATA_UPDATE("保存/修改", "/sysDictData/update", "update", RequestMethod.POST,
            SysDictDataController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class}),
    SYSDICTDATA_DELETE("删除", "/sysDictData/delete", "delete", RequestMethod.DELETE,
            SysDictDataController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class}),

    // ***************************SysDictCategoryController***************************** //
    SYSDICTCATEGORY_SAVE("保存/新增", "/sysDictCategory/save", "save", RequestMethod.POST,
            SysDictCategoryController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class,
            SysDictCategory.class}),
    SYSDICTCATEGORY_UPDATE("保存/修改", "/sysDictCategory/update", "update", RequestMethod.POST,
            SysDictCategoryController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class}),
    SYSDICTCATEGORY_DELETE("删除", "/sysDictCategory/delete", "delete", RequestMethod.DELETE,
            SysDictCategoryController.class, new Class[]{HttpServletRequest.class, HttpServletResponse.class}),

    ;

    MappingRegisterEnum(String name, String path, String methodName, RequestMethod requestMethod, Class<?> beanClass,
            Class<?>[] paramClass) {
        this.name = name;
        this.path = path;
        this.methodName = methodName;
        this.requestMethod = requestMethod;
        this.beanClass = beanClass;
        this.paramClass = paramClass;
    }

    /**
     * 映射名称
     */
    private String name;
    /**
     * 映射地址
     */
    private String path;
    /**
     * 映射处理的class
     */
    private Class<?> beanClass;
    /**
     * 映射方法名
     */
    private String methodName;
    /**
     * 映射方法类型
     */
    private RequestMethod requestMethod;
    /**
     * 映射处理方法参数类型数组
     */
    private Class<?>[] paramClass;
}
