 package com.trina.battery.plugin.impl;

 import com.battery.plugin.api.BatteryDataValidator;
 import com.battery.plugin.api.dto.BatteryData;
 import com.battery.plugin.api.dto.ValidateResult;

 /**
  * 天合储能自定义校验器 —— 容量偏差校验。
  * <p>
  * 天合储能要求其生产的电池额定容量偏差不能超过 2%。
  * </p>
  */
 public class TrinaCapacityValidator implements BatteryDataValidator {

     @Override
     public String validatorId() {
         return "trina-capacity-validator";
     }

     @Override
     public int order() {
         return 1; // 最先执行
     }

     @Override
     public ValidateResult validate(BatteryData data) {
         // 天合储能内部标准：额定容量 >= 100Ah 时偏差需在 2% 以内
         // 此处假设 rawJson 中带有实际测试容量值
         if (data.getRatedCapacity() == null || data.getRatedCapacity() < 100) {
             return ValidateResult.ok();
         }

         // 从原始数据中解析实际测试容量
         double actualCapacity = parseActualCapacity(data.getRawJson());
         double deviation = Math.abs(actualCapacity - data.getRatedCapacity())
                 / data.getRatedCapacity() * 100;

         if (deviation > 2.0) {
             return ValidateResult.fail(
                     String.format("天合储能容量偏差校验未通过: 额定=%.2fAh, 实测=%.2fAh, 偏差=%.2f%%",
                             data.getRatedCapacity(), actualCapacity, deviation));
         }

         if (deviation > 1.5) {
             return ValidateResult.warn(
                     String.format("天合储能容量偏差接近阈值: 偏差=%.2f%%", deviation));
         }

         return ValidateResult.ok();
     }

     private double parseActualCapacity(String rawJson) {
         if (rawJson == null || rawJson.isBlank()) return 0;
         try {
             // 假设 rawJson 中包含 "actualCapacity": 102.5
             int idx = rawJson.indexOf("\"actualCapacity\"");
             if (idx == -1) return 0;
             int colon = rawJson.indexOf(':', idx);
             int end = rawJson.indexOf(',', colon);
             if (end == -1) end = rawJson.indexOf('}', colon);
             String valStr = rawJson.substring(colon + 1, end).trim();
             return Double.parseDouble(valStr);
         } catch (Exception e) {
             return 0;
         }
     }
 }
