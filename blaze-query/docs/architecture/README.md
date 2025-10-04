# 架构设计详解

## 项目概述

Velocity Data Domain 是一个专门针对高并发架构进行合理优化的框架。它基于领域驱动设计（DDD）原则，通过深入分析数据访问模式和业务场景，提供了一套完整的高性能数据处理方案。

在典型的高并发场景中，如电商秒杀、实时排行榜、热点数据查询等，传统的关系型数据库往往成为系统瓶颈。本项目通过一系列优化策略和技术手段，在保证数据一致性和系统可靠性的前提下，最大化系统吞吐量和响应速度。

## 整体架构

### 前置分析

首先我们仅针对当前场景，进行初步的流程分析:

1. 用户首先登录，获取用户信息、Token
2. 点进商品简单描述页
3. 随后选择某个商品，进入商品详情页
4. 选择购买，订单生成，支付成功

### 业务流程图

```mermaid
sequenceDiagram
    participant U as 用户
    participant FE as 前端
    participant GW as 网关/认证
    participant AUTH as 认证服务
    participant PROD as 商品服务
    participant ORDER as 订单服务
    participant PAY as 支付服务

%% 1. 登录流程
    Note over U, AUTH: 第一步:用户登录
    U->>+FE: 输入用户名/密码
    FE->>+AUTH: 发送登录请求
    AUTH-->>-FE: 返回用户信息 & Token
    FE-->>-U: 登录成功，保存Token

%% 2. 浏览商品列表
    Note over U, PROD: 第二步:浏览商品列表
    U->>+FE: 进入商品简单描述页
    FE->>+GW: 携带Token请求商品列表
    GW->>GW: 验证Token
    GW->>+PROD: 转发请求
    PROD-->>-GW: 返回商品简要列表
    GW-->>-FE: 返回数据
    FE-->>-U: 渲染展示商品列表

%% 3. 查看商品详情
    Note over U, PROD: 第三步:查看商品详情
    U->>+FE: 点击某个商品
    FE->>+GW: 携带Token请求商品详情
    GW->>GW: 验证Token
    GW->>+PROD: 转发请求
    PROD-->>-GW: 返回商品详细信息
    GW-->>-FE: 返回数据
    FE-->>-U: 渲染商品详情页

%% 4. 下单与简化支付
    Note over U, PAY: 第四步:下单与支付
    U->>+FE: 点击购买
    FE->>+GW: 携带Token提交订单
    GW->>GW: 验证Token
    GW->>+ORDER: 转发创建订单请求
    ORDER->>ORDER: 校验库存、生成订单
    ORDER-->>-GW: 返回生成的订单信息
    GW-->>-FE: 返回订单信息

    U->>+FE: 点击支付
    FE->>+PAY: 调用支付接口
    PAY->>PAY: 处理支付(1-2秒)
    PAY-->>-FE: 返回支付成功
    FE-->>-U: 显示支付成功
```

### 模块拆分

根据该流程图，我们可以直接进行模块拆分，我们采用DDD领域驱动设计，以提升模块的可迁移性:

- **认证服务**: user-auth-service 模块
- **用户个人信息服务**: user-profile-service 模块
- **商品信息服务**: product-profile-service 模块
- **订单服务**: order-service 模块
- **支付服务**: pay-service 模块