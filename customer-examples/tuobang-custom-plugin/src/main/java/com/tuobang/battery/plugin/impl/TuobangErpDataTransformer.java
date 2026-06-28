 package com.tuobang.battery.plugin.impl;

 import com.battery.plugin.api.BatteryDataTransformer;
 import com.battery.plugin.api.dto.BatteryData;
 import com.fasterxml.jackson.core.type.TypeReference;
 import com.fasterxml.jackson.databind.ObjectMapper;

 import java.time.LocalDate;
 import java.time.format.DateTimeFormatter;
 import java.util.Map;

 /**
  * 托邦股份 ERP 数据转换器。
  * <p>
  * 托邦股份的 ERP 系统导出的电池数据为自定义 CSV/JSON 格式，
  * 此转换器将其映射为平台标准 BatteryData。
  * </p>
  */
 public class TuobangErpDataTransformer implements BatteryDataTransformer {

     private static final ObjectMapper MAPPER = new ObjectMapper();

     @Override
     public String transformerId() {
         return "tuobang-erp-transformer";
     }

     @Override
     public boolean supports(String sourceType) {
         return "tuobang_erp_v2".equals(sourceType) || "tuobang_erp_v3".equals(sourceType);
     }

     @Override
     public BatteryData transform(Object rawData) {
         if (rawData instanceof String jsonStr) {
             return transformFromJson(jsonStr);
         }
         if (rawData instanceof Map) {
             return transformFromMap((Map<String, Object>) rawData);
         }
         throw new IllegalArgumentException("托邦不支持的数据类型: " + rawData.getClass());
     }

     private BatteryData transformFromJson(String json) {
         try {
             Map<String, Object> map = MAPPER.readValue(json, new TypeReference<>() {});
             return transformFromMap(map);
         } catch (Exception e) {
             throw new RuntimeException("托邦ERP JSON解析失败", e);
         }
     }

     @SuppressWarnings("unchecked")
     private BatteryData transformFromMap(Map<String, Object> map) {
         BatteryData data = new BatteryData();

         // 托邦ERP字段映射
         data.setSerialNumber(str(map, "bat_sn"));
         data.setProductModel(str(map, "model_code"));
         data.setManufacturer("托邦股份");
         data.setRatedCapacity(doubleVal(map, "cap_rated"));
         data.setRatedVoltage(doubleVal(map, "volt_rated"));
         data.setChemistryType(str(map, "chem_type"));
         data.setWeight(doubleVal(map, "weight_kg"));

         String prodDate = str(map, "prod_date");
         if (prodDate != null && !prodDate.isBlank()) {
             data.setProductionDate(LocalDate.parse(prodDate, DateTimeFormatter.ofPattern("yyyyMMdd")));
         }

         try {
             data.setRawJson(MAPPER.writeValueAsString(map));
         } catch (Exception ignored) {}

         return data;
     }

     private String str(Map<String, Object> map, String key) {
         Object v = map.get(key);
         return v != null ? v.toString() : null;
     }

     private Double doubleVal(Map<String, Object> map, String key) {
         Object v = map.get(key);
         if (v instanceof Number n) return n.doubleValue();
         if (v instanceof String s) {
             try { return Double.parseDouble(s); } catch (NumberFormatException e) { return null; }
         }
         return null;
     }
 }
