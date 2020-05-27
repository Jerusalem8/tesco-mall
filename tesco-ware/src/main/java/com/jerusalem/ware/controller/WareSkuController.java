package com.jerusalem.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jerusalem.common.to.SkuStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.ware.entity.WareSkuEntity;
import com.jerusalem.ware.service.WareSkuService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 商品库存
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@RestController
@RequestMapping("ware/ware/sku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /***
     * 查询Sku是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/stock")
    public R getSkuStock(@RequestBody List<Long> skuIds){
        List<SkuStockVo> stockVoList = wareSkuService.getSkuHasStock(skuIds);
        return R.ok().setData(stockVoList);
    }

    /***
    * 根据仓库、SKU ID进行分页查询
    * @param params
    * @return
    */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /***
    * 查询库存信息（用于修改时的数据回显）
    * @return
    */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);
        return R.ok().put("wareSku", wareSku);
    }

    /***
    * 新增库存信息
    * @return
    */
    @PostMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);
        return R.ok();
    }

    /***
    * 修改库存信息
    * @return
    */
    @PostMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);
        return R.ok();
    }

    /***
    * 删除、批量删除库存信息
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
