package com.jerusalem.goods.controller.server;

import java.util.Arrays;
import java.util.Map;
import com.jerusalem.goods.vo.AttrResponseVo;
import com.jerusalem.goods.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jerusalem.goods.service.AttrService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;

/****
 * 控制层
 * 商品属性
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@RestController
@RequestMapping("goods/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;

    /***
     * 根据分类ID、关键词分页查询基本属性和销售属性
     * @param params
     * @param categoryId
     * @return
     */
    @GetMapping("/{attrType}/list/{categoryId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable(value = "categoryId") Long categoryId,
                          @PathVariable(value = "attrType") String attrType){
        PageUtils page = attrService.queryAttrPage(params,categoryId,attrType);
        return R.ok().put("page", page);
    }

    /***
    * 查询属性信息（实现修改时的数据回显）
    * @return
    */
    @GetMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrResponseVo attrResponseVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrResponseVo);
    }

    /***
    * 新增属性
    * @return
    */
    @PostMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);
        return R.ok();
    }

    /***
    * 修改属性
    * @return
    */
    @PostMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /***
    * 删除、批量删除属性
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));
        return R.ok();
    }
}
