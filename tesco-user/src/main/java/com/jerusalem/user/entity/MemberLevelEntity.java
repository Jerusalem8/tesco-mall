package com.jerusalem.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/****
 * 实体类
 * 会员等级
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:48:41
 */
@Data
@TableName("member_level")
public class MemberLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 等级名称
	 */
	private String name;
	/**
	 * 等级需要的成长值
	 */
	private Integer growthPoint;
	/**
	 * 是否为默认等级[0->不是；1->是]
	 */
	private Integer defaultStatus;
	/**
	 * 免运费标准
	 */
	private BigDecimal freeFreightPoint;
	/**
	 * 每次评价获取的成长值
	 */
	private Integer commentGrowthPoint;
	/**
	 * 是否有免邮特权
	 */
	private Integer priviledgeFreeFreight;
	/**
	 * 是否有会员价格特权
	 */
	private Integer priviledgeMemberPrice;
	/**
	 * 是否有生日特权
	 */
	private Integer priviledgeBirthday;
	/**
	 * 备注
	 */
	private String note;

}
