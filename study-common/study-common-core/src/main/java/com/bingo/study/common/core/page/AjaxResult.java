package com.bingo.study.common.core.page;


import com.bingo.study.common.core.utils.ObjectUtil;

import java.util.HashMap;

/**
 * @author bingo
 * @date 2022-03-24 10:34
 */
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    private static final String MSG_TAG = "message";

    /**
     * 数据对象
     */
    private static final String DATA_TAG = "data";

    protected AjaxResult() {
    }

    protected AjaxResult(String code, String message, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, message);
        if (!ObjectUtil.equals(data, null)) {
            super.put(DATA_TAG, data);
        }
    }
}
