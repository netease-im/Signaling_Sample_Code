# 信令SDK Android端示例代码

示例代码中需要开发者自己填写信令SDK的`appKey`，请参考信令SDK开发手册中的`接入指南`获得appKey。

第三方SDK的appKey也需要开发者自己填写，请在第三方网站上注册并获得`appKey`。

需要填写`appKey`的文件有

- `Signaling/AndroidManifest/com.netease.nim.appKey`
- `Signalling-Agora-Group/AndroidManifest/com.netease.nim.appKey`
- `Signalling-Agora-p2p/AndroidManifest/com.netease.nim.appKey`

需要填写第三方`appKey`的文件有

- `strings/agora_app_id & private_app_id`

*注：* Signaling 示范Demo 仅需要填写NIMSdk的Appkey，其他的还需要填写声网AppID

## 运行DEMO

0. 首先安装AndroidStudio，配置环境
1. 在对应的目录中点击 `build.gradle` 工程文件进入工程

## 目录详情

- `Signaling` 只有信令SDK的使用展示(**建议先看这个**)
- `Signalling-Agora-p2p` 信令SDK搭配声网音视频SDK做的双人通话示例代码
- `Signalling-Agora-Group` 信令SDK搭配声网音视频SDK做的视频会议示例代码

