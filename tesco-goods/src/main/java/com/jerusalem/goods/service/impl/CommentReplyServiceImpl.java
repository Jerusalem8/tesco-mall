package com.jerusalem.goods.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;

import com.jerusalem.goods.dao.CommentReplyDao;
import com.jerusalem.goods.entity.CommentReplyEntity;
import com.jerusalem.goods.service.CommentReplyService;

/****
 * 服务层接口实现类
 * 商品评价回复关系
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("commentReplyService")
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyDao, CommentReplyEntity> implements CommentReplyService {

    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentReplyEntity> page = this.page(
                new Query<CommentReplyEntity>().getPage(params),
                new QueryWrapper<CommentReplyEntity>()
        );
        return new PageUtils(page);
    }

}