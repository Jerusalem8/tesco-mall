package com.jerusalem.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.jerusalem.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.coupon.entity.SkuFullReductionEntity;
import com.jerusalem.coupon.service.SkuFullReductionService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 商品满减信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:47:38
 */
@RestController
@RequestMapping("coupon/sku/full/reduction")
public class SkuFullReductionController {

    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /***
     * 保存满减信息
     * @return
     */
    @PostMapping("/save")
    public R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo){
        skuFullReductionService.saveSkuReduction(skuReductionTo);
        return R.ok();
    }



    /***
     * 分页查询
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
     * 查询
     * @return
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /***
     * 修改
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
        skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /***
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
