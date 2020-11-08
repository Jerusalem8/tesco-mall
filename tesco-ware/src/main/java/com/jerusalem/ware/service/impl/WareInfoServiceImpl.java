package com.jerusalem.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.UserAddressVo;
import com.jerusalem.user.feign.UserReceiveAddressFeign;
import com.jerusalem.ware.vo.AddressWithFareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.ware.dao.WareInfoDao;
import com.jerusalem.ware.entity.WareInfoEntity;
import com.jerusalem.ware.service.WareInfoService;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 仓库信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:54:29
 */
@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    UserReceiveAddressFeign userReceiveAddressFeign;


    /**
    * 根据关键词进行分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("id",key)
                        .or().like("name",key)
                        .or().like("address",key)
                        .or().like("areacode",key);
        }

        IPage<WareInfoEntity> page = this.page(new Query<WareInfoEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    /***
     * 根据收货地址获取运费
     * @param addrId
     * @return
     */
    @Override
    public AddressWithFareVo getFare(Long addrId) {
        AddressWithFareVo addressWithFareVo = new AddressWithFareVo();
        R addressInfo = userReceiveAddressFeign.info(addrId);
        UserAddressVo userAddressVo = addressInfo.getData("userReceiveAddress",new TypeReference<UserAddressVo>() {
        });
        if (userAddressVo!=null){
            String phone = userAddressVo.getPhone();
            //TODO 简单处理运费问题，将手机号的最后一位作为运费
            String substring = phone.substring(phone.length() - 1, phone.length());
            BigDecimal fare = new BigDecimal(substring);
            addressWithFareVo.setFare(fare);
            addressWithFareVo.setUserAddressVo(userAddressVo);
            return addressWithFareVo;
        }
        return null;
    }
}