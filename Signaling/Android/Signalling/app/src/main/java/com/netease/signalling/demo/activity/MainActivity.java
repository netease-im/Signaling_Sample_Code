package com.netease.signalling.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.constant.ChannelType;
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo;
import com.netease.signalling.demo.R;
import com.netease.signalling.demo.model.CacheInfo;
import com.netease.signalling.demo.utils.ToastHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editRoom;

    public static final int MAX_LENGTH = 8;

    private ChannelBaseInfo channelInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheInfo.setBusy(false);
        setContentView(R.layout.activity_main);
        findViews();
    }


    private void findViews() {
        editRoom = findViewById(R.id.edt_call_account);
        findViewById(R.id.room_info).setOnClickListener(this);
        findViewById(R.id.tv_create).setOnClickListener(this);
        findViewById(R.id.tv_enter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_info: {
                NIMClient.getService(AuthService.class).logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }
            case R.id.tv_create:
                create();
                break;
            case R.id.tv_enter:
                enter();
                break;
        }
    }

    private void enter() {
        String channelName = editRoom.getText().toString().trim();
        if (TextUtils.isEmpty(channelName)/* || roomId.trim().length() > MAX_LENGTH*/) {
            ToastHelper.showToast(this, "请输入房间号码");
            return;
        }
        NIMClient.getService(SignallingService.class).queryChannelInfo(channelName).setCallback(
                new RequestCallbackWrapper<ChannelBaseInfo>() {

                    @Override
                    public void onResult(int i, ChannelBaseInfo channelBaseInfo, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            channelInfo = channelBaseInfo;
                            goRoom();
                            ToastHelper.showToast(MainActivity.this, "加入成功");
                        } else {
                            ToastHelper.showToast(MainActivity.this, "加入失败， code = " + i +
                                                                     (throwable == null ? "" : ", throwable = " +
                                                                                               throwable.getMessage()));
                        }
                    }
                });
    }

    /**
     * 创建房间
     */
    private void create() {
        String roomId = editRoom.getText().toString().trim();
        if (TextUtils.isEmpty(roomId) || roomId.trim().length() > MAX_LENGTH) {
            ToastHelper.showToast(this, "请输入房间号码，最多8位");
            return;
        }
        NIMClient.getService(SignallingService.class).create(ChannelType.CUSTOM, roomId, "").setCallback(
                new RequestCallbackWrapper<ChannelBaseInfo>() {

                    @Override
                    public void onResult(int i, ChannelBaseInfo channelBaseInfo, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            channelInfo = channelBaseInfo;
                            goRoom();
                            ToastHelper.showToast(MainActivity.this, "创建成功");
                        } else {
                            ToastHelper.showToast(MainActivity.this, "创建失败， code = " + i +
                                                                     (throwable == null ? "" : ", throwable = " +
                                                                                               throwable.getMessage()));
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void goRoom() {
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra(RoomActivity.CHANNEL_INFO, channelInfo);
        startActivity(intent);
    }
}
