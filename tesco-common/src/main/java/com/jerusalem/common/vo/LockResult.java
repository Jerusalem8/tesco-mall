package com.jerusalem.common.vo;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;

/****
 * @Author: jerusalem
 * @Description: LockResult
 * 锁定库存的结果
 * @Date 2020/11/10 15:52
 *****/
@Data
public class LockResult {
    private Long skuId;
    private Integer num;
    private boolean locked;
}
