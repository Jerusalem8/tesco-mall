package com.jerusalem.common.vo;

import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: UserReceiveAddressVo
 * 用户收货地址数据模型封装
 * @Date 2020/11/7 13:10
 *****/
@Data
public class UserAddressVo {

    /**
     * id
     */
    private Long id;
    /**
     * user_id
     */
    private Long userId;
    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 省份/直辖市
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区
     */
    private String region;
    /**
     * 详细地址(街道)
     */
    private String detailAddress;
    /**
     * 省市区代码
     */
    private String areacode;
    /**
     * 是否默认
     */
    private Integer defaultStatus;
}
