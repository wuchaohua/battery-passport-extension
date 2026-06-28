 @echo off
 REM 客户方二次开发构建脚本 —— Windows

 echo ==== 电池护照客户二次开发构建 ====

 REM 检查插件API jar是否存在
 if not exist "..\platform\plugin-api\target\battery-plugin-api-1.0.0.jar" (
     echo 请先将 platform/plugin-api 编译安装到本地仓库
     echo mvn install -f ../platform/plugin-api/pom.xml
     exit /b 1
 )

 REM 编译天合定制插件
 echo [1/2] 编译天合储能定制插件...
 cd ..\customer-examples\trina-custom-plugin
 call mvn clean package

 REM 编译托邦定制插件
 echo [2/2] 编译托邦股份定制插件...
 cd ..\tuobang-custom-plugin
 call mvn clean package

 echo.
 echo ==== 构建完成 ====
 echo 天合插件: ..\customer-examples\trina-custom-plugin\target\trina-battery-plugin-1.0.0.jar
 echo 托邦插件: ..\customer-examples\tuobang-custom-plugin\target\tuobang-battery-plugin-1.0.0.jar
 echo.
 echo 请将插件的 jar 文件提交给平台运维，放入 plugins/ 目录。
