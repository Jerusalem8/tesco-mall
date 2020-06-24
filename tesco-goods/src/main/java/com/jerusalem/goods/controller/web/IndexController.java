package com.jerusalem.goods.controller.web;

import com.jerusalem.goods.entity.CategoryEntity;
import com.jerusalem.goods.service.CategoryService;
import com.jerusalem.goods.vo.Category2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/****
 * @Author: jerusalem
 * @Description: IndexController
 * 前端页面跳转控制器
 * @Date 2020/5/28 13:52
 *****/
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    /***
     * 跳转到首页
     * @param model
     * @return
     */
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //查出所有的一级分类
        List<CategoryEntity> categoryList = categoryService.getCategoryLevelOne();
        model.addAttribute("categorys",categoryList);
        return "index";
    }

    /***
     * 获取三级分类数据树（首页）
     * @return
     */
    @ResponseBody
    @GetMapping("/index/category.json")
    public Map<String, List<Category2Vo>> getCategoryJson(){
        /**
         * 调用相关方法
         * getCategoryJsonWithSpringCache()：整合使用 SpringCache
         * getCategoryJson()：特殊处理，整合各种高级锁
         */
        Map<String, List<Category2Vo>> categoryMap = categoryService.getCategoryJsonWithSpringCache();
//        Map<String, List<Category2Vo>> categoryMap = categoryService.getCategoryJson();
        return categoryMap;
    }
}
