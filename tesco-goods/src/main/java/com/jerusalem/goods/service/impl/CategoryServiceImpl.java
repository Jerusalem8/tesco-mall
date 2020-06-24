package com.jerusalem.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jerusalem.goods.service.CategoryBrandRelationService;
import com.jerusalem.goods.vo.Category2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;
import com.jerusalem.goods.dao.CategoryDao;
import com.jerusalem.goods.entity.CategoryEntity;
import com.jerusalem.goods.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/****
 * 服务层接口实现类
 * 商品三级分类
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-09 14:48:19
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    /**
     * Redis缓存
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Redisson缓存
     */
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /***
     * 查询所有分类，并以树形结构组装（后台管理系统）
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> categoryList = baseMapper.selectList(null);
        //2.组装成父子的树形结构
        //2.1 找到所有的一级分类
        List<CategoryEntity> level1Menus = categoryList.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, categoryList));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }
    /***
     * 抽取的方法
     * 递归查找所有分类的子分类
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCategoryId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 逻辑删除
     * @param asList
     */
    @Override
    public void removeMenuIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 根据第三级分类ID查询完整的三级分类ID路径
     * 路径示例：[2,25,225]
     * @param categoryId
     * @return
     */
    @Override
    public Long[] findCategoryPath(Long categoryId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(categoryId, paths);
        //逆序转换
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }
    /***
     * 抽取的方法
     * 递归查找所有分类的父分类
     * 最终得到逆序[225,25,2]
     * @param categoryId
     * @param paths
     * @return
     */
    private List<Long> findParentPath(Long categoryId, List<Long> paths) {
        //收集当前节点id
        paths.add(categoryId);
        CategoryEntity byId = this.getById(categoryId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 级联更新
     * 更新分类表及其他关联表的关联数据
     * @Caching ：组合进行多种缓存操作
     * @CacheEvict(value={"category"},allEntries=true) ：删除某分区所有数据
     * @return
     */
//    @Caching(evict = {
//            @CacheEvict(value = {"category"},key = "'getCategoryLevelOne'"),
//            @CacheEvict(value = {"category"},key = "'getCategoryJson'")
//    })
    @CacheEvict(value = {"category"},allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCategoryId(), category.getName());
    }

    /***
     * 查询所有的一级分类（首页）
     * 方法一：整合使用 SpringCache
     * @Cacheable() ：当前方法需要缓存。如果缓存中有，不调用方法；如果缓存中没有，调用方法并将结果保存到缓存。
     *                value：缓存分区名称
     *                sync：开启本地锁
     * @return
     */
    @Cacheable(value = {"category"},key = "#root.methodName",sync = true)
    @Override
    public List<CategoryEntity> getCategoryLevelOne() {
        //缓存中没有，执行业务逻辑
        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_level", 1);
        List<CategoryEntity> categoryList = baseMapper.selectList(queryWrapper);
        System.out.println("查询数据库，获取一级分类数据！");
        return categoryList;
    }

    /***
     * 获取三级分类数据树（首页）
     * 方法一：整合使用 SpringCache
     * @return
     */
    @Cacheable(value = {"category"},key = "#root.methodName",sync = true)
    @Override
    public Map<String, List<Category2Vo>> getCategoryJsonWithSpringCache(){
        //缓存中没有，执行业务逻辑
        //1.一次性查询出所有分类categoryList
        List<CategoryEntity> categoryList = baseMapper.selectList(null);

        //2.调用方法，在categoryList集合中得到一级分类集合
        List<CategoryEntity> categoryOneList = getCategoryXList(categoryList, 0L);

        //3.封装数据
        Map<String, List<Category2Vo>> categoryMap = categoryOneList.stream().collect(Collectors.toMap(key -> key.getCategoryId().toString(), value -> {
            //调用方法，根据父ID，在categoryList集合中得到对应的二级分类集合
            List<CategoryEntity> categoryTwoList = getCategoryXList(categoryList, value.getCategoryId());

            //封装，得到Category2Vo的形式
            List<Category2Vo> category2Vos = null;
            if (categoryTwoList != null) {
                category2Vos = categoryTwoList.stream().map(categoryTwo -> {
                    Category2Vo category2Vo = new Category2Vo(
                            value.getCategoryId().toString(),
                            null,
                            categoryTwo.getCategoryId().toString(),
                            categoryTwo.getName());
                    //调用方法，根据父ID，在categoryList集合中得到对应的三级分类集合
                    List<CategoryEntity> categoryThreeList = getCategoryXList(categoryList, categoryTwo.getCategoryId());

                    //封装，得到Category3Vo的形式
                    if (categoryThreeList != null) {
                        List<Category2Vo.Category3Vo> category3Vos = categoryThreeList.stream().map(categoryThree -> {
                            Category2Vo.Category3Vo category3Vo = new Category2Vo.Category3Vo(
                                    categoryTwo.getCategoryId().toString(),
                                    categoryThree.getCategoryId().toString(),
                                    categoryThree.getName()
                            );
                            return category3Vo;
                        }).collect(Collectors.toList());
                        category2Vo.setCategory3List(category3Vos);
                    }
                    return category2Vo;
                }).collect(Collectors.toList());
            }
            return category2Vos;
        }));
        System.out.println("查询数据库，获取三级分类数据树！");
        return categoryMap;
    }

    /***
     * 获取三级分类数据树（首页）
     * 方法二：特殊处理，整合各种高级锁
     * TODO 堆外内存溢出
     * springboot2.0以后默认使用lettuce作为操作redis的客户端，他使用netty进行网络通信
     * lettuce的bug导致netty堆外内存溢出
     * 解决方案：
     *      1）、升级lettuce
     *      2）、切换使用jedis
     * @return
     */
    @Override
    public Map<String, List<Category2Vo>> getCategoryJson(){

        /***
         * 缓存优化
         * 解决穿透，雪崩，击穿问题
         * 1. 空结果缓存 ---》 穿透
         * 2. 设置过期时间（加随机值） ---》 雪崩
         * 3. 加锁 ---》 击穿
         */
        //1. 查询缓存
        String categoryJson = stringRedisTemplate.opsForValue().get("categoryJson");

        //2. 判断缓存中是否存在所需数据
        if (StringUtils.isEmpty(categoryJson)) {
            //若缓存中没有，则查询数据库
            System.out.println("缓存不命中，将要查询数据库！");
            /**
             * 加锁
             * 1.Redisson
             * 2.Redis
             * 3.本地锁
             */
            Map<String, List<Category2Vo>> categoryMapFromDB = getCategoryJsonFromDBWithRedissonLock();
//            Map<String, List<Category2Vo>> categoryMapFromDB = getCategoryJsonFromDBWithRedisLock();
//            Map<String, List<Category2Vo>> categoryMapFromDB = getCategoryJsonFromDBWithLocalLock();
            return categoryMapFromDB;
        }
        //3. 若缓存中存在，将Json数据转为所需的对象类型，并直接返回
        System.out.println("缓存命中，直接返回结果！");
        TypeReference<Map<String, List<Category2Vo>>> mapTypeReference = new TypeReference<Map<String, List<Category2Vo>>>() {
        };
        Map<String, List<Category2Vo>> categoryMapFromCache = JSON.parseObject(categoryJson, mapTypeReference);
        return categoryMapFromCache;
    }

    /***
     * 分布式锁 - Redisson
     * 整合Redisson加锁，调用相关方法，执行业务逻辑
     * @return
     */
    public Map<String, List<Category2Vo>> getCategoryJsonFromDBWithRedissonLock() {
        /**
         * 加锁
         * 注意锁的名字，锁的粒度越细，业务运行的越快。
         */
        RLock lock = redissonClient.getLock("category-lock");
        lock.lock();
        System.out.println("成功获取分布式锁！");
        Map<String, List<Category2Vo>> categoryMap;
        try {
            //执行业务逻辑
            categoryMap = getCategoryMap();
        } finally {
            //解锁
            lock.unlock();
        }
        return categoryMap;
    }

    /***
     * 分布式锁 - Redis
     * 利用Redis，自行编写代码，加锁，调用相关方法，执行业务逻辑
     * @return
     */
    public Map<String, List<Category2Vo>> getCategoryJsonFromDBWithRedisLock() {
        /**
         * 首先，抢占分布式锁，同时设置过期时间,uuid
         */
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if (lock){
            /**
             * 加锁成功 ---> 执行业务逻辑，得到返回结果 ---> 解锁(确认删除的是自己的锁) ---> 返回数据
             * lua脚本解锁（原子操作）
             */
            System.out.println("成功获取分布式锁！");
            Map<String, List<Category2Vo>> categoryMap;
            try{
                categoryMap = getCategoryMap();
            }finally {
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script,Long.class),Arrays.asList("lock"),uuid);
            }
            return categoryMap;
        }else {
            /**
             * 加锁失败，休眠200ms重试（自旋）
             */
            System.out.println("获取分布式锁失败，等待重试！");
            try {
                Thread.sleep(200);
            }catch (Exception e){

            }
            return getCategoryJsonFromDBWithRedisLock();
        }
    }

    /***
     * 本地锁
     * 调用相关方法，执行业务逻辑
     * @return
     */
    public Map<String, List<Category2Vo>> getCategoryJsonFromDBWithLocalLock() {
        synchronized (this) {
            //执行业务逻辑
            return getCategoryMap();
        }
    }

    /***
     * 抽取的公共方法
     * 获取三级分类数据，封装成指定格式的Map，将结果放入缓存并返回
     * @return
     */
    private Map<String, List<Category2Vo>> getCategoryMap() {
        //加锁后，要去缓存中再次确定
        String categoryJson = stringRedisTemplate.opsForValue().get("categoryJson");

        //若缓存中存在，将Json数据转为所需的对象类型，并直接返回
        if (!StringUtils.isEmpty(categoryJson)) {
            TypeReference<Map<String, List<Category2Vo>>> mapTypeReference = new TypeReference<Map<String, List<Category2Vo>>>() {
            };
            Map<String, List<Category2Vo>> categoryMapFromCache = JSON.parseObject(categoryJson, mapTypeReference);
            return categoryMapFromCache;
        }

        //若缓存中没有，继续执行业务逻辑
        //1.一次性查询出所有分类categoryList
        List<CategoryEntity> categoryList = baseMapper.selectList(null);

        //2.调用方法，在categoryList集合中得到一级分类集合
        List<CategoryEntity> categoryOneList = getCategoryXList(categoryList, 0L);

        //3.封装数据
        Map<String, List<Category2Vo>> categoryMap = categoryOneList.stream().collect(Collectors.toMap(key -> key.getCategoryId().toString(), value -> {
            //调用方法，根据父ID，在categoryList集合中得到对应的二级分类集合
            List<CategoryEntity> categoryTwoList = getCategoryXList(categoryList, value.getCategoryId());

            //封装，得到Category2Vo的形式
            List<Category2Vo> category2Vos = null;
            if (categoryTwoList != null) {
                category2Vos = categoryTwoList.stream().map(categoryTwo -> {
                    Category2Vo category2Vo = new Category2Vo(
                            value.getCategoryId().toString(),
                            null,
                            categoryTwo.getCategoryId().toString(),
                            categoryTwo.getName());
                    //调用方法，根据父ID，在categoryList集合中得到对应的三级分类集合
                    List<CategoryEntity> categoryThreeList = getCategoryXList(categoryList, categoryTwo.getCategoryId());

                    //封装，得到Category3Vo的形式
                    if (categoryThreeList != null) {
                        List<Category2Vo.Category3Vo> category3Vos = categoryThreeList.stream().map(categoryThree -> {
                            Category2Vo.Category3Vo category3Vo = new Category2Vo.Category3Vo(
                                    categoryTwo.getCategoryId().toString(),
                                    categoryThree.getCategoryId().toString(),
                                    categoryThree.getName()
                            );
                            return category3Vo;
                        }).collect(Collectors.toList());
                        category2Vo.setCategory3List(category3Vos);
                    }
                    return category2Vo;
                }).collect(Collectors.toList());
            }
            return category2Vos;
        }));
        System.out.println("查询数据库，获取三级分类数据！");

        //将查询到的数据转为Json，再放入缓存(设置过期时间1天)，并返回
        String s = JSON.toJSONString(categoryMap);
        stringRedisTemplate.opsForValue().set("categoryJson", s, 1, TimeUnit.DAYS);
        return categoryMap;
    }

    /***
     * 抽取的公共方法
     * 在所有分类的集合中，根据父ID进行查询，得到所需的分类集合
     * @param categoryList
     * @param parent_cid
     * @return
     */
    private List<CategoryEntity> getCategoryXList(List<CategoryEntity> categoryList, Long parent_cid) {
        List<CategoryEntity> collect = categoryList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }

}