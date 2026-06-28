 # Tuobang - 托邦股份定制插件

 ## 构建

 ```bash
 mvn clean package
 ```

 ## 交付

 将 target/tuobang-battery-plugin-1.0.0.jar 提交给平台运维，
 放入 services/plugins/ 目录即可生效。

 ## 包含的自定义逻辑

 | 插件 | 接口 | 说明 |
 |------|------|------|
 | TuobangErpDataTransformer | BatteryDataTransformer | 将托邦ERP导出的电池数据转换为平台标准格式 |
 | TuobangWeComNotificationInterceptor | NotificationInterceptor | 将质量告警通知推送到托邦企业微信群 |

 ## 配置要求

 1. 在平台配置中设置 erp-source-types: tuobang_erp_v2,tuobang_erp_v3
 2. 在平台 webhook 配置中设置企业微信机器人 webhook URL
 3. 托邦ERP导出数据需包含字段: bat_sn, model_code, cap_rated, volt_rated, chem_type, prod_date, weight_kg
