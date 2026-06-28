 #!/bin/bash
 # 客户方二次开发构建脚本 —— MacOS / Linux
 set -euo pipefail

 SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

 echo "===== 电池护照客户二次开发构建 ====="

 # 检测平台并设置 bundle
 case "$(uname -s)" in
     Darwin*)   BUNDLE="bundle-osx" ;;
     Linux*)    BUNDLE="bundle-linux" ;;
     CYGWIN*|MINGW*|MSYS*) BUNDLE="bundle-win" ;;
     *)         echo "Unsupported OS"; exit 1 ;;
 esac

 # 1. 安装插件API jar到本地仓库（从平台获取）
 if [ ! -f "$SCRIPT_DIR/platform/plugin-api/target/battery-plugin-api-1.0.0.jar" ]; then
     echo "请先将 platform/plugin-api 编译安装到本地仓库"
     echo "mvn install -f platform/plugin-api/pom.xml"
     exit 1
 fi

 # 2. 编译天合定制插件
 echo "[1/2] 编译天合储能定制插件..."
 (cd "$SCRIPT_DIR/customer-examples/trina-custom-plugin" && mvn clean package)

 # 3. 编译托邦定制插件
 echo "[2/2] 编译托邦股份定制插件..."
 (cd "$SCRIPT_DIR/customer-examples/tuobang-custom-plugin" && mvn clean package)

 echo ""
 echo "===== 构建完成 ====="
 echo "天合插件: customer-examples/trina-custom-plugin/target/trina-battery-plugin-1.0.0.jar"
 echo "托邦插件: customer-examples/tuobang-custom-plugin/target/tuobang-battery-plugin-1.0.0.jar"
 echo ""
 echo "请将插件的 jar 文件提交给平台运维，放入 plugins/ 目录。"
