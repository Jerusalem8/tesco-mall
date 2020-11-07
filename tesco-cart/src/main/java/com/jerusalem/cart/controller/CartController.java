package com.jerusalem.cart.controller;

import com.jerusalem.cart.interceptor.CartInterceptor;
import com.jerusalem.cart.service.CartService;
import com.jerusalem.cart.vo.Cart;
import com.jerusalem.cart.vo.CartItem;
import com.jerusalem.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/****
 * @Author: jerusalem
 * @Description: CartController
 * @Date 2020/9/30 19:07
 *****/
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /***
     * 获取当前登录用户的所有选中的购物项
     * @return
     */
    @GetMapping("/cartItems")
    @ResponseBody       //返回JSON数据
    public List<CartItem> getUserCartItems(){
        List<CartItem> cartItems = cartService.getUserCartItems();
        return cartItems;
    }

    /***
     * 获取购物车页面
     * 关于临时购物车、用户购物车的做法
     * 浏览器有一个cookie:user-key:标识用户唯一身份（有效期为一个月）
     * ThreadLocal（用户身份鉴别） - 同一个线程共享数据
     * @return
     */
    @GetMapping("/cartList.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /***
     * 添加商品到购物车
     * RedirectAttributes：重定向携带数据
     *              addFlashAttribute:将数据放在Session中可以在页面取出，但是只能取一次
     *              addAttribute：将数据放在URL后
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num")Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        //调用方法，添加到购物车，暂不展示数据
        cartService.addToCart(skuId,num);
        attributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.tesco.com/addToCartSuccessPage.html";
    }

    /***
     * 跳转到成功页（与添加购物车操作分离开）
     * 实现：页面刷新时，只是再次查询购物车中的信息，不会再次添加
     * @return
     */
    @GetMapping("/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        //重定向到成功页面，再次查询购物车数据
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item",cartItem);
        return "success";
    }

    /***
     * 购物项选中状态的改变
     * @param skuId
     * @param check
     * @return
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("check") Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.tesco.com/cartList.html";
    }

    /***
     * 购物项数量的改变
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.tesco.com/cartList.html";
    }

    /***
     * 删除购物项
     * @param skuId
     * @return
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.tesco.com/cartList.html";
    }
}
