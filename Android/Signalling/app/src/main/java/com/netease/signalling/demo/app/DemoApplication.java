package com.netease.signalling.demo.app;

import android.app.Application;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.signalling.demo.model.CacheInfo;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //SDK初始化
        NIMClient.init(this, null, null);

        CacheInfo.init(this);
    }
}
