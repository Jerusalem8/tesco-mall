package com.jerusalem.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.jerusalem.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jerusalem.ware.entity.PurchaseDetailEntity;
import com.jerusalem.ware.service.PurchaseDetailService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 采购需求信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@RestController
@RequestMapping("ware/purchase/detail")
public class PurchaseDetailController {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /***
     * 合并采购项到采购单
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseDetailService.mergePurchaseDetail(mergeVo);
        return R.ok();
    }

    /***
    * 根据仓库、采购单状态、关键字进行分页查询采购项
    * @param params
    * @return
    */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseDetailService.queryPage(params);
        return R.ok().put("page", page);
    }

    /***
    * 查询采购项信息（用于修改时的数据回显）
    * @return
    */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /***
    * 新增采购项
    * @return
    */
    @PostMapping("/save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /***
    * 修改采购项
    * @return
    */
    @PostMapping("/update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.updateById(purchaseDetail);
        return R.ok();
    }

    /***
    * 删除、批量删除采购项
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseDetailService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
