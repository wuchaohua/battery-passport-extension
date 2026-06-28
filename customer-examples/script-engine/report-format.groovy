 /**
  * 脚本引擎 —— 报告格式自定义（Groovy 示例）。
  *
  * 在报告服务生成 PDF 前，调用此脚本注入企业特定的水印和页脚。
  */

 def customizeReport(byte[] pdfContent, Map<String, Object> context) {
     def enterprise = context["enterpriseCode"]
     def watermarkText = ""

     switch (enterprise) {
         case "trina":
             watermarkText = "天合储能 ｜ 机密文件"
             break
         case "tuobang":
             watermarkText = "托邦股份 ｜ 内部资料"
             break
         default:
             watermarkText = "电池护照平台"
     }

     log.info("报告水印: {}", watermarkText)

     // 返回添加水印后的 PDF 内容
     // 实际实现使用 Apache PDFBox 或 iText
     // return addWatermark(pdfContent, watermarkText)

     return pdfContent
 }
