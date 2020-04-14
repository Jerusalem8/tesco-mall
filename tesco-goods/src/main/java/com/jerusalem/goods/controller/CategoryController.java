package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/list/tree")
    public R categoryListTree(){
        List<CategoryEntity> categoryListTree = categoryService.listWithTree();
        return R.ok().put("data", categoryListTree);
    }

    /***
     * 新增
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
        categoryService.save(category);
        return R.ok();
    }

    /***
     * 修改
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateById(category);
        return R.ok();
    }

    /***
     * 拖拽排序
     * @param category
     * @return
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category){
        //批量修改
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /***
     * 批量、逻辑删除
     * @RequestBody: 获取请求体，必须发送POST请求
     * SpringMVC自动将请求体的数据（json），转为对应的对象
     * @return
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] categoryIds){
        categoryService.removeMenuIds(Arrays.asList(categoryIds));
        return R.ok();
    }



    /***
    * 分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /***
    * 查询
    * @return
    */
    @RequestMapping("/info/{categoryId}")
    public R info(@PathVariable("categoryId") Long categoryId){
		CategoryEntity category = categoryService.getById(categoryId);
        return R.ok().put("category", category);
    }
}
