# 🚀 获取APK文件 - 3种最快方法

## 方法一：使用Android Studio（5分钟内完成）⭐⭐⭐⭐⭐

### 1. 下载Android Studio
- 官网：https://developer.android.com/studio
- 选择适合您系统的版本下载

### 2. 安装并打开项目
1. 安装Android Studio（会自动配置Android SDK）
2. 启动后选择 "Open an existing Android Studio project"
3. 选择当前项目文件夹（包含build.gradle的文件夹）
4. 等待项目同步（首次可能需要下载依赖，约2-3分钟）

### 3. 构建APK
1. 顶部菜单：Build → Build Bundle(s) / APK(s) → Build APK(s)
2. 等待构建完成（约1-2分钟）
3. 点击弹出的通知中的"locate"链接
4. APK文件位置：`app/build/outputs/apk/debug/app-debug.apk`

---

## 方法二：在线构建服务（无需本地环境）⭐⭐⭐⭐

### GitHub Actions自动构建
1. **创建GitHub仓库**
   - 访问 https://github.com
   - 创建新仓库，上传所有项目文件

2. **配置自动构建**
   ```yaml
   # 创建 .github/workflows/build.yml
   name: Build APK
   on: [push]
   jobs:
     build:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v3
         - uses: actions/setup-java@v3
           with:
             java-version: '11'
             distribution: 'temurin'
         - name: Build APK
           run: ./gradlew assembleDebug
         - name: Upload APK
           uses: actions/upload-artifact@v3
           with:
             name: app-debug
             path: app/build/outputs/apk/debug/app-debug.apk
   ```

3. **下载APK**
   - 推送代码后，在Actions页面下载构建的APK

### Appcircle（在线构建平台）
1. 访问 https://appcircle.io
2. 注册账号，连接GitHub
3. 配置自动构建
4. 下载生成的APK

---

## 方法三：请朋友帮助构建 ⭐⭐⭐

如果您有朋友使用Android Studio：
1. 将项目文件夹打包发送给他们
2. 请他们按方法一操作
3. 获取构建好的APK文件

---

## 🎯 我推荐的最佳方案

**使用Android Studio（方法一）**，因为：
- ✅ 最简单可靠
- ✅ 5-10分钟即可完成
- ✅ 官方工具，100%兼容
- ✅ 以后修改代码也方便

### Android Studio下载链接：
- **官网**: https://developer.android.com/studio
- **文件大小**: 约1GB
- **安装时间**: 5-10分钟

---

## 📱 APK安装步骤

构建完成后：

### 1. 传输APK到手机
- 通过数据线传输
- 发送到微信/QQ文件助手
- 上传到网盘再下载

### 2. 安装APK
1. 在手机上找到APK文件
2. 点击安装
3. 如提示"未知来源"，请在设置中允许

### 3. 配置权限（重要！）
1. **启用无障碍服务**：
   - 设置 → 辅助功能 → 自动连点器 → 开启
2. **授予悬浮窗权限**：
   - 设置 → 应用 → 特殊访问权限 → 显示在其他应用上层

---

## ❓ 需要帮助？

如果您在构建过程中遇到问题：
1. 参考 `INSTALL_GUIDE.md` 详细说明
2. 检查 `README.md` 使用指南
3. 确保按步骤正确操作

**项目代码完整，只需要构建环境即可生成APK！**

---

**建议：现在就下载Android Studio，10分钟后您就能有可用的APK了！** 🚀
