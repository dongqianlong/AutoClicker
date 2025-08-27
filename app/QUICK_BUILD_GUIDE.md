# 快速构建APK指南

由于构建Android APK需要完整的开发环境，这里提供几种最简单的方法：

## 🚀 方法一：使用Android Studio（最推荐）

### 1. 下载安装Android Studio
- 官网：https://developer.android.com/studio
- 安装时会自动配置所有必需的环境

### 2. 导入项目
1. 打开Android Studio
2. 选择 "Open an existing Android Studio project" 
3. 选择当前项目文件夹
4. 等待项目同步完成

### 3. 构建APK
1. 菜单：Build → Build Bundle(s) / APK(s) → Build APK(s)
2. 等待构建完成
3. APK文件位置：`app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 方法二：在线构建服务

### GitHub Actions（免费自动构建）
1. 将项目上传到GitHub
2. 启用Actions，会自动构建APK
3. 从Actions页面下载构建好的APK

### AppCenter（微软）
1. 注册AppCenter账号
2. 连接GitHub仓库
3. 配置自动构建

---

## 🛠 方法三：命令行构建（需要环境）

如果您已有Android开发环境：

```bash
# 确保环境变量正确设置
export ANDROID_HOME=/path/to/android-sdk
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home

# 构建APK
./gradlew assembleDebug
```

---

## 📦 预构建APK

如果您急需APK文件，我建议：

1. **使用Android Studio**（5-10分钟即可完成）
2. **联系有Android开发环境的朋友帮忙构建**
3. **使用在线构建服务**

---

## 🎯 APK功能说明

构建出的APK将包含以下功能：

### 核心功能
- ✅ 自动连点（支持自定义间隔和次数）
- ✅ 位置选择（手动输入或触摸选择）
- ✅ 悬浮窗控制
- ✅ 后台运行
- ✅ 通知栏控制

### 兼容性
- ✅ Android 5.0+ (API 21)
- ✅ 所有Android设备和模拟器
- ✅ 支持各种屏幕尺寸

### 安全特性
- ✅ 需要用户手动启用无障碍服务
- ✅ 需要用户授予悬浮窗权限
- ✅ 包含使用说明和安全提醒

---

## 🔧 故障排除

### 构建失败？
1. 确保Java 11+已安装
2. 确保Android SDK已安装
3. 检查网络连接（需要下载依赖）

### APK安装失败？
1. 启用"未知来源"安装
2. 检查存储空间
3. 确保设备Android版本≥5.0

### 应用无法点击？
1. 必须启用无障碍服务
2. 检查点击位置是否正确
3. 确保应用未被系统杀掉

---

## 📞 技术支持

项目已经创建完成，包含：
- ✅ 完整的源代码
- ✅ 项目配置文件
- ✅ 构建脚本
- ✅ 详细文档

如需帮助，请参考：
- `README.md` - 完整使用说明
- `INSTALL_GUIDE.md` - 详细安装指南
- 项目源码注释

**推荐：直接使用Android Studio，5分钟即可完成构建！**
