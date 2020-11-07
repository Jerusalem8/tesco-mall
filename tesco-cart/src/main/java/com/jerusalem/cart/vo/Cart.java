package com.jerusalem.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/****
 * @Author: jerusalem
 * @Description: Cart
 * 购物车
 * 需要自定义计算属性（重写get方法）
 * @Date 2020/9/30 18:17
 *****/
public class Cart {

    /**
     * 尚品项
     * 商品总数量
     * 商品总类型数量
     * 总价格
     * 减免价格
     */
    List<CartItem> cartItems;
    private Integer countNum;
    private Integer countType;
    private BigDecimal totalAmount;
    private BigDecimal reduce = new BigDecimal("0.00");

    /**
     * 自定义计算
     * @return
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getCountNum() {
        int count = 0;
        if (cartItems != null && cartItems.size()>0){
            for (CartItem cartItem : cartItems) {
                count+= cartItem.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if (cartItems != null && cartItems.size()>0){
            for (CartItem cartItem : cartItems) {
                count++;
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        //1.计算总价
        if (cartItems != null && cartItems.size()>0){
            for (CartItem cartItem : cartItems) {
                if (cartItem.getCheck()){
                    BigDecimal totalPrice = cartItem.getTotalPrice();
                    amount = amount.add(totalPrice);
                }
            }
        }
        //2.减去优惠
        BigDecimal totalAmount = amount.subtract(getReduce());
        return totalAmount;
    }


    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
