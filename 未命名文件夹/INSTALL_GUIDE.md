# 自动连点器 APK 构建和安装指南

## 方法一：使用 Android Studio（推荐）

### 1. 安装 Android Studio
- 下载地址：https://developer.android.com/studio
- 安装时会自动配置 Android SDK

### 2. 导入项目
1. 打开 Android Studio
2. 选择 "Open an existing Android Studio project"
3. 选择当前项目文件夹
4. 等待 Gradle 同步完成

### 3. 构建 APK
1. 在 Android Studio 中选择 `Build` > `Build Bundle(s) / APK(s)` > `Build APK(s)`
2. 构建完成后，APK 文件位于 `app/build/outputs/apk/debug/` 目录

## 方法二：命令行构建

### 1. 安装依赖
```bash
# 安装 Android SDK
# 下载 Android SDK Command line tools
# https://developer.android.com/studio#command-tools

# 设置环境变量（添加到 ~/.zshrc 或 ~/.bash_profile）
export ANDROID_HOME=/Users/YOUR_USERNAME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### 2. 配置本地 SDK 路径
编辑 `local.properties` 文件，设置你的 Android SDK 路径：
```
sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk
```

### 3. 运行构建脚本
```bash
./build.sh
```

或手动构建：
```bash
./gradlew assembleDebug
```

## 方法三：在线构建服务

如果本地环境配置困难，可以使用在线构建服务：

1. **GitHub Actions**（免费）
   - 将代码上传到 GitHub
   - 配置 CI/CD 自动构建 APK

2. **AppCenter**（Microsoft）
   - 支持自动构建和分发
   - 有免费额度

## APK 安装步骤

### 1. 传输 APK 到 Android 设备
- 通过 USB 传输
- 通过邮件发送
- 上传到云盘下载

### 2. 启用未知来源安装
1. Android 8.0+：设置 > 应用和通知 > 特殊应用访问 > 安装未知应用
2. Android 7.0 及以下：设置 > 安全 > 未知来源

### 3. 安装 APK
1. 在文件管理器中找到 APK 文件
2. 点击安装
3. 按照提示完成安装

### 4. 配置权限（重要）
安装后首次启动应用时：

1. **启用无障碍服务**
   - 设置 > 辅助功能 > 自动连点器
   - 开启服务

2. **授予悬浮窗权限**
   - 设置 > 应用 > 特殊应用访问 > 显示在其他应用的上层
   - 找到自动连点器并启用

## 常见问题

### Q: 构建失败怎么办？
A: 检查以下项目：
- Java 版本（需要 JDK 8 或更高）
- Android SDK 是否正确安装
- 网络连接是否正常（需要下载依赖）

### Q: 应用安装后无法点击？
A: 确保已启用无障碍服务，这是核心功能的前提。

### Q: 悬浮窗无法显示？
A: 检查悬浮窗权限是否已授予。

### Q: 兼容性问题？
A: 应用支持 Android 5.0+，推荐 Android 7.0+ 获得最佳体验。

## 技术支持

如果遇到问题，请检查：
1. 设备 Android 版本是否 ≥ 5.0
2. 是否正确配置了所需权限
3. 应用是否被系统杀掉（添加到电池优化白名单）

---

**注意：本应用仅供学习和合法用途使用，请遵守相关法律法规。**
