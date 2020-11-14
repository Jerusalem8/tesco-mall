package com.jerusalem.common.enume;

/****
 * @Author: jerusalem
 * @Description: OrderCodeEnume
 * 提交订单的状态码
 ****/
public enum OrderCodeEnume {

    TOKEN_EXCEPTION(1,"令牌验证失败，订单信息过期，请重新提交订单"),
    PRICE_EXCEPTION(2,"价格验证失败，订单商品价格发生变化，请确认后重新提交订单"),
    STOCK_EXCEPTION(3,"商品库存不足，请重新选择商品提交订单");

    private int code;
    private String msg;
    OrderCodeEnume(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
