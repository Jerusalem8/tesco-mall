package com.jerusalem.common.exception;

/****
 * @Author: jerusalem
 * @Description: NoStockException
 * 无库存异常
 * @Date 2020/11/10 16:35
 *****/
public class NoStockException extends RuntimeException{

    private Long skuId;

    public NoStockException(Long skuId){
        super("商品："+skuId+" "+"没有足够的库存量！");
    }
    public NoStockException(){
        super("商品库存量不足！");
    }

    public NoStockException(String msg) {
        super();
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
