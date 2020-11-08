package com.jerusalem.ware.vo;

import com.jerusalem.common.vo.UserAddressVo;
import lombok.Data;

import java.math.BigDecimal;

/****
 * @Author: jerusalem
 * @Description: AddressWithFare
 * 地址及运费模型封装返回页面
 * @Date 2020/11/8 16:19
 *****/
@Data
public class AddressWithFareVo {
    private UserAddressVo userAddressVo;
    private BigDecimal fare;
}
