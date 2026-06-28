 package com.tuobang.battery.plugin.impl;

 import com.battery.plugin.api.NotificationInterceptor;
 import com.battery.plugin.api.dto.NotificationContext;

 import java.util.Map;

 /**
  * 托邦股份企业微信通知拦截器。
  * <p>
  * 将平台的邮件/短信通知同时推送到托邦企业微信群机器人。
  * </p>
  */
 public class TuobangWeComNotificationInterceptor implements NotificationInterceptor {

     private static final String WE_COM_WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=tuobang-bot-key";

     @Override
     public String interceptorId() {
         return "tuobang-wecom-interceptor";
     }

     @Override
     public boolean shouldSend(NotificationContext ctx) {
         // 托邦要求：质量告警类通知必须发送
         return ctx.getMetadata() != null
                 && "QUALITY_ALERT".equals(ctx.getMetadata().get("category"));
     }

     @Override
     public NotificationContext transform(NotificationContext ctx) {
         ctx.setTitle("[托邦电池护照] " + ctx.getTitle());
         ctx.setContent(ctx.getContent() + "\n\n---\n来自电池护照平台 · " + java.time.LocalDateTime.now());
         ctx.setRecipient(WE_COM_WEBHOOK_URL);
         ctx.setNotificationType("wecom_robot");
         return ctx;
     }
 }
