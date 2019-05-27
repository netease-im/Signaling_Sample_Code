# 云信 IM 独立信令 + 声网音视频示例程序 for PC

该示例代码演示了如何使用云信 IM 的独立信令账号体系与多方建立联系通道，并整合了第三方音视频能力通过信令通道沟通建立连接。

## 如何使用

 - [https://yunxin.163.com/](https://yunxin.163.com/) 注册并登录云信账号，在左侧导航应用一栏中创建自己的应用
 - 点击生成的应用，在应用页面上方点击 `App Key 管理`，复制 AppKey 到示例项目 main.cpp 中赋值给 `LoginManager::kAppKey`
 - 返回应用页面在功能管理->IM免费版右侧点击账号管理，生成部分测试账号提供测试使用
 - 注册声网账号并创建应用，复制应用对应的 App ID 到 main.cpp 中赋值给 `AgoraRtcEngine::kAppId`（独立信令演示不需要）
 - 使用 Qt5 以上版本（MSVC 编译器）打开目录下的 signaling.pro 生成并编译项目即可测试使用

## 目录结构

目录 | 作用
---- | ---
libs | 包含云信信令 SDK 和 声网 RTC SDK
shared | 共享 Qt Quick 组件和一些工具类
signaling | 单独云信信令通道演示
signaling_agora_p2p | 点对点通过信令通道呼叫对方+第三方音视频聊天演示
signaling_agora_meeting | 会议模式通过信令通道邀请对方+第三方音视频聊天演示
third_party | 第三方依赖库（主要由云信 C++ 封装层使用）

## 程序发布

**发布流程**

1. 生成所有示例程序并复制到一个固定目录
2. 复制 libs 目录下所有 .dll 动态链接库（包含云信信令 SDK 和第三方音视频 SDK）到执行程序目录
3. 使用 windeployqt 发布程序（需携带 qmldir 参数）

如：

```
d:\Qt\Qt5.12.3\5.12.3\msvc2017\bin\windeployqt.exe signaling.exe --qmldir d:\Qt\Qt5.12.3\5.12.3\msvc2017\qml\
```

**SDK更新**

如果您希望使用新版本的云信 SDK，可到 [https://yunxin.163.com/im-sdk-demo](https://yunxin.163.com/im-sdk-demo) 下载 Windows PC C/C++ SDK，解压后将 x86_dlls 目录下的所有文件复制到 libs 目录即可。

**运行时库**

云信 SDK 依赖 MSVC2017 运行库，如果在没有安装 MSVC2017 运行库的环境，您需要将安装 MSVC2017 运行库或将 SDK x86_dlls\redist_packages 目录下的所有文件复制到 libs 目录才能在这类环境中正常加载 SDK。


