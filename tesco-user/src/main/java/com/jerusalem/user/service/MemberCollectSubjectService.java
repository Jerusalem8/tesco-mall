package com.jerusalem.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.user.entity.MemberCollectSubjectEntity;

import java.util.Map;

/****
 * 服务层接口
 * 会员收藏的专题活动
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 17:48:41
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils queryPage(Map<String, Object> params);
}

