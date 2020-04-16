/*==============================================================*/
/* Database: tesco-ware                                          */
/*==============================================================*/

drop table if exists purchase;

drop table if exists purchase_detail;

drop table if exists ware_info;

drop table if exists ware_order_task;

drop table if exists ware_order_task_detail;

drop table if exists ware_sku;

/*==============================================================*/
/* Table: purchase                                          */
/*==============================================================*/
create table purchase
(
   id                   bigint not null auto_increment comment '采购单id',
   assignee_id          bigint comment '采购人id',
   assignee_name        varchar(255) comment '采购人名',
   phone                char(13) comment '联系方式',
   priority             int(4) comment '优先级',
   status               int(4) comment '状态',
   ware_id              bigint comment '仓库id',
   amount               decimal(18,4) comment '总金额',
   create_time          datetime comment '创建日期',
   update_time          datetime comment '更新日期',
   primary key (id)
);

alter table purchase comment '采购信息';

/*==============================================================*/
/* Table: purchase_detail                                   */
/*==============================================================*/
create table purchase_detail
(
   id                   bigint not null auto_increment,
   purchase_id          bigint comment '采购单id',
   sku_id               bigint comment '采购商品id',
   sku_num              int comment '采购数量',
   sku_price            decimal(18,4) comment '采购金额',
   ware_id              bigint comment '仓库id',
   status               int comment '状态[0新建，1已分配，2正在采购，3已完成，4采购失败]',
   primary key (id)
);

/*==============================================================*/
/* Table: ware_info                                         */
/*==============================================================*/
create table ware_info
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(255) comment '仓库名',
   address              varchar(255) comment '仓库地址',
   areacode             varchar(20) comment '区域编码',
   primary key (id)
);

alter table ware_info comment '仓库信息';

/*==============================================================*/
/* Table: ware_order_task                                   */
/*==============================================================*/
create table ware_order_task
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   order_sn             varchar(255) comment 'order_sn',
   consignee            varchar(100) comment '收货人',
   consignee_tel        char(15) comment '收货人电话',
   delivery_address     varchar(500) comment '配送地址',
   order_comment        varchar(200) comment '订单备注',
   payment_way          tinyint(1) comment '付款方式【 1:在线付款 2:货到付款】',
   task_status          tinyint(2) comment '任务状态',
   order_body           varchar(255) comment '订单描述',
   tracking_no          char(30) comment '物流单号',
   create_time          datetime comment 'create_time',
   ware_id              bigint comment '仓库id',
   task_comment         varchar(500) comment '工作单备注',
   primary key (id)
);

alter table ware_order_task comment '库存工作单';

/*==============================================================*/
/* Table: ware_order_task_detail                            */
/*==============================================================*/
create table ware_order_task_detail
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   sku_name             varchar(255) comment 'sku_name',
   sku_num              int comment '购买个数',
   task_id              bigint comment '工作单id',
   primary key (id)
);

alter table ware_order_task_detail comment '库存工作单';

/*==============================================================*/
/* Table: ware_sku                                          */
/*==============================================================*/
create table ware_sku
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   ware_id              bigint comment '仓库id',
   stock                int comment '库存数',
   sku_name             varchar(200) comment 'sku_name',
   stock_locked         int comment '锁定库存',
   primary key (id)
);

alter table ware_sku comment '商品库存';
