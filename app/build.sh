#!/bin/bash

# 自动连点器 APK 构建脚本
# Auto Clicker APK Build Script

echo "========================================="
echo "     自动连点器 APK 构建工具"
echo "     Auto Clicker APK Builder"
echo "========================================="

# 检查是否安装了 Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ 错误: 未找到 ANDROID_HOME 环境变量"
    echo "请安装 Android SDK 并设置 ANDROID_HOME 环境变量"
    exit 1
fi

echo "✅ Android SDK 路径: $ANDROID_HOME"

# 检查 Gradle Wrapper
if [ ! -f "./gradlew" ]; then
    echo "❌ 错误: 未找到 gradlew 文件"
    echo "请确保在项目根目录执行此脚本"
    exit 1
fi

# 赋予 gradlew 执行权限
chmod +x ./gradlew

echo "🔧 开始构建 APK..."

# 清理项目
echo "🧹 清理项目..."
./gradlew clean

# 构建调试版本 APK
echo "📦 构建调试版本 APK..."
./gradlew assembleDebug

# 检查构建结果
if [ $? -eq 0 ]; then
    echo "✅ APK 构建成功!"
    echo ""
    echo "📁 APK 文件位置:"
    find . -name "*.apk" -type f | grep -E "(debug|release)"
    echo ""
    echo "🚀 安装说明:"
    echo "1. 将 APK 文件传输到 Android 设备"
    echo "2. 在设备上启用'未知来源'安装权限"
    echo "3. 点击 APK 文件进行安装"
    echo "4. 安装后启用无障碍服务和悬浮窗权限"
    echo ""
    echo "🎯 支持设备:"
    echo "- Android 5.0+ (API 21)"
    echo "- 所有 Android 设备和模拟器"
    echo "- 推荐 Android 7.0+ 获得最佳体验"
else
    echo "❌ APK 构建失败!"
    echo "请检查错误信息并解决问题后重试"
    exit 1
fi

echo "========================================="
echo "           构建完成"
echo "========================================="
