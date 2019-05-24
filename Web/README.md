# 信令SDK Web端示例代码

示例代码中需要开发者自己填写信令SDK的`appKey`，请参考信令SDK开发手册中的`接入指南`获得appKey。

第三方SDK的appKey也需要开发者自己填写，请在第三方网站上注册并获得`appKey`。

需要填写`appKey`的文件有

- `signaling/index.js`
- `signaling_agora_p2p/index.js`
- `signaling_agora_meeting/index.js`

需要填写appKey的地方均有`WRITE_APPKEY_HERE`注释。

## 运行DEMO

0. 首先执行全局安装`npm install -g serve` 或者 `npm install -g http-server`
1. 在当前目录下执行 `serve` 或者 `http-server`，即可在本地浏览器访问Demo

## 目录详情

- `libs` 用到的js库
- `signaling` 只有信令SDK的使用展示(**建议先看这个**)
- `signaling_agora_p2p` 信令SDK搭配声网音视频SDK做的双人通话示例代码
- `signaling_agora_meeting` 信令SDK搭配声网音视频SDK做的视频会议示例代码



