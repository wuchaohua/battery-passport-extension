 package com.battery.sdk.client.dto;

 import java.time.LocalDateTime;
 import java.util.Map;

 public class TraceEventDTO {
     private String eventId;
     private String eventType;
     private String operator;
     private String location;
     private LocalDateTime occurredAt;
     private Map<String, Object> details;

     public String getEventId() { return eventId; }
     public void setEventId(String eventId) { this.eventId = eventId; }
     public String getEventType() { return eventType; }
     public void setEventType(String eventType) { this.eventType = eventType; }
     public String getOperator() { return operator; }
     public void setOperator(String operator) { this.operator = operator; }
     public String getLocation() { return location; }
     public void setLocation(String location) { this.location = location; }
     public LocalDateTime getOccurredAt() { return occurredAt; }
     public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
     public Map<String, Object> getDetails() { return details; }
     public void setDetails(Map<String, Object> details) { this.details = details; }
 }
