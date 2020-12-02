## 乐购商城

#### 项目介绍
乐购商城（tesco-mall）是一套完善的微服务电商系统，由前台商城系统和后台管理系统构成，基于SpringBoot、SpringCloud、SpringCloud alibaba、Vue实现，采用前后端分离开发模式。前台商城系统具有首页门户、商品推荐、商品检索、商品详情、用户中心、购物车、订单流程、支付、秒杀等功能，后台管理系统具有控制面板、统计管理、商品系统、用户系统、订单系统、库存系统、优惠系统、内容管理、系统管理等模块。

涵盖Restful接口、数据校验、网关、注册发现、配置中心、熔断、限流、降级、链路追踪、性能监控、压力测试、系统预警、集群部署、持续集成、持续部署等技术点，均采用当前最流行的技术栈。
#### 项目架构
##### 系统架构图
![系统架构图](http://jerusalem01.gitee.io/images-bed/images/tesco/架构图.png)

##### 微服务划分图
![微服务划分图](http://jerusalem01.gitee.io/images-bed/images/tesco/服务划分图.png)

##### 模块说明
```text
|-- tesco
    |-- tesco-admin -- 后台管理系统后端
    |-- tesco-admin-vue -- 后台管理系统前端
    |-- tesco-api -- 各个微服务的API接口
    |   |-- tesco-cart-api
    |   |-- tesco-coupon-api
    |   |-- tesco-goods-api
    |   |-- tesco-order-api
    |   |-- tesco-search-api
    |   |-- tesco-seckill-api
    |   |-- tesco-third-api
    |   |-- tesco-user-api
    |   |-- tesco-ware-api
    |-- tesco-cart -- 购物车微服务
    |-- tesco-common -- 通用工具类
    |-- tesco-coupon -- 优惠微服务
    |-- tesco-database -- 数据库表
    |-- tesco-dependency -- 核心依赖包
    |-- tesco-gateway -- 网关微服务（分为管理员、用户、游客三个急别）
    |   |-- tesco-gateway-system
    |   |-- tesco-gateway-user
    |   |-- tesco-gateway-web
    |-- tesco-goods -- 商品微服务
    |-- tesco-oauth2 -- 认证微服务
    |-- tesco-order -- 订单微服务
    |-- tesco-search -- 检索微服务
    |-- tesco-seckill -- 秒杀微服务
    |-- tesco-third-services -- 第三方接口服务
    |-- tesco-user -- 用户微服务
    |-- tesco-ware -- 库存微服务
```
#### 技术选型
##### 后端技术
技术|说明
---|---
Spring Boot|容器+MVC框架
MyBatis|ORM框架
MyBatis Plus|MyBatis增强工具
MySql|数据库
Redis|分布式缓存
RabbitMQ|消息中间件
Elasticsearch|搜索引擎
Kibana|Elasticsearch可视化工具
LogStash|日志收集工具
Redisson|分布式锁框架
SpringCache|简化分布式缓存开发
JSR303|数据校验
Lombok|简化对象封装工具
Nginx|反向代理、限流、负载均衡、容错
Docker|虚拟化容器技术
Kubernetes|容器管理、集群部署
Jenkins|持续集成
AlipayTemplate|支付宝支付
Spring Cloud Gateway|API 网关
Spring Cloud Security+Oauth2|安全认证授权、第三方登录
Spring Cloud OpenFeign|服务消费（远程调用）
Spring Cloud Ribbon|服务消费（负载均衡）
Spring Cloud Sleuth+Zipkin|分布式链路追踪及可视化
Spring Cloud Alibaba Nacos|服务发现与注册、分布式配置中心
Spring Cloud Alibaba Sentinel|服务容错（限流、熔断、降级）
Spring Cloud Alibaba OSS|阿里云对象存储服务
Spring Cloud Alibaba Seata|分布式事务解决方案
Spring Cloud Alibaba SMS|短信服务

##### 前端技术
技术|说明
---|---
Vue|前端框架
Vue-router|路由管理器
Element UI|前端UI框架
Axios|前端HTTP框架
V-Charts|前端图表框架
HTML CSS JS|前端技术
ECMAScript 6|JavaScript语言标准
JQuery|JS插件库
Thymeleaf|模板引擎

#### 环境搭建
##### 开发环境
工具|版本号
---|---
JDK|1.8
MySql|5.7.22
Redis|5.0
RabbitMQ|3.8.2
Elasticsearch|7.4.2
Kibana|7.4.2
LogStash|7.4.2
Nginx|1.17.10
Docker|19.03.05
Zipkin|2.22.2
Spring Cloud Alibaba Nacos|1.1.14
Spring Cloud Alibaba Sentinel|1.8.0
Spring Cloud Alibaba Seata|0.9.0

##### 开发工具
工具|说明
---|---
IDEA|Java代码编译环境
VsCode|前端代码编辑器
VMware|虚拟机管理
Navicat|数据库可视化工具
RedisManager|缓存可视化工具
Postman|接口调试工具
Xshell|Linux远程连接工具
Xftp|数据传输工具
SwitchHosts|本地Host管理
ApacheJMeter|压力测试工具
花生壳|内网穿透工具
Notepad|好用的记事本

##### 搭建步骤

#### 效果演示
##### 后台管理系统

- **登录页**
![登录页](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211901.png)

- **统计管理**
![统计管理](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211939.png)

- **品牌管理**
![品牌管理](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122212011.png)

- **属性管理**
![属性管理](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122212040.png)

- **商品管理**
![商品管理](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122212114.png)

- **发布商品**
![登录页](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122212134.png)

- **秒杀系统**
![秒杀系统](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122212343.png)

##### 前台商城系统

- **首页门户**
![首页门户](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211436.png)

- **商品搜索**
![商品搜索](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211519.png)

- **购物车**
![购物车](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211558.png)

- **结算页**
![结算页](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211749.png)

- **支付页**
![支付页](http://jerusalem01.gitee.io/images-bed/images/tesco/QQ图片20201122211820.png)


#### 友情链接

- **[个人博客](http://www.nm83.com)**

- **[码云](https://gitee.com/jerusalem01/tesco)**

- **[GitHub](https://github.com/Jerusalem01/tesco-mall)**

#### 参与贡献

- **感谢[人人开源](https://gitee.com/renrenio)，后台管理系统部分基于[renren-fast](https://gitee.com/renrenio/renren-fast)+[renren-fast-vue](https://gitee.com/renrenio/renren-fast-vue)进行快速二次开发。**