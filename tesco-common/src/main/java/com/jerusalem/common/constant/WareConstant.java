package com.jerusalem.common.constant;

/****
 * @Author: jerusalem
 * @Description: GoodsConstant
 * 仓储微服务相关常量
 * 采购单的状态：0-新建
 *             1-已分配
 *             2-已领取
 *             3-已完成
 *             4-有异常
 * 采购需求的状态：0-新建
 *               1-已分配
 *               2-正在采购
 *               3-采购完成
 *               4-采购失败
 * @Date 2020/4/16 10:00
 *****/
public class WareConstant {

    public enum  PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private String msg;

        PurchaseStatusEnum(int code,String msg){
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

    public enum  PurchaseDetailStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),FINISH(3,"采购完成"),
        HASERROR(4,"采购失败");
        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
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
