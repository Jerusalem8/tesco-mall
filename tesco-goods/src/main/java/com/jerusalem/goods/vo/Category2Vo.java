package com.jerusalem.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: Category2Vo
 * 二级分类
 * @Date 2020/5/28 15:19
 *****/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category2Vo {

    /**
     * 一级父分类ID
     */
    private String category1Id;
    /**
     * 三级子分类集合
     */
    private List<Category3Vo> category3List;
    private String id;
    private String name;


    /***
     * 三级分类
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Category3Vo{
        /**
         * 二级父分类ID
         */
        private String category2Id;
        private String id;
        private String name;
    }
}
