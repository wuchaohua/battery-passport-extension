 # battery-plugin-api

 电池护照平台公开SPI插件接口。
 天合储能/托邦股份的二次开发团队依赖此jar开发自定义插件。

 ## maven坐标（平台方私有Nexus）

 ```xml
 <dependency>
     <groupId>com.battery</groupId>
     <artifactId>battery-plugin-api</artifactId>
     <version>1.0.0</version>
     <scope>provided</scope>
 </dependency>
 ```

 ## 可实现的接口

 | 接口 | 用途 |
 |------|------|
 | BatteryDataValidator | 自定义电池数据校验 |
 | BatteryDataTransformer | ERP/MES数据格式转换 |
 | ReportCustomizer | 报告内容定制 |
 | NotificationInterceptor | 通知拦截和定制 |
 | EventSubscriber | 事件订阅 |

 ## 插件交付规范

 1. 客户实现上述一个或多个接口
 2. 配置 META-INF/services/ 文件
 3. 打包为 jar 文件
 4. 提交给平台运维放入 services/plugins/ 目录
 5. 平台无需重启即可热加载插件
