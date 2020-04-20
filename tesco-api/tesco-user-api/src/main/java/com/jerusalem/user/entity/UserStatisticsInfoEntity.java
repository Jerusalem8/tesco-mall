package com.jerusalem.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/****
 * 实体类
 * 用户统计信息
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-20 20:16:14
 */
@Data
@TableName("user_statistics_info")
public class UserStatisticsInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 累计消费金额
	 */
	private BigDecimal consumeAmount;
	/**
	 * 累计优惠金额
	 */
	private BigDecimal couponAmount;
	/**
	 * 订单数量
	 */
	private Integer orderCount;
	/**
	 * 优惠券数量
	 */
	private Integer couponCount;
	/**
	 * 评价数
	 */
	private Integer commentCount;
	/**
	 * 退货数量
	 */
	private Integer returnOrderCount;
	/**
	 * 登录次数
	 */
	private Integer loginCount;
	/**
	 * 关注数量
	 */
	private Integer attendCount;
	/**
	 * 粉丝数量
	 */
	private Integer fansCount;
	/**
	 * 收藏的商品数量
	 */
	private Integer collectProductCount;
	/**
	 * 收藏的专题活动数量
	 */
	private Integer collectSubjectCount;
	/**
	 * 收藏的评论数量
	 */
	private Integer collectCommentCount;
	/**
	 * 邀请的朋友数量
	 */
	private Integer inviteFriendCount;

}
