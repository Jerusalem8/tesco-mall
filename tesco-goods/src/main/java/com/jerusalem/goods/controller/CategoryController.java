package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.goods.entity.CategoryEntity;
import com.jerusalem.goods.service.CategoryService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;

/****
 * 控制层
 * 商品三级分类
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /***
     * 查询所有分类，并以树形结构组装
     * @return
     */
    @GetMapping("/list/tree")
    public R categoryListTree(){
        List<CategoryEntity> categoryListTree = categoryService.listWithTree();
        return R.ok().put("data", categoryListTree);
    }

    /***
     * 查询分类信息（实现修改分类时数据的回显）
     * @return
     */
    @GetMapping("/info/{categoryId}")
    public R info(@PathVariable("categoryId") Long categoryId){
        CategoryEntity category = categoryService.getById(categoryId);
        return R.ok().put("category", category);
    }

    /***
     * 新增分类
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
        categoryService.save(category);
        return R.ok();
    }

    /***
     * 修改分类
     * 同步更新其他关联表中的数据，保证数据的一致性
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateCascade(category);
        return R.ok();
    }

    /***
     * 拖拽、排序、批量更新
     * @param category
     * @return
     */
    @PostMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /***
     * 批量、逻辑删除分类
     * @RequestBody: 获取请求体，必须发送POST请求
     * SpringMVC自动将请求体的数据（json），转为对应的对象
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] categoryIds){
        categoryService.removeMenuIds(Arrays.asList(categoryIds));
        return R.ok();
    }






}
