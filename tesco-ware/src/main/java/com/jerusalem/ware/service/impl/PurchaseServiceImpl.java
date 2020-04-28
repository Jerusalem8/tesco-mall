package com.jerusalem.ware.service.impl;

import com.jerusalem.common.constant.WareConstant;
import com.jerusalem.ware.entity.PurchaseDetailEntity;
import com.jerusalem.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.ware.dao.PurchaseDao;
import com.jerusalem.ware.entity.PurchaseEntity;
import com.jerusalem.ware.service.PurchaseService;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 采购信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
    * 根据采购单状态、关键词进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        //关键词（采购单ID、采购人名、采购人ID）
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and(wrapper->{
                wrapper.eq("id",key)
                        .or().eq("assignee_id",key)
                        .or().like("assignee_name",key);
            });
        }
        //状态
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }

        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params),queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 查询未领取（还未开始执行的）的采购单
     * @param params
     * @return
     */
    @Override
    public PageUtils queryUnreceivePage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0).or().eq("status",1);

        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params),queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 采购人员领取采购单
     * @param purchaseIds
     * @return
     */
    @Override
    public void received(List<Long> purchaseIds) {
        //1.得到符合领取状态（新建或已分配状态）的集合，并改变状态
        List<PurchaseEntity> collect = purchaseIds.stream().map(purchaseId -> {
            PurchaseEntity purchaseEntity = this.getById(purchaseId);
            return purchaseEntity;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item ->{
            //改变状态
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //2.改变采购单的状态
        this.updateBatchById(collect);
        //3.改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.detailListByPurchaseId(item.getId());
        });

    }
}