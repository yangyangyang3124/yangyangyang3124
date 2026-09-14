@echo off
chcp 65001 >nul
setlocal
cd /d "%~dp0"

echo [1/3] 编译主程序...
if exist build rmdir /s /q build
javac -encoding UTF-8 -d build\main src\plagiarism\*.java || goto :error

echo [2/3] 打包 main.jar...
jar --create --file main.jar --main-class plagiarism.Main -C build\main . || goto :error

echo [3/3] 编译并运行单元测试...
javac -encoding UTF-8 -d build\test -cp build\main test\plagiarism\*.java || goto :error
java -cp "build\main;build\test" plagiarism.TestRunner || goto :error

echo.
echo 构建完成，可执行文件：main.jar
echo 用法：java -jar main.jar 原文文件 抄袭版文件 答案文件
exit /b 0

:error
echo 构建失败！
exit /b 1
