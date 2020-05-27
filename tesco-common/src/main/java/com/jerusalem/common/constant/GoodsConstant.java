package com.jerusalem.common.constant;

/****
 * @Author: jerusalem
 * @Description: GoodsConstant
 * 商品微服务相关常量
 * 属性的类型：1-基本属性（规格参数）
 *            2-销售属性
 * @Date 2020/4/16 10:00
 *****/
public class GoodsConstant {

    /**
     * 属性类型
     */
    public enum  AttrEnum{

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

    /**
     * 商品状态
     */
    public enum  StatusEnum{

        SPU_NEW(0,"新建"),
        SPU_UP(1,"上架中"),
        SPU_DOWN(2,"已下架");

        private int code;
        private String msg;

        StatusEnum(int code,String msg){
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
