package com.netease.signalling.demo.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class CacheInfo {

    private static String sAppKey;

    private static Context sContext;

    private static String sAccount;
    private static boolean sIsBusy;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }


    public static String getAccount() {
        return sAccount;
    }


    public static String getAppKey() {
        if (sAppKey == null) {
            readAppKey();
        }
        return sAppKey;
    }

    public static boolean isBusy() {
        return sIsBusy;
    }

    public static void setBusy(boolean isBusy) {
        sIsBusy = isBusy;
    }

    public static void setAccount(String account) {
        sAccount = account;
    }


    private static void readAppKey() {
        try {
            ApplicationInfo appInfo = sContext.getPackageManager().getApplicationInfo(sContext.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                sAppKey = appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
