package com.bingo.mybatisPlus.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.study.common.core.web.interfaces.IBaseWebModel;
import com.bingo.study.common.core.web.interfaces.IOrderModel;
import com.bingo.study.common.core.web.interfaces.IPageModel;
import com.bingo.study.common.core.web.page.PageResult;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * @Author h-bingo
 * @Date 2023-08-21 14:29
 * @Version 1.0
 */
public class PageHelper {

    /***
     * 获取分页对象 Page
     *
     * @Param [baseWebModel, f]
     * @Return com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>
     * @Date 2023-08-21 14:52
     */
    public static <T> IPage<T> getPage(IBaseWebModel baseWebModel, Function<String, String> f) {
        Page<T> page = new Page<>();
        if (baseWebModel instanceof IPageModel) {
            IPageModel pageModel = (IPageModel) baseWebModel;
            if (pageModel.getPageNum() == null) {
                page.setCurrent(IPageModel.DEFAULT_PAGE_NUM);
            } else if (pageModel.getPageNum() > 1) {
                page.setCurrent(pageModel.getPageNum());
            }

            if (pageModel.getPageSize() == null) {
                page.setSize(IPageModel.DEFAULT_PAGE_SIZE);
            } else {
                page.setSize(pageModel.getPageSize());
            }
        }

        if (baseWebModel instanceof IOrderModel) {
            IOrderModel orderModel = (IOrderModel) baseWebModel;
            if (StringUtils.isNotBlank(orderModel.getOrderField())) {
                if (f == null) {
                    f = s -> s;
                }

                if (IOrderModel.OrderType.ORDER_DESC.getCode().equals(orderModel.getOrderType())) {
                    page.addOrder(OrderItem.desc(f.apply(orderModel.getOrderField())));
                } else {
                    page.addOrder(OrderItem.asc(f.apply(orderModel.getOrderField())));
                }
            }
        }
        return page;
    }

    public static <T> IPage<T> getPage(IBaseWebModel baseWebModel) {
        return getPage(baseWebModel, null);
    }

    /***
     * 对象转换，并且封装 PageResult 对象
     *
     * @Param [page, f]
     * @Return com.bingo.study.common.core.web.page.PageResult<N>
     * @Date 2023-08-21 15:13
     */
    public static <O, N> PageResult<N> getPageResult(IPage<O> page, Function<O, N> f) {
        return getPageResult(convert(page, f));
    }

    /***
     * 封装 PageResult 对象
     *
     * @Param [page]
     * @Return com.bingo.study.common.core.web.page.PageResult<T>
     * @Date 2023-08-21 15:13
     */
    public static <T> PageResult<T> getPageResult(IPage<T> page) {
        return PageResult.of(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
    }

    /***
     * 对象转换
     *
     * @Param [page, f]
     * @Return com.baomidou.mybatisplus.core.metadata.IPage<N>
     * @Date 2023-08-21 15:14
     */
    public static <O, N> IPage<N> convert(IPage<O> page, Function<O, N> f) {
        return page.convert(f);
    }
}
