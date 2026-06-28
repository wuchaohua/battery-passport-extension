 #!/bin/bash
 # 平台运维 —— 热部署客户插件脚本
 # 将客户提交的 jar 部署到目标服务
 set -euo pipefail

 if [ $# -lt 2 ]; then
     echo "用法: $0 <插件jar路径> <目标企业>"
     echo "示例: $0 ./trina-battery-plugin-1.0.0.jar trina"
     exit 1
 fi

 JAR_PATH="$1"
 ENTERPRISE="$2"

 PLUGIN_DIR="/opt/battery/plugins/${ENTERPRISE}"
 JAR_NAME=$(basename "$JAR_PATH")

 echo "部署插件到 ${ENTERPRISE} 环境..."

 # 1. 备份旧版本
 if [ -f "${PLUGIN_DIR}/${JAR_NAME}" ]; then
     mv "${PLUGIN_DIR}/${JAR_NAME}" "${PLUGIN_DIR}/${JAR_NAME}.bak.$(date +%Y%m%d%H%M%S)"
 fi

 # 2. 复制新插件
 cp "$JAR_PATH" "${PLUGIN_DIR}/${JAR_NAME}"

 # 3. 触发平台热加载（通过管理端口）
 curl -s -X POST "http://localhost:18084/actuator/plugins/reload" \
     -H "Content-Type: application/json" \
     -d "{\"enterprise\": \"${ENTERPRISE}\"}"

 echo "插件 ${JAR_NAME} 部署完成，已触发热加载"
