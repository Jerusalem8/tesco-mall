package com.jerusalem.cart.service.impl;

import com.jerusalem.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/****
 * @Author: jerusalem
 * @Description: CartServiceImpl
 * @Date 2020/9/30 18:57
 *****/
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;
}
