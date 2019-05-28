# 信令SDK iOS端示例代码

信令SDK是网易云信提供的稳定可靠的信令通道，可用于搭建音视频场景下的呼叫邀请机制。信令SDK目前兼容市面上所有主流的音视频SDK，呼叫到达率高达100%，支持离线推送和自定义控制消息，是功能最丰富、稳定性最高的信令SDK之一。

信令SDK目前支持以下功能：
- 呼叫邀请
- 房间管理
- 用户管理
- 离线消息同步
- 多端同步
- 自定义控制信令

更多信令功能介绍参见：（[官网信令产品页](https://hubble.netease.com/sl/aaacQC)）

信令开发文档参见：（[官网信令开发文档页](https://hubble.netease.com/sl/aaacQE)）

示例代码中需要开发者自己填写信令SDK的appKey，立即注册获取appKey：（[登陆页面](https://hubble.netease.com/sl/aaacQD)）

若克隆仓库失败，可以使用如下命令克隆
```bash
git clone https://github.com/netease-im/Signaling_Sample_Code.git -v --depth 1
```

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

