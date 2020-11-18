package com.jerusalem.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: SeckillSessionWithSkusVo
 * 秒杀活动场次及其关联的秒杀商品
 * @Date 2020/11/17 16:26
 *****/
@Data
public class SeckillSessionWithSkusVo {

    /**
     * id
     */
    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    /***
     * 关联的秒杀商品
     */
    private List<SeckillSkuVo> relationSkus;
}
