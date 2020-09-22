package com.jerusalem.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.goods.dao.SpuInfoDescDao;
import com.jerusalem.goods.entity.SpuInfoDescEntity;
import com.jerusalem.goods.service.SpuInfoDescService;

/****
 * 服务层接口实现类
 * spu信息介绍
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    /***
     * 保存SPU的图片描述
     * @param spuInfoDesc
     */
    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDesc) {
        this.baseMapper.insert(spuInfoDesc);
    }

}