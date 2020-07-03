package com.jerusalem.search.service.impl;

import com.jerusalem.search.service.SearchService;
import com.jerusalem.search.vo.SearchParam;
import com.jerusalem.search.vo.SearchResult;
import org.springframework.stereotype.Service;

/****
 * @Author: jerusalem
 * @Description: SearchService
 * @Date 2020/6/25 14:49
 *****/
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    /***
     * 检索方法
     * 根据页面传递来查询参数，去ES查询，封装到SearchResult返回给页面
     * @param searchParam
     * @return
     */
    @Override
    public SearchResult search(SearchParam searchParam) {
        return null;
    }
}
