package com.bingo.study.common.core.utils.validate;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bingo.study.common.core.utils.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-04-13 17:10
 * @Version 1.0
 */
@Slf4j
public class ValidateFactory {

    private Map<String, ValidateField> validateFieldMap;

    private Object validateObj;

    private ValidateFactory(Map<String, ValidateField> validateFieldMap, Object validateObj) {
        this.validateFieldMap = validateFieldMap;
        this.validateObj = validateObj;
    }

    public static ValidateFactory getInstance(Object validateObj) {
        return new ValidateFactory(new HashMap<>(), validateObj);
    }

    public ValidateFactory addField(ValidateField validateField) {
        if (!validateFieldMap.containsKey(validateField.getFieldName())) {
            validateFieldMap.put(validateField.getFieldName(), validateField);
        }
        return this;
    }

    public ProcessResult<String> validate() {
        if (validateFieldMap.isEmpty()) {
            return ProcessResult.success();
        }

        StringBuilder errMsg = new StringBuilder();
        for (ValidateField validateField : validateFieldMap.values()) {

        }

        return ProcessResult.success();
    }

    private boolean canInstance(Class<?> clazz) {
        return !Modifier.isInterface(clazz.getModifiers())
                && !Modifier.isAbstract(clazz.getModifiers());
    }

    private ProcessResult<FieldInfo> getFieldInfo(String fieldName) {
        Object obj = this.validateObj;
        Field field = null;
        String[] split = fieldName.split("\\.");
        try {
            for (String s : split) {
                field = ReflectUtil.getField(obj.getClass(), s);
                obj = ReflectUtil.getFieldValue(obj, field);
                if (obj == null && canInstance(field.getType())) {
                    obj = field.getType().newInstance();
                }
                if (obj == null) {
                    // 如果是接口或抽象类中的属性，则跳过验证
                    return ProcessResult.fail();
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            return ProcessResult.fail();
        }
        return ProcessResult.success(new FieldInfo(field, obj));
    }

    private ProcessResult<String> validateField(ValidateField validateField) throws InstantiationException,
            IllegalAccessException {
        List<ValidateType> validateTypeList = validateField.getValidateTypeList();
        if (CollectionUtil.isEmpty(validateTypeList) || StringUtils.isBlank(validateField.getFieldName())) {
            return ProcessResult.success();
        }

        ProcessResult<FieldInfo> fieldInfo = getFieldInfo(validateField.getFieldName());
        if (!fieldInfo.isSuccess()) {
            return ProcessResult.success();
        }
        Field field = fieldInfo.getResult().getField();
        Object fieldValue = fieldInfo.getResult().getFieldValue();
        for (ValidateType validateType : validateTypeList) {
            if (validateType == ValidateType.NOT_NULL) {
                if (fieldValue == null) {
                    return ProcessResult.fail(String.format(validateType.getMessage(), validateField.getFieldDesc(),
                            validateField.getFieldName()));
                }
            } else if (validateType == ValidateType.NOT_BLANK) {
                if (field.getType().getTypeName().equals(String.class.getTypeName())) {
                    String stringValue = (String) fieldValue;
                    if (StringUtils.isBlank(stringValue)) {
                        return ProcessResult.fail(String.format(validateType.getMessage(), validateField.getFieldDesc(),
                                validateField.getFieldName()));
                    }
                }
            } else if (validateType == ValidateType.MAX_LENGTH) {
                if (validateField.getMaxLength() != null) {
                    if (field.getType().getTypeName().equals(String.class.getTypeName())) {
                        String stringValue = (String) fieldValue;
                        if (StringUtils.isNotBlank(stringValue) && stringValue.length() > validateField.getMaxLength()) {
                            return ProcessResult.fail(String.format(validateType.getMessage(),
                                    validateField.getFieldDesc(), validateField.getFieldName(),
                                    validateField.getMaxLength()));
                        }
                    } else if (field.getType().newInstance() instanceof Collection) {
                        Collection<?> collectionValue = (Collection<?>) fieldValue;
                        if (collectionValue != null && collectionValue.size() > validateField.getMaxLength()) {
                            return ProcessResult.fail(String.format(validateType.getMessage(),
                                    validateField.getFieldDesc(), validateField.getFieldName(),
                                    validateField.getMaxLength()));
                        }
                    }
                }
            } else if (validateType == ValidateType.MIN_LENGTH) {
                if (validateField.getMinLength() != null) {
                    if (field.getType().getTypeName().equals(String.class.getTypeName())) {
                        String stringValue = (String) fieldValue;
                        if (StringUtils.isNotBlank(stringValue) && stringValue.length() < validateField.getMinLength()) {
                            return ProcessResult.fail(String.format(validateType.getMessage(),
                                    validateField.getFieldDesc(), validateField.getFieldName(),
                                    validateField.getMaxLength()));
                        }
                    } else if (field.getType().newInstance() instanceof Collection) {
                        Collection<?> collectionValue = (Collection<?>) fieldValue;
                        if (collectionValue != null && collectionValue.size() < validateField.getMinLength()) {
                            return ProcessResult.fail(String.format(validateType.getMessage(),
                                    validateField.getFieldDesc(), validateField.getFieldName(),
                                    validateField.getMaxLength()));
                        }
                    }
                }
            } else if (validateType == ValidateType.REGEX) {
                if (StringUtils.isNotBlank(validateField.getRegex())
                        && field.getType().getTypeName().equals(String.class.getTypeName())) {
                    String stringValue = (String) fieldValue;
                    boolean matches = stringValue.matches(validateField.getRegex());
                    if (!matches) {
                        return ProcessResult.fail(String.format(validateType.getMessage(),
                                validateField.getFieldDesc(), validateField.getFieldName()));
                    }
                }
            } else if (validateType == ValidateType.RANGE) {
                if(field.getType().newInstance() instanceof Number){
                    Number numberValue = (Number) fieldValue;
                    String[] mark = {"-∞", "+∞"};
                    boolean flag = true;
                    if (validateField.getMin() != null) {
                        mark[0] = validateField.getMin().toString();
                        // if(){
                        //
                        // }
                        // Math.
                    }
                    if (validateField.getMax() != null) {
                        mark[1] = validateField.getMax().toString();
                    }
                }
            }
        }
        return ProcessResult.success();
    }

    @Data
    @AllArgsConstructor
    private static class FieldInfo {

        private Field field;

        private Object fieldValue;
    }
}
