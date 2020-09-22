package com.jerusalem.goods.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.jerusalem.common.utils.PageUtils;
        import com.jerusalem.goods.entity.BrandEntity;

        import java.util.List;
        import java.util.Map;

/****
 * 服务层接口
 * 品牌
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
public interface BrandService extends IService<BrandEntity> {

    /**
     * 分页查询、关键词查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改
     * 同步更新其他关联表中的数据，保证数据的一致性
     * @param brand
     * @return
     */
    void updateDetail(BrandEntity brand);

    /***
     * 根据分类ID查询品牌
     * @param categoryId
     * @return
     */
    List<BrandEntity> getBrandsByCategoryId(Long categoryId);

    /***
     * 批量查询品牌信息（筛选栏查询）
     * @param brandIds
     * @return
     */
    List<BrandEntity> getBrandsByIds(List<Long> brandIds);
}

