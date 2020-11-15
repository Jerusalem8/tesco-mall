package com.jerusalem.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.ware.dao.WareOrderTaskDao;
import com.jerusalem.ware.entity.WareOrderTaskEntity;
import com.jerusalem.ware.service.WareOrderTaskService;

/****
 * 服务层接口实现类
 * 库存工作单
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );
        return new PageUtils(page);
    }


    /***
     * 查询库存工作单
     * @param orderSn
     * @return
     */
    @Override
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {
        WareOrderTaskEntity taskEntity = this.getOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderSn));
        return taskEntity;
    }

}