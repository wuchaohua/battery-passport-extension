# Battery Passport Extension Demo

电池护照平台 - 客户二次开发示例工程

在不获取 Java 后端源码的前提下，通过 SPI 插件、REST API、Webhook、
脚本引擎和前端扩展实现业务定制。

---

## 目录结构

```
battery-passport-extension-demo/
├── platform/                    平台方公开发布的 SDK 和 SPI 接口
│   ├── plugin-api/              SPI 插件接口 jar
│   └── sdk-client/              RESTful API 客户端 SDK
├── customer-examples/           客户方二次开发示例
│   ├── trina-custom-plugin/     企业A定制插件(容量校验)
│   ├── tuobang-custom-plugin/   企业B定制插件(ERP转换+企微通知)
│   ├── webhook-receiver/        Webhook 接收服务
│   ├── frontend-extension/      前端 React 扩展组件
│   └── script-engine/           Groovy 脚本扩展示例
├── config-examples/             配置定制示例
├── build.sh                     Linux 构建脚本
└── scripts/                     构建和部署脚本
```

## SPI 插件接口

接口 | 触发时机 | 说明
--- | --- | ---
BatteryDataValidator | 数据导入/创建时 | 自定义校验逻辑
BatteryDataTransformer | 外部数据导入时 | ERP 数据格式转换
ReportCustomizer | 报告生成后 | 企业 Logo 水印
NotificationInterceptor | 通知发送前 | 企业微信推送
EventSubscriber | 状态变更事件 | 入库通知

## 开发流程

1. 依赖 SPI 接口 jar (maven coordinate: com.battery:plugin-api)
2. 实现一个或多个 SPI 接口
3. 在 META-INF/services/ 中注册实现类
4. 编译为 jar 提交给运维
5. 运维放入 plugins/ 目录，平台自动热加载(无需重启)

## REST API 客户端

```java
BatteryPassportClient client = BatteryPassportClientBuilder
    .baseUrl("https://battery-api.example.com")
    .apiKey("your-key")
    .build();
PassportDTO passport = client.getPassport("BP-2026-00001");
```

## Webhook 事件

事件类型 | 触发时机
--- | ---
PASSPORT_CREATED | 护照创建
PASSPORT_UPDATED | 护照更新
REPORT_GENERATED | 报告生成
QUALITY_ALERT | 质量告警

## 前置扩展

React 组件通过配置注入,无需改主应用:

```json
{
  "mountPoint": "passport.dashboard.top",
  "componentUrl": "https://cdn.example.com/CustomDashboard.js"
}
```

## 构建

```bash
# 安装 SPI 接口 jar
mvn install -f platform/plugin-api/pom.xml

# 编译定制插件
mvn clean package -f customer-examples/trina-custom-plugin/pom.xml

# 编译 Webhook 接收服务
mvn clean package -f customer-examples/webhook-receiver/pom.xml
```