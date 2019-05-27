# 信令SDK iOS端示例代码

示例代码中需要开发者自己填写信令SDK的`appKey`，请参考信令SDK开发手册中的`接入指南`获得appKey。

第三方SDK的appKey也需要开发者自己填写，请在第三方网站上注册并获得`appKey`。

需要填写`appKey`的文件有

- `Signaling/NTESDemoConfig.h`
- `Signalling-Agora-Group/NTESDemoConfig.h`
- `Signalling-Agora-p2p/NTESDemoConfig.h`

*注：* Signaling 示范Demo 仅需要填写NIMSdk的Appkey，其他的还需要填写声网AppID

## 运行DEMO

0. 首先执行全局安装`pod install`
1. 在对应的目录中点击 `.xcworkspace` 工程文件进入工程

## 目录详情

- `NTESDemoConfig.h` demo的配置文件，在这里填写需要的appkey
- `Signaling` 只有信令SDK的使用展示(**建议先看这个**)
- `Signalling-Agora-p2p` 信令SDK搭配声网音视频SDK做的双人通话示例代码
- `Signalling-Agora-Group` 信令SDK搭配声网音视频SDK做的视频会议示例代码

