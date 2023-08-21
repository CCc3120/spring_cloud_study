package com.bingo.study.common.core.web.interfaces;

import com.bingo.study.common.core.enums.CodeDescEnum;

/**
 * 查询排序接口
 *
 * @Author h-bingo
 * @Date 2023-08-21 11:03
 * @Version 1.0
 */
public interface IOrderModel extends IBaseWebModel {

    // private String orderType = ORDER_ASC;
    //
    // private String orderField;

    String getOrderField();

    void setOrderField(String orderField);

    String getOrderType();

    void setOrderType(String orderType);

    enum OrderType implements CodeDescEnum<String> {

        ORDER_ASC("asc", "升序"),
        ORDER_DESC("desc", "降序"),
        ;

        private final String code;

        private final String desc;

        OrderType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }
}
