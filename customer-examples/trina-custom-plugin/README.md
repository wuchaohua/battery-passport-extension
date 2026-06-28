 # Trina Storage - 天合储能定制插件

 ## 构建

 ```bash
 mvn clean package
 ```

 ## 交付

 将 target/trina-battery-plugin-1.0.0.jar 提交给平台运维，
 放入 services/plugins/ 目录即可生效。

 ## 包含的自定义逻辑

 - **TrinaCapacityValidator**: 容量偏差校验（偏差超2%拒绝入库）

 ## 配置要求

 需要在平台后台上传电池数据时，确保 rawJson 包含 actualCapacity 字段。
