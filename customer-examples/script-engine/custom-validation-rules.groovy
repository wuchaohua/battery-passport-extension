 /**
  * 脚本引擎 —— 自定义校验规则（Groovy 示例）。
  *
  * 平台内嵌 GraalVM Polyglot 执行 Groovy/JS 脚本。
  * 客户方编写脚本文件放入 scripts/custom/ 目录，
  * 平台自动加载并在数据入库时执行。
  *
  * 脚本中可以使用的绑定变量：
  *   - batteryData: com.battery.plugin.api.dto.BatteryData
  *   - log: org.slf4j.Logger
  *   - enterpriseCode: String （当前企业编码，如 "trina" / "tuobang"）
  */

 // 天合储能覆盖额外校验规则
 if (enterpriseCode == "trina") {
     // 温度范围校验：天合电池要求工作温度 -20 ~ 60
     def minTemp = batteryData.getRawJson() ? findInJson(batteryData.getRawJson(), "min_operating_temp") : null
     def maxTemp = batteryData.getRawJson() ? findInJson(batteryData.getRawJson(), "max_operating_temp") : null

     if (minTemp != null && maxTemp != null) {
         if (minTemp < -20 || maxTemp > 60) {
             log.warn("天合储能温度范围超限: min={}, max={}", minTemp, maxTemp)
         }
     }
 }

 // 托邦股份覆盖额外校验规则
 if (enterpriseCode == "tuobang") {
     // 托邦要求所有电池必须有重量数据
     if (batteryData.getWeight() == null || batteryData.getWeight() <= 0) {
         throw new RuntimeException("托邦电池必须填写重量")
     }
 }

 // 通用规则：2026年后生产的电池必须有完整SOH数据
 if (batteryData.getProductionDate() != null
         && batteryData.getProductionDate().getYear() >= 2026
         && batteryData.getInitialSoh() == null) {
     log.warn("2026年后生产的电池缺少初始SOH数据: {}", batteryData.getSerialNumber())
 }

 return true

 // --- 辅助方法 ---
 def findInJson(String json, String key) {
     def pattern = ~/"${key}"\s*:\s*([\d.]+)/
     def matcher = pattern.matcher(json)
     return matcher.find() ? Double.parseDouble(matcher.group(1)) : null
 }
