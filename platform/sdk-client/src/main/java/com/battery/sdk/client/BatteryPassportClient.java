 package com.battery.sdk.client;

 import com.battery.sdk.client.dto.*;
 import java.util.List;

 /**
  * 电池护照开放 API 客户端接口。
  * <p>
  * 平台方发布 SDK jar，客户方无需了解内网架构，
  * 只需配置 API 地址和认证信息即可调用。
  * </p>
  *
  * 使用示例：
  * <pre>
  * BatteryPassportClient client = BatteryPassportClientBuilder
  *     .baseUrl("https://battery-api.trinasolar.com")
  *     .apiKey("your-api-key")
  *     .apiSecret("your-api-secret")
  *     .build();
  *
  * PassportDTO passport = client.getPassport("BP-TR-2026-00001");
  * </pre>
  */
 public interface BatteryPassportClient {
 
     // ========== 电池护照 ==========
 
     PassportDTO getPassport(String passportId);
 
     List<PassportDTO> listPassports(PassportQuery query);
 
     String createPassport(PassportDTO passport);
 
     void updatePassport(String passportId, PassportDTO passport);
 
     void deletePassport(String passportId);
 
     // ========== 溯源 ==========
 
     List<TraceEventDTO> getTraceChain(String passportId);
 
     void addTraceEvent(String passportId, TraceEventDTO event);
 
     // ========== 报告 ==========
 
     byte[] generateReport(String passportId, String reportType, String language);
 
     String getReportDownloadUrl(String reportId);
 
     // ========== 回调注册 ==========
 
     void registerWebhook(String url, String secret, List<String> eventTypes);
 
     void unregisterWebhook(String webhookId);
 }
