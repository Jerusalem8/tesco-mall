package com.jerusalem.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jerusalem.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jerusalem.ware.entity.PurchaseEntity;
import com.jerusalem.ware.service.PurchaseService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.R;


/****
 * 控制层
 * 采购信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /***
     * 查询未领取（还未开始执行的）的采购单
     * @param params
     * @return
     */
    @GetMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryUnreceivePage(params);
        return R.ok().put("page", page);
    }

    /***
    * 根据采购单状态、关键词进行分页查询
    * @param params
    * @return
    */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }

    /***
     * 采购人员领取采购单
     * @param purchaseIds
     * @return
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> purchaseIds){
        purchaseService.received(purchaseIds);
        return R.ok();
    }

    /***
     * 完成采购
     * @param purchaseDoneVo
     * @return
     */
    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo purchaseDoneVo){
        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }

    /***
    * 查询采购单信息（用于修改时的数据回显）
    * @return
    */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /***
    * 新增采购单
    * @return
    */
    @PostMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);
        return R.ok();
    }

    /***
    * 修改采购单
    * @return
    */
    @PostMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);
        return R.ok();
    }

    /***
    * 删除、批量删除采购单
    * @return
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
