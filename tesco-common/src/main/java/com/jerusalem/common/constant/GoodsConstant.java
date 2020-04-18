package com.jerusalem.common.constant;

/****
 * @Author: jerusalem
 * @Description: GoodsConstant
 * 商品微服务相关常量
 * @Date 2020/4/16 10:00
 *****/
public class GoodsConstant {

    public enum  AttrEnum{

        /**
         * 属性类型
         */
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnum(int code,String msg){
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
}
