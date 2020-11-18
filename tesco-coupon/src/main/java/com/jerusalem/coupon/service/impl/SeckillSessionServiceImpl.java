package com.jerusalem.coupon.service.impl;

import com.jerusalem.coupon.dao.SeckillSessionDao;
import com.jerusalem.coupon.entity.SeckillSessionEntity;
import com.jerusalem.coupon.entity.SeckillSkuRelationEntity;
import com.jerusalem.coupon.service.SeckillSessionService;
import com.jerusalem.coupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jerusalem.common.utils.PageUtils;
import com.jerusalem.common.utils.Query;


/****
 * 服务层接口实现类
 * 秒杀活动场次
 * @author jerusalem
 * @email 3276586184@qq.com
 * @date 2020-04-25 13:53:20
 */
@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    SeckillSkuRelationService seckillSkuRelationService;


    /**
    * 分页查询
    * @param params
    * @return
    */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );
        return new PageUtils(page);
    }

    /***
     * 获取最近三天的秒杀商品
     * 重点：时间日期处理
     * @return
     */
    @Override
    public List<SeckillSessionEntity> getLatest3DaysSession() {
        String startTime = startTime();
        String endTime = endTime();
        List<SeckillSessionEntity> list = this.list(
                new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime,endTime));
        if (list != null && list.size()>0){
            List<SeckillSessionEntity> collect = list.stream().map(session -> {
                Long id = session.getId();
                List<SeckillSkuRelationEntity> relationList = seckillSkuRelationService.list(
                        new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", id));
                session.setRelationSkus(relationList);
                return session;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    /***
     * 获取起始时间
     * @return
     */
    private String startTime(){
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime of = LocalDateTime.of(now, min);
        String start = of.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return start;
    }

    /***
     * 获取结束时间
     * @return
     */
    private String endTime(){
        LocalDate now = LocalDate.now();
        LocalDate plusDays = now.plusDays(2);
        LocalTime max = LocalTime.MAX;
        LocalDateTime of = LocalDateTime.of(plusDays, max);
        String end = of.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return end;
    }
}