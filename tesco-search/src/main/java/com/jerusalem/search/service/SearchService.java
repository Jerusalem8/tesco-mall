package com.jerusalem.search.service;

import com.jerusalem.search.vo.SearchParam;
import com.jerusalem.search.vo.SearchResult;
import org.springframework.stereotype.Service;

/****
 * @Author: jerusalem
 * @Description: SearchSevice
 * @Date 2020/6/25 14:45
 *****/
public interface SearchService {

    /***
     * 检索业务
     * 根据页面传递来查询参数，去ES查询，封装到SearchResult返回给页面
     * @param searchParam
     * @return
     */
    SearchResult search(SearchParam searchParam);

}
