# Blaze Query 热点查询场景优化

## 1. 前言

1. 本项目将针对经典的热点查询场景进行一步步地优化，并力求在不过度提升复杂度的前提下，在提升性能的同时，减少当下的开发成本以及将来的维护成本
2. 本项目将针对典型的 **电商平台秒杀商品**作为例子，将从简单到困难，一步步演进，并在本文档中逐步记录，介绍如何进行优化、优化坑点

## 2. 技术栈使用前置

1. V1.0 初步版本，使用**SpringBoot + MyBatis Plus + MySQL + Lombok + Swagger**

## 3. 场景描述

1. **20:00 - 21:00** 为秒杀时刻，此时会开放单独的一个页面，作为商品的简单介绍页面，用户可手动点击进入一个商品的详情页面
2. 商品简单描述页会显示的信息是:
   - 秒杀商品列表
   - 秒杀商品标题
   - 秒杀商品图片
   - 秒杀商品描述
3. 商品详情页会显示的信息是:
   - 商品标题
   - 商品图片
   - 商品描述
   - 商品库存
   - 商品原价、秒杀价、折扣

## 4. 架构设计 v1.0

### 4.1 前置分析

1. 首先我们仅针对当前场景，进行初步的流程分析
   - 用户首先登录，获取用户信息、Token
   - 点进商品简单描述页
   - 随后选择某个商品，进入商品详情页
   - 选择购买，订单生成，支付成功

2. 随后我们画出流程图
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
   
3. 根据该流程图，我们可以直接进行模块拆分，我们采用DDD领域驱动设计，以提升模块的可迁移性
   - **认证服务**: user-auth-service 模块
   - **用户个人信息服务**: user-profile-service 模块
   - **商品信息服务**: product-profile-service 模块
   - **订单服务**: order-service 模块
   - **支付服务**: pay-service 模块

### 4.2 模型设计

1. 随后我们设计基础信息模型，再以此进行聚合根设计
    1. **认证服务**: 该服务需要进行登录日志记录，因此设计模型如下
       - **id**: 登录id
       - **user_id**: 登录用户id
       - **ip**: 登录ip
       - **login_time**: 登录时间
       - **logout_time**: 登出时间 (这里需要前端配合使用心跳检测机制，因此这里的字段仅记录用户主动登出的记录)
       
    2. **用户个人信息服务**: 该服务需要进行用户信息记录，因此设计模型如下
       - **user_id**: 用户id
       - **username**: 用户名
       - **password**: 密码
       - **phone**: 手机号
       - **email**: 邮箱
       - **display_name**: 昵称
       - **avatar**: 头像地址
       - **create_time**: 创建时间
       - **update_time**: 更新时间
   
    3. **商品信息服务**: 该服务需要进行商品信息记录，因此设计模型如下
       - **product_id**: 商品ID
       - **title**: 商品标题
       - **description**: 商品描述
       - **image_url**: 商品图片地址
       - **original_price**: 商品原价
       - **seckill_price**: 秒杀价
       - **discount**: 折扣
       - **stock**: 商品库存
       - **status**: 商品状态（上架/下架）
       - **seckill_start_time**: 秒杀开始时间
       - **seckill_end_time**: 秒杀结束时间
       - **create_time**: 创建时间
       - **update_time**: 更新时间

    4. **订单服务**: 该服务需要进行订单信息记录，因此设计模型如下
       - **order_id**: 订单ID
       - **user_id**: 用户ID
       - **product_id**: 商品ID
       - **quantity**: 购买数量
       - **unit_price**: 下单时单价
       - **total_amount**: 订单总金额
       - **order_status**: 订单状态（待付款/已付款/已取消）
       - **create_time**: 创建时间
       - **pay_time**: 支付时间
       
    5. **支付服务**: 该服务需要进行支付信息记录，因此设计模型如下
       - **payment_id**: 支付ID
       - **order_id**: 订单ID
       - **user_id**: 用户ID
       - **payment_method**: 支付方式（如：支付宝、微信、银行卡等）
       - **payment_amount**: 支付金额
       - **payment_status**: 支付状态（待支付/支付成功/支付失败/退款）
       - **transaction_id**: 第三方支付流水号
       - **payment_time**: 支付时间
       - **complete_time**: 支付完成时间
       - **refund_status**: 退款状态（无退款/部分退款/全额退款）
       - **create_time**: 创建时间
       - **update_time**: 更新时间

### 4.3 聚合根设计

1. 根据上述模型，我们进行聚合根设计，并给出聚合根的描述

2. 聚合根设计的要点：
   - 聚合根作为一次事务的最小单元，其内部必须要包含一次事务可能用到的所有字段
   - 而如果有必要，则需要进行适度地提取
   - 聚合根作为充血实体，与其内部高度相关的逻辑，需要下沉到该对象中，例如创建对象时候，对参数进行校验
   - 聚合根允许灵活定制，可以增加一些冗余字段，预防未来的拓展需求
   - 非聚合根如何提取？当一组字段类型具有独立的**业务意义**时候，推荐提取成一个实体类作为非聚合根，减少聚合根的复杂度
   
3. **认证服务**: 
    - **包含实体**: 
      - UserAuth (聚合根)
      - UserProfile (非聚合根) 用户个人信息，仅包含必要字段
      - LoginLog (非聚合根) 登录日志，仅包含必要字段
    - **核心字段**
    ```java
    // 认证聚合根
    class UserAuth {
        private Long userId;
        // 注意：下述字段因为具有认证的业务意义，因此不进行非聚合根的提取
        private String username;
        private String password;
        private String phone;
        private String email;
        // 聚合根要直接包含非聚合根
        private LoginLog loginLog;
        private UserProfile userProfile;
    }
    
    // 登录日志实体  
    class LoginLog {
        private Long logId;
        private Long userId;
        private String ip;
        private LocalDateTime loginTime;
        private LocalDateTime logoutTime;
    }
    
    // 用户信息值对象
    class UserProfile {
        private String displayName;
        private String avatar;
    }
    ```
   
4. **用户个人信息服务**: 
    - **包含实体**:
      - UserProfile (聚合根)
    - 字段描述
    ```java
    class UserProfile {
        private Long userId;
        private String username;
        private String password;
        private String phone;
        private String email;
        private String displayName;
        private String avatar;
    }
    ```
   - **补充**：因为createTime、updateTime暂时没有具体的业务含义，或者说，这是数据库字段中的标志信息，如果将来需要，可以再选择性拓展

5. **商品信息服务**:
    - **包含实体**:
        - Product (聚合根)
        - ProductBasicInfo (非聚合根)
        - ProductPriceInfo (非聚合根)
        - ProductSeckillInfo (非聚合根)
    - 字段描述
    ```java
    // 商品聚合根
    public class Product {
        private Long productId;
        private ProductBasicInfo basicInfo;
        private ProductPriceInfo priceInfo;
        private ProductSeckillInfo seckillInfo;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }
    
    // 商品基础信息
    public class ProductBasicInfo {
        private String title;
        private String description;
        private String imageUrl;
        private Integer status;
    }
    
    // 商品价格信息
    public class ProductPriceInfo {
        private BigDecimal originalPrice;
        private BigDecimal seckillPrice;
        private BigDecimal discount;
    }
    
    // 商品秒杀信息
    public class ProductSeckillInfo {
        private Integer stock;
        private LocalDateTime seckillStartTime;
        private LocalDateTime seckillEndTime;
    }
    ```
   - 信息补充：
     - 聚合根是事务的**最小单元**，但是还有一点是: **一个聚合根 = 一个实体**
     - 不要被查询列表迷惑，而写出List<ProductSimpleProfile>这样错误的非聚合根，这违背了**一个聚合根 = 一个实体**的原则
     - 换言之：业务中的查询参数多样性，**应该以DTO、XxxResponse来适配，而不是靠污染聚合根来解决**

6. **订单服务**:
   - **包含实体**:
     - Order (聚合根)
     - OrderItem (非聚合根)
     - OrderStatus (非聚合根) 订单状态
   - 字段描述
    ```java
    // 订单聚合根
    public class Order {
        private Long orderId;
        private Long userId;
        private OrderItem orderItem;
        private BigDecimal totalAmount;
        private OrderStatus status;
        private LocalDateTime createTime;
        private LocalDateTime payTime;
    }
    
    // 订单项
    public class OrderItem {
        private Long productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
    
    // 订单状态值对象
    public enum OrderStatus {
        PENDING_PAYMENT,  // 待付款
        PAID,             // 已付款
        CANCELLED         // 已取消
    }
    ```
   
7. **支付服务**:
   - **包含实体**:
     - Payment (聚合根) 订单服务聚合根
     - PaymentMethod (非聚合根) 支付方式
     - PaymentStatus (非聚合根) 支付状态
     - TimeInfo (非聚合根) 时间信息描述
   - 字段描述
    ```java
    // 支付聚合根
    public class Payment {
        private Long paymentId;
        private Long orderId;
        private Long userId;
        private PaymentMethod paymentMethod;
        private BigDecimal paymentAmount;
        private PaymentStatus paymentStatus;
        private String transactionId;
        private RefundStatus refundStatus;
        private TimeInfo timeInfo;
    }
    
    // 支付方式值对象
    public enum PaymentMethod {
        ALIPAY, WECHAT_PAY, BANK_CARD
    }
    
    // 支付状态值对象
    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, REFUND
    }
    
    // 退款状态值对象
    public enum RefundStatus {
        NO_REFUND, PARTIAL_REFUND, FULL_REFUND
    }
    
    // 时间信息
    public class TimeInfo {
        private LocalDateTime paymentTime;
        private LocalDateTime completeTime;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }
    ```
   
**至此，我们的DDD架构设计就初步完成了！**