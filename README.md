 # Battery Passport Extension Demo

 > 电池护照平台 — 客户二次开发示例工程
 >
 > 适用场景: 天合储能、托邦股份在不获取 Java 后端源码的情况下进行业务定制

 ---

 ## 概览

 本示例工程展示 **在不交付 Java 后端源码的前提下**，客户方如何通过 SPI 插件、REST API、Webhook、脚本引擎和前端扩展实现业务定制。

 ## 目录结构

 ```
 battery-passport-extension-demo/
 ├── platform/                           ← 平台方公开发布的 SDK 和 SPI 接口
 │   ├── plugin-api/                     ←   SPI 插件接口 jar（maven 坐标公开）
 │   │   ├── BatteryDataValidator.java   ←    校验器 SPI
 │   │   ├── BatteryDataTransformer.java ←    数据转换 SPI
 │   │   ├── ReportCustomizer.java       ←    报告定制 SPI
 │   │   ├── NotificationInterceptor.java ←   通知拦截 SPI
 │   │   └── EventSubscriber.java        ←    事件订阅 SPI
 │   └── sdk-client/                     ←   RESTful API 客户端 SDK
 │       └── BatteryPassportClient.java  ←     客户端接口
 │
 ├── customer-examples/                  ← 客户方二次开发示例
 │   ├── trina-custom-plugin/            ←  天合储能定制插件
 │   │   └── TrinaCapacityValidator.java ←   容量偏差校验
 │   ├── tuobang-custom-plugin/          ←  托邦股份定制插件
 │   │   ├── TuobangErpDataTransformer.java ←  ERP 数据格式转换
 │   │   └── TuobangWeComNotificationInterceptor.java ← 企微通知
 │   ├── webhook-receiver/               ←  Webhook 接收服务示例
 │   ├── frontend-extension/             ←  前端 React 扩展组件
 │   └── script-engine/                  ←  Groovy 脚本扩展示例
 │       ├── custom-validation-rules.groovy ← 企业校验规则
 │       └── report-format.groovy          ← 报告格式定制
 ├── config-examples/                    ← 配置定制示例
 │   ├── battery-service-application-trina.yml
 │   ├── battery-service-application-tuobang.yml
 │   └── gateway-routes-trina.yml
 ├── build.sh                            ← Linux 构建脚本
 └── scripts/
     ├── build.bat                       ← Windows 构建脚本
     └── deploy-plugin.sh                ← 运维热部署脚本
 ```

 ## 六种二次开发方式

 ### 1. SPI 插件机制

 实现平台公开的接口，打包为 jar 提交给运维热部署，**无需重启服务**。

 ```java
 // 天合储能示例：实现 BatteryDataValidator 接口
 public class TrinaCapacityValidator implements BatteryDataValidator {
     public ValidateResult validate(BatteryData data) {
         // 天合储能容量偏差不能超过 2%
         if (deviation > 2.0) return ValidateResult.fail("容量偏差超限");
         return ValidateResult.ok();
     }
 }
 ```

 | 接口 | 触发时机 | 天合示例 | 托邦示例 |
 |------|---------|---------|---------|
 | BatteryDataValidator | 数据导入/创建时 | 容量偏差校验 | - |
 | BatteryDataTransformer | 外部数据导入时 | - | ERP 格式转换 |
 | ReportCustomizer | 报告生成后 | Logo 水印 | 页脚模板 |
 | NotificationInterceptor | 通知发送前 | - | 企业微信推送 |
 | EventSubscriber | 状态变更事件 | 入库通知 | 质量告警 |

 ### 2. RESTful API + SDK

 ```java
 BatteryPassportClient client = BatteryPassportClientBuilder
     .baseUrl("https://battery-api.trinasolar.com")
     .apiKey("key_abc123")
     .build();
 PassportDTO passport = client.getPassport("BP-TR-2026-00001");
 byte[] pdf = client.generateReport("BP-TR-2026-00001", "COMPLIANCE", "zh-CN");
 ```

 ### 3. Webhook 事件回调

 客户部署 HTTP 服务接收平台事件推送：

 | 事件类型 | 触发时机 | 用途 |
 |---------|---------|------|
 | PASSPORT_CREATED | 护照创建 | ERP 同步 |
 | PASSPORT_UPDATED | 护照更新 | 本地缓存更新 |
 | REPORT_GENERATED | 报告生成 | 归档下载 |
 | QUALITY_ALERT | 质量告警 | 企微通知 |

 ### 4. 脚本引擎扩展

 Groovy 脚本热加载，无需编译：

 ```groovy
 // validation-rules.groovy
 if (enterpriseCode == "trina") {
     def minTemp = findInJson(data.rawJson, "min_operating_temp")
     if (minTemp < -20) throw new RuntimeException("温度超限")
 }
 ```

 ### 5. 前端远程组件

 React 组件通过配置注入，无需改主应用代码：

 ```json
 {
   "mountPoint": "passport.dashboard.top",
   "componentUrl": "https://cdn.trina.com/battery/CustomDashboard.js"
 }
 ```

 ### 6. 配置化定制

 通过 yaml 覆盖差异，无需写代码：

 ```yaml
 battery:
   sso:
     provider: trina
     trina:
       type: cas
       server-url: https://cas.trinasolar.com
   validation:
     capacity-deviation-threshold: 2.0
 custom-branding:
   primary-color: '#003366'
 ```

 ## 客户开发流程

 ```
 客户开发 -> 编译插件 jar -> 提交给平台运维
                                 |
                         运维放入 plugins/{enterprise}/
                                 |
                         平台触发热加载（无需重启）
                                 |
                         验证日志：插件注册成功
 ```

 ## 天合储能 vs 托邦股份 定制差异

 | 维度 | 天合储能 | 托邦股份 |
 |------|---------|---------|
 | SSO 协议 | CAS | OIDC |
 | 定制插件 | 容量校验器 | ERP 转换器 + 企微通知 |
 | 脚本规则 | 温度范围校验 | 必填重量字段 |
 | 前端扩展 | 专属仪表盘 | - |
 | 品牌色 | #003366 | #c9362b |

 ## 构建

 ```bash
 # 安装 SPI 接口 jar 到本地 maven 仓库
 mvn install -f platform/plugin-api/pom.xml

 # 编译天合定制插件
 mvn clean package -f customer-examples/trina-custom-plugin/pom.xml

 # 编译托邦定制插件
 mvn clean package -f customer-examples/tuobang-custom-plugin/pom.xml

 # 编译 Webhook 接收服务
 mvn clean package -f customer-examples/webhook-receiver/pom.xml
 ```

 ## 许可证

 Copyright (c) 2026 电池护照平台。保留所有权利。
