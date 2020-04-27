package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jerusalem.goods.entity.CategoryBrandRelationEntity;
import com.jerusalem.goods.service.CategoryBrandRelationService;
import com.jerusalem.common.utils.R;

/****
 * 控制层
 * 品牌分类关联
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/category/brand/relation")
public class CategoryBrandRelationController {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /***
     * 获取品牌分类关联列表
     * @param brandId
     * @return
     */
    @GetMapping("/list")
    public R categoryList(@RequestParam("brandId")Long brandId){
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId)
        );
        return R.ok().put("data", data);
    }

    /***
    * 新增品牌分类关联
    * @return
    */
    @PostMapping("/save")
    public R saveDetail(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /***
    * 删除品牌分类关联
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
