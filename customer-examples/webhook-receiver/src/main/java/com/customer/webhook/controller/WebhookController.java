 package com.customer.webhook.controller;

 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

 import java.util.Map;

 /**
  * 电池护照 Webhook 接收端点。
  * <p>
  * 天合储能/托邦股份部署此服务，接收平台推送的事件通知。
  * 平台侧配置 webhook URL 为 http://customer-host:9000/webhook/battery
  * </p>
  */
 @RestController
 @RequestMapping("/webhook")
 public class WebhookController {
 
     private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
 
     /**
      * 接收电池护照事件通知
      * POST /webhook/battery
      *
      * 请求体示例：
      * {
      *   "eventId": "evt_20260628_001",
      *   "eventType": "PASSPORT_CREATED",
      *   "passportId": "BP-TR-2026-00001",
      *   "serialNumber": "TR-BAT-2026-A001",
      *   "occurredAt": "2026-06-28T10:30:00",
      *   "payload": { ... }
      * }
      */
     @PostMapping("/battery")
     public ResponseEntity<String> receiveEvent(@RequestBody Map<String, Object> payload,
                                                @RequestHeader("X-Webhook-Signature") String signature) {
         log.info("收到电池护照事件推送: type={}, passportId={}",
                 payload.get("eventType"), payload.get("passportId"));
 
         // 1. 验签（使用预共享密钥）
         if (!verifySignature(payload, signature)) {
             log.warn("Webhook 签名验证失败");
             return ResponseEntity.status(401).body("invalid signature");
         }
 
         String eventType = (String) payload.get("eventType");
 
         // 2. 根据事件类型分发处理
         switch (eventType != null ? eventType : "") {
             case "PASSPORT_CREATED" -> handlePassportCreated(payload);
             case "PASSPORT_UPDATED" -> handlePassportUpdated(payload);
             case "QUALITY_ALERT" -> handleQualityAlert(payload);
             case "REPORT_GENERATED" -> handleReportGenerated(payload);
             default -> log.info("忽略未知事件类型: {}", eventType);
         }
 
         return ResponseEntity.ok("received");
     }
 
     /**
      * Webhook 健康检查（平台定期调用）
      */
     @GetMapping("/health")
     public ResponseEntity<String> health() {
         return ResponseEntity.ok("OK");
     }
 
     private boolean verifySignature(Map<String, Object> payload, String signature) {
         // 生产环境使用 HMAC-SHA256
         // String expected = hmac(payload, sharedSecret);
         // return expected.equals(signature);
         return signature != null && !signature.isBlank();
     }
 
     private void handlePassportCreated(Map<String, Object> payload) {
         log.info("处理电池护照创建事件 -> 同步到本地ERP系统");
     }
 
     private void handlePassportUpdated(Map<String, Object> payload) {
         log.info("处理电池护照更新事件 -> 更新本地缓存");
     }
 
     private void handleQualityAlert(Map<String, Object> payload) {
         log.warn("收到质量告警: {}", payload);
     }
 
     private void handleReportGenerated(Map<String, Object> payload) {
         // payload 中包含报告下载地址，客户方可以下载并存档
         String reportUrl = (String) payload.get("reportUrl");
         if (reportUrl != null) {
             log.info("报告生成完毕，下载地址: {}", reportUrl);
         }
     }
 }
