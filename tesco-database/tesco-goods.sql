/*
 Navicat Premium Data Transfer

 Source Server         : Docker-MySQL
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 192.168.75.136:3306
 Source Schema         : tesco-goods

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 16/11/2020 20:52:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attr
-- ----------------------------
DROP TABLE IF EXISTS `attr`;
CREATE TABLE `attr`  (
  `attr_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `attr_name` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `search_type` tinyint(4) NULL DEFAULT NULL COMMENT '是否需要检索[0-不需要，1-需要]',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性图标',
  `value_select` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可选值列表[用逗号分隔]',
  `attr_type` tinyint(4) NULL DEFAULT NULL COMMENT '属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]',
  `enable` bigint(20) NULL DEFAULT NULL COMMENT '启用状态[0 - 禁用，1 - 启用]',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属分类',
  `show_desc` tinyint(4) NULL DEFAULT NULL COMMENT '快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整',
  `value_type` tinyint(4) NULL DEFAULT NULL COMMENT '值类型（1-允许多个，2-只能一个值）',
  PRIMARY KEY (`attr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attr_attr_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `attr_attr_group_relation`;
CREATE TABLE `attr_attr_group_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `attr_id` bigint(20) NULL DEFAULT NULL COMMENT '属性id',
  `attr_group_id` bigint(20) NULL DEFAULT NULL COMMENT '属性分组id',
  `attr_sort` int(11) NULL DEFAULT NULL COMMENT '属性组内排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '属性&属性分组关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attr_group
-- ----------------------------
DROP TABLE IF EXISTS `attr_group`;
CREATE TABLE `attr_group`  (
  `attr_group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组id',
  `attr_group_name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组图标',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属分类id',
  PRIMARY KEY (`attr_group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '属性分组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
  `brand_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名',
  `logo` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌logo地址',
  `descript` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  `show_status` tinyint(4) NULL DEFAULT NULL COMMENT '显示状态[0-不显示；1-显示]',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检索首字母',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `parent_cid` bigint(20) NULL DEFAULT NULL COMMENT '父分类id',
  `category_level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `show_status` tinyint(4) NULL DEFAULT NULL COMMENT '是否显示[0-不显示，1显示]',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `icon` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标地址',
  `product_unit` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `product_count` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1430 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品三级分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category_brand_relation
-- ----------------------------
DROP TABLE IF EXISTS `category_brand_relation`;
CREATE TABLE `category_brand_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌分类关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `comment_reply`;
CREATE TABLE `comment_reply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment_id` bigint(20) NULL DEFAULT NULL COMMENT '评论id',
  `reply_id` bigint(20) NULL DEFAULT NULL COMMENT '回复id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品评价回复关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `product_attr_value`;
CREATE TABLE `product_attr_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `attr_id` bigint(20) NULL DEFAULT NULL COMMENT '属性id',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `attr_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `attr_sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `quick_show` tinyint(4) NULL DEFAULT NULL COMMENT '快速展示【是否展示在介绍上；0-否 1-是】',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu属性值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sku_images
-- ----------------------------
DROP TABLE IF EXISTS `sku_images`;
CREATE TABLE `sku_images`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `img_sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `default_img` int(11) NULL DEFAULT NULL COMMENT '默认图[0 - 不是默认图，1 - 是默认图]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sku_info
-- ----------------------------
DROP TABLE IF EXISTS `sku_info`;
CREATE TABLE `sku_info`  (
  `sku_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'skuId',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spuId',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `sku_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku介绍描述',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属分类id',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
  `sku_default_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认图片',
  `sku_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `sku_subtitle` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `price` decimal(18, 4) NULL DEFAULT NULL COMMENT '价格',
  `sale_count` bigint(20) NULL DEFAULT NULL COMMENT '销量',
  PRIMARY KEY (`sku_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sku_sale_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `sku_sale_attr_value`;
CREATE TABLE `sku_sale_attr_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `attr_id` bigint(20) NULL DEFAULT NULL COMMENT 'attr_id',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售属性名',
  `attr_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售属性值',
  `attr_sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku销售属性&值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_comment
-- ----------------------------
DROP TABLE IF EXISTS `spu_comment`;
CREATE TABLE `spu_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名字',
  `member_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员昵称',
  `star` tinyint(1) NULL DEFAULT NULL COMMENT '星级',
  `member_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员ip',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `show_status` tinyint(1) NULL DEFAULT NULL COMMENT '显示状态[0-不显示，1-显示]',
  `spu_attributes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买时属性组合',
  `likes_count` int(11) NULL DEFAULT NULL COMMENT '点赞数',
  `reply_count` int(11) NULL DEFAULT NULL COMMENT '回复数',
  `resources` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论图片/视频[json数据；[{type:文件类型,url:资源路径}]]',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `member_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `comment_type` tinyint(4) NULL DEFAULT NULL COMMENT '评论类型[0 - 对商品的直接评论，1 - 对评论的回复]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品评价' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_images
-- ----------------------------
DROP TABLE IF EXISTS `spu_images`;
CREATE TABLE `spu_images`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `img_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片名',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `img_sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `default_img` tinyint(4) NULL DEFAULT NULL COMMENT '是否默认图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_info
-- ----------------------------
DROP TABLE IF EXISTS `spu_info`;
CREATE TABLE `spu_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spu_description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属分类id',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
  `weight` decimal(18, 4) NULL DEFAULT NULL,
  `publish_status` tinyint(4) NULL DEFAULT NULL COMMENT '上架状态[0 - 下架，1 - 上架]',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_info_desc
-- ----------------------------
DROP TABLE IF EXISTS `spu_info_desc`;
CREATE TABLE `spu_info_desc`  (
  `spu_id` bigint(20) NOT NULL COMMENT '商品id',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品介绍',
  PRIMARY KEY (`spu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu信息介绍' ROW_FORMAT = Dynamic;

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

SET FOREIGN_KEY_CHECKS = 1;
