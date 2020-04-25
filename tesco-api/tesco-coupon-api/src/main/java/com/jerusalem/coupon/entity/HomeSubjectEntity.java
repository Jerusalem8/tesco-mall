package com.jerusalem.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/****
 * 实体类
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Data
@TableName("home_subject")
public class HomeSubjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 专题名字
	 */
	private String name;
	/**
	 * 专题标题
	 */
	private String title;
	/**
	 * 专题副标题
	 */
	private String subTitle;
	/**
	 * 显示状态
	 */
	private Integer status;
	/**
	 * 详情连接
	 */
	private String url;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 专题图片地址
	 */
	private String img;

}
