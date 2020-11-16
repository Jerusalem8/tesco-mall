/*
 Navicat Premium Data Transfer

 Source Server         : Docker-MySQL
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 192.168.75.136:3306
 Source Schema         : tesco-user

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 16/11/2020 20:53:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for growth_change_history
-- ----------------------------
DROP TABLE IF EXISTS `growth_change_history`;
CREATE TABLE `growth_change_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user_id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `change_count` int(11) NULL DEFAULT NULL COMMENT '改变的值（正负计数）',
  `note` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_type` tinyint(4) NULL DEFAULT NULL COMMENT '积分来源[0-购物，1-管理员修改]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '成长值变化历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for integration_change_history
-- ----------------------------
DROP TABLE IF EXISTS `integration_change_history`;
CREATE TABLE `integration_change_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user_id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `change_count` int(11) NULL DEFAULT NULL COMMENT '变化的值',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_tyoe` tinyint(4) NULL DEFAULT NULL COMMENT '来源[0->购物；1->管理员修改;2->活动]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分变化历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_collect_spu
-- ----------------------------
DROP TABLE IF EXISTS `user_collect_spu`;
CREATE TABLE `user_collect_spu`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_name',
  `spu_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'spu_img',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏的商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_collect_subject
-- ----------------------------
DROP TABLE IF EXISTS `user_collect_subject`;
CREATE TABLE `user_collect_subject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` bigint(20) NULL DEFAULT NULL COMMENT 'subject_id',
  `subject_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'subject_name',
  `subject_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'subject_img',
  `subject_urll` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏的专题活动' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_level
-- ----------------------------
DROP TABLE IF EXISTS `user_level`;
CREATE TABLE `user_level`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '等级名称',
  `growth_point` int(11) NULL DEFAULT NULL COMMENT '等级需要的成长值',
  `default_status` tinyint(4) NULL DEFAULT NULL COMMENT '是否为默认等级[0->不是；1->是]',
  `free_freight_point` decimal(18, 4) NULL DEFAULT NULL COMMENT '免运费标准',
  `comment_growth_point` int(11) NULL DEFAULT NULL COMMENT '每次评价获取的成长值',
  `priviledge_free_freight` tinyint(4) NULL DEFAULT NULL COMMENT '是否有免邮特权',
  `priviledge_user_price` tinyint(4) NULL DEFAULT NULL COMMENT '是否有用户价格特权',
  `priviledge_birthday` tinyint(4) NULL DEFAULT NULL COMMENT '是否有生日特权',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户等级' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user_id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'city',
  `login_type` tinyint(1) NULL DEFAULT NULL COMMENT '登录类型[1-web，2-app]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_receive_address
-- ----------------------------
DROP TABLE IF EXISTS `user_receive_address`;
CREATE TABLE `user_receive_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user_id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份/直辖市',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址(街道)',
  `areacode` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省市区代码',
  `default_status` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_statistics_info
-- ----------------------------
DROP TABLE IF EXISTS `user_statistics_info`;
CREATE TABLE `user_statistics_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `consume_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '累计消费金额',
  `coupon_amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '累计优惠金额',
  `order_count` int(11) NULL DEFAULT NULL COMMENT '订单数量',
  `coupon_count` int(11) NULL DEFAULT NULL COMMENT '优惠券数量',
  `comment_count` int(11) NULL DEFAULT NULL COMMENT '评价数',
  `return_order_count` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `login_count` int(11) NULL DEFAULT NULL COMMENT '登录次数',
  `attend_count` int(11) NULL DEFAULT NULL COMMENT '关注数量',
  `fans_count` int(11) NULL DEFAULT NULL COMMENT '粉丝数量',
  `collect_product_count` int(11) NULL DEFAULT NULL COMMENT '收藏的商品数量',
  `collect_subject_count` int(11) NULL DEFAULT NULL COMMENT '收藏的专题活动数量',
  `collect_comment_count` int(11) NULL DEFAULT NULL COMMENT '收藏的评论数量',
  `invite_friend_count` int(11) NULL DEFAULT NULL COMMENT '邀请的朋友数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户统计信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `level_id` bigint(20) NULL DEFAULT NULL COMMENT '用户等级id',
  `username` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `header` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `city` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在城市',
  `job` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职业',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `source_type` tinyint(4) NULL DEFAULT NULL COMMENT '用户来源',
  `integration` int(11) NULL DEFAULT NULL COMMENT '积分',
  `growth` int(11) NULL DEFAULT NULL COMMENT '成长值',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '启用状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `social_uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '社交用户id',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问令牌',
  `expires_in` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '令牌过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
