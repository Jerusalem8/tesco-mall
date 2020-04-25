package com.jerusalem.goods.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/****
 * 视图对象
 * 1.接收页面传递的数据，封装对象
 * 2.将业务处理后的对象，封装成页面所需的数据
 *
 * SpuVo：商品Spu的整体封装
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-018 14:48:19
 */
@Data
public class SpuVo {

    /**
     * 基本信息
     * --------------
     * 商品名称
     * 商品描述
     * 所属三级分类ID
     * 所属品牌ID
     * 重量
     * 发布状态
     */
    private String spuName;
    private String spuDescription;
    private Long categoryId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;

    /**
     * 图片介绍集合
     */
    private List<String> description;

    /**
     * 图集
     */
    private List<String> images;

    /**
     * 积分信息
     */
    private Bounds bounds;

    /**
     * 基本属性（规格参数）
     */
    private List<BaseAttrs> baseAttrs;

    /**
     * SKU集合
     */
    private List<SkuVo> skus;
}