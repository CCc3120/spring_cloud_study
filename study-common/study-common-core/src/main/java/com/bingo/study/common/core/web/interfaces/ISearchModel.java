package com.bingo.study.common.core.web.interfaces;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-21 11:06
 * @Version 1.0
 */
public interface ISearchModel extends IBaseWebModel {

    // private String searchContent;
    //
    // private String searchField;
    //
    // private List<String> searchFieldList;

    String getSearchContent();

    void setSearchContent(String searchContent);

    String getSearchField();

    void setSearchField(String searchField);

    List<String> getSearchFieldList();

    void setSearchFieldList(List<String> searchFieldList);
}
