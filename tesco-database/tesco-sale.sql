/*==============================================================*/
/* Database: tesco-sale                                           */
/*==============================================================*/

drop table if exists coupon;

drop table if exists coupon_history;

drop table if exists coupon_spu_category_relation;

drop table if exists coupon_spu_relation;

drop table if exists home_adv;

drop table if exists home_subject;

drop table if exists home_subject_spu;

drop table if exists user_price;

drop table if exists seckill_promotion;

drop table if exists seckill_session;

drop table if exists seckill_sku_notice;

drop table if exists seckill_sku_relation;

drop table if exists sku_full_reduction;

drop table if exists sku_ladder;

drop table if exists spu_bounds;

/*==============================================================*/
/* Table: coupon                                            */
/*==============================================================*/
create table coupon
(
   id                   bigint not null auto_increment comment 'id',
   coupon_type          tinyint(1) comment '优惠卷类型[0->全场赠券；1->用户赠券；2->购物赠券；3->注册赠券]',
   coupon_img           varchar(2000) comment '优惠券图片',
   coupon_name          varchar(100) comment '优惠卷名字',
   num                  int comment '数量',
   amount               decimal(18,4) comment '金额',
   per_limit            int comment '每人限领张数',
   min_point            decimal(18,4) comment '使用门槛',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   use_type             tinyint(1) comment '使用类型[0->全场通用；1->指定分类；2->指定商品]',
   note                 varchar(200) comment '备注',
   publish_count        int(11) comment '发行数量',
   use_count            int(11) comment '已使用数量',
   receive_count        int(11) comment '领取数量',
   enable_start_time    datetime comment '可以领取的开始日期',
   enable_end_time      datetime comment '可以领取的结束日期',
   code                 varchar(64) comment '优惠码',
   user_level         tinyint(1) comment '可以领取的用户等级[0->不限等级，其他-对应等级]',
   publish              tinyint(1) comment '发布状态[0-未发布，1-已发布]',
   primary key (id)
);

alter table coupon comment '优惠券信息';

/*==============================================================*/
/* Table: coupon_history                                    */
/*==============================================================*/
create table coupon_history
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   user_id            bigint comment '用户id',
   user_nick_name     varchar(64) comment '用户名字',
   get_type             tinyint(1) comment '获取方式[0->后台赠送；1->主动领取]',
   create_time          datetime comment '创建时间',
   use_type             tinyint(1) comment '使用状态[0->未使用；1->已使用；2->已过期]',
   use_time             datetime comment '使用时间',
   order_id             bigint comment '订单id',
   order_sn             bigint comment '订单号',
   primary key (id)
);

alter table coupon_history comment '优惠券领取历史记录';

/*==============================================================*/
/* Table: coupon_spu_category_relation                      */
/*==============================================================*/
create table coupon_spu_category_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   category_id          bigint comment '产品分类id',
   category_name        varchar(64) comment '产品分类名称',
   primary key (id)
);

alter table coupon_spu_category_relation comment '优惠券分类关联';

/*==============================================================*/
/* Table: coupon_spu_relation                               */
/*==============================================================*/
create table coupon_spu_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment '优惠券id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   primary key (id)
);

alter table coupon_spu_relation comment '优惠券与产品关联';

/*==============================================================*/
/* Table: home_adv                                          */
/*==============================================================*/
create table home_adv
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(100) comment '名字',
   pic                  varchar(500) comment '图片地址',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   status               tinyint(1) comment '状态',
   click_count          int comment '点击数',
   url                  varchar(500) comment '广告详情连接地址',
   note                 varchar(500) comment '备注',
   sort                 int comment '排序',
   publisher_id         bigint comment '发布者',
   auth_id              bigint comment '审核者',
   primary key (id)
);

alter table home_adv comment '首页轮播广告';

/*==============================================================*/
/* Table: home_subject                                      */
/*==============================================================*/
create table home_subject
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '专题名字',
   title                varchar(255) comment '专题标题',
   sub_title            varchar(255) comment '专题副标题',
   status               tinyint(1) comment '显示状态',
   url                  varchar(500) comment '详情连接',
   sort                 int comment '排序',
   img                  varchar(500) comment '专题图片地址',
   primary key (id)
);

alter table home_subject comment '首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】';

/*==============================================================*/
/* Table: home_subject_spu                                  */
/*==============================================================*/
create table home_subject_spu
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '专题名字',
   subject_id           bigint comment '专题id',
   spu_id               bigint comment 'spu_id',
   sort                 int comment '排序',
   primary key (id)
);

alter table home_subject_spu comment '专题商品';

/*==============================================================*/
/* Table: user_price                                      */
/*==============================================================*/
create table user_price
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   user_level_id      bigint comment '用户等级id',
   user_level_name    varchar(100) comment '用户等级名',
   user_price         decimal(18,4) comment '用户对应价格',
   add_other            tinyint(1) comment '可否叠加其他优惠[0-不可叠加优惠，1-可叠加]',
   primary key (id)
);

alter table user_price comment '商品用户价格';

/*==============================================================*/
/* Table: seckill_promotion                                 */
/*==============================================================*/
create table seckill_promotion
(
   id                   bigint not null auto_increment comment 'id',
   title                varchar(255) comment '活动标题',
   start_time           datetime comment '开始日期',
   end_time             datetime comment '结束日期',
   status               tinyint comment '上下线状态',
   create_time          datetime comment '创建时间',
   user_id              bigint comment '创建人',
   primary key (id)
);

alter table seckill_promotion comment '秒杀活动';

/*==============================================================*/
/* Table: seckill_session                                   */
/*==============================================================*/
create table seckill_session
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment '场次名称',
   start_time           datetime comment '每日开始时间',
   end_time             datetime comment '每日结束时间',
   status               tinyint(1) comment '启用状态',
   create_time          datetime comment '创建时间',
   primary key (id)
);

alter table seckill_session comment '秒杀活动场次';

/*==============================================================*/
/* Table: seckill_sku_notice                                */
/*==============================================================*/
create table seckill_sku_notice
(
   id                   bigint not null auto_increment comment 'id',
   user_id            bigint comment 'user_id',
   sku_id               bigint comment 'sku_id',
   session_id           bigint comment '活动场次id',
   subcribe_time        datetime comment '订阅时间',
   send_time            datetime comment '发送时间',
   notice_type          tinyint(1) comment '通知方式[0-短信，1-邮件]',
   primary key (id)
);

alter table seckill_sku_notice comment '秒杀商品通知订阅';

/*==============================================================*/
/* Table: seckill_sku_relation                              */
/*==============================================================*/
create table seckill_sku_relation
(
   id                   bigint not null auto_increment comment 'id',
   promotion_id         bigint comment '活动id',
   promotion_session_id bigint comment '活动场次id',
   sku_id               bigint comment '商品id',
   seckill_price        decimal comment '秒杀价格',
   seckill_count        decimal comment '秒杀总量',
   seckill_limit        decimal comment '每人限购数量',
   seckill_sort         int comment '排序',
   primary key (id)
);

alter table seckill_sku_relation comment '秒杀活动商品关联';

/*==============================================================*/
/* Table: sku_full_reduction                                */
/*==============================================================*/
create table sku_full_reduction
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_price           decimal(18,4) comment '满多少',
   reduce_price         decimal(18,4) comment '减多少',
   add_other            tinyint(1) comment '是否参与其他优惠',
   primary key (id)
);

alter table sku_full_reduction comment '商品满减信息';

/*==============================================================*/
/* Table: sku_ladder                                        */
/*==============================================================*/
create table sku_ladder
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_count           int comment '满几件',
   discount             decimal(4,2) comment '打几折',
   price                decimal(18,4) comment '折后价',
   add_other            tinyint(1) comment '是否叠加其他优惠[0-不可叠加，1-可叠加]',
   primary key (id)
);

alter table sku_ladder comment '商品阶梯价格';

/*==============================================================*/
/* Table: spu_bounds                                        */
/*==============================================================*/
create table spu_bounds
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint,
   grow_bounds          decimal(18,4) comment '成长积分',
   buy_bounds           decimal(18,4) comment '购物积分',
   work                 tinyint(1) comment '优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]',
   primary key (id)
);

alter table spu_bounds comment '商品spu积分设置';
