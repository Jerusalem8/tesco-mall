package com.jerusalem.user.feign;

import com.jerusalem.common.utils.R;
import com.jerusalem.common.vo.UserAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/****
 * @Author: jerusalem
 * @Description: UserReceiveAddressFeign业务层接口
 * @Date 2020/11/7 13:35
 *****/
@FeignClient(name = "user-service")
@RequestMapping("user/receive/address")
public interface UserReceiveAddressFeign {

    /***
     * 根据用户id获取收货地址列表
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/address")
    List<UserAddressVo> getAddress(@PathVariable("userId")Long userId);

    /***
     * 根据id获取收货地址信息
     * @return
     */
    @GetMapping("/info/{id}")
    R info(@PathVariable("id") Long id);
}
