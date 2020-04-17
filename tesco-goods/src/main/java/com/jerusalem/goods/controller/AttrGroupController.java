package com.jerusalem.goods.controller;

import java.util.Arrays;
import java.util.Map;

import com.jerusalem.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.goods.entity.AttrGroupEntity;
import com.jerusalem.goods.service.AttrGroupService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 属性分组
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/attr/group")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    /***
     * 根据三级分类ID、搜索关键词（属性分组名）查询属性分组
     * 分类ID为0，则默认查询全部
     * @param params
     * @param categoryId
     * @return
     */
    @GetMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("categoryId") Long categoryId){
        PageUtils page = attrGroupService.queryPage(params,categoryId);
        return R.ok().put("page", page);
    }

    /***
    * 查询属性分组信息(实现修改时的数据回显)
    * @return
    */
    @GetMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		//根据属性分组ID查询出所属分类ID
		Long categoryId = attrGroup.getCategoryId();
		//根据分类ID查询出完整的三级分类ID路径
        Long[] categoryPath = categoryService.findCategoryPath(categoryId);
        //返回
        attrGroup.setCategoryPath(categoryPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /***
    * 新增属性分组
    * @return
    */
    @PostMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /***
    * 修改属性分组
    * @return
    */
    @PostMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /***
    * 删除、批量删除属性分组
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }




    /***
     * 分页查询
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }
}
