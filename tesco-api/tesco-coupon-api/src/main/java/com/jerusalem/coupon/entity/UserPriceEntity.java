package com.jerusalem.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/****
 * 实体类
 * 商品用户价格
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Data
@TableName("user_price")
public class UserPriceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * 用户等级id
	 */
	private Long userLevelId;
	/**
	 * 用户等级名
	 */
	private String userLevelName;
	/**
	 * 用户对应价格
	 */
	private BigDecimal userPrice;
	/**
	 * 可否叠加其他优惠[0-不可叠加优惠，1-可叠加]
	 */
	private Integer addOther;

}
