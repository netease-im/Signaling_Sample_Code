package com.netease.signalling.demo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastHelper {

    private static Toast sToast;

    private ToastHelper() {

    }

    public static void showToast(Context context, String text) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
        }
        sToast.show();
    }


}
