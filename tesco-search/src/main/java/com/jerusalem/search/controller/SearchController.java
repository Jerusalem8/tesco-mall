package com.jerusalem.search.controller;

import com.jerusalem.search.service.SearchService;
import com.jerusalem.search.vo.SearchParam;
import com.jerusalem.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/****
 * @Author: jerusalem
 * @Description: SearchController
 * @Date 2020/6/25 11:11
 *****/
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    /***
     * 搜索功能
     * 根据页面传递来查询参数，去ES查询，封装到SearchResult返回给页面
     * @return
     */
    @GetMapping("/search.html")
    public String searchPage(SearchParam searchParam, Model model, HttpServletRequest request){
        searchParam.setOldQueryString(request.getQueryString());
        SearchResult result = searchService.search(searchParam);
        model.addAttribute("result",result);
        return "search";
    }
}
