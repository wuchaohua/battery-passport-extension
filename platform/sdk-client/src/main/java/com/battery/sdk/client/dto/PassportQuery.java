 package com.battery.sdk.client.dto;

 public class PassportQuery {
     private String serialNumber;
     private String productModel;
     private String manufacturer;
     private String status;
     private String dateFrom;
     private String dateTo;
     private int page = 1;
     private int size = 20;

     public String getSerialNumber() { return serialNumber; }
     public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
     public String getProductModel() { return productModel; }
     public void setProductModel(String productModel) { this.productModel = productModel; }
     public String getManufacturer() { return manufacturer; }
     public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
     public String getStatus() { return status; }
     public void setStatus(String status) { this.status = status; }
     public String getDateFrom() { return dateFrom; }
     public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }
     public String getDateTo() { return dateTo; }
     public void setDateTo(String dateTo) { this.dateTo = dateTo; }
     public int getPage() { return page; }
     public void setPage(int page) { this.page = page; }
     public int getSize() { return size; }
     public void setSize(int size) { this.size = size; }
 }
