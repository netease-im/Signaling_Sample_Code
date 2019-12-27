package com.netease.signalling.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.EGLContext;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.LongSparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.SignallingServiceObserver;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.ControlEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo;
import com.netease.nrtc.sdk.NRtc;
import com.netease.nrtc.sdk.NRtcEx;
import com.netease.nrtc.sdk.common.EglContextWrapper;
import com.netease.nrtc.sdk.common.statistics.RtcStats;
import com.netease.nrtc.sdk.video.CameraCapturer;
import com.netease.nrtc.sdk.video.SurfaceViewRender;
import com.netease.nrtc.sdk.video.VideoCapturerFactory;
import com.netease.nrtc.video.render.IVideoRender;
import com.netease.signalling.demo.R;
import com.netease.signalling.demo.call.SimpleNRtcCallback;
import com.netease.signalling.demo.model.CacheInfo;
import com.netease.signalling.demo.utils.ToastHelper;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String CHANNEL_INFO = "channel_info";
    public static final String SELF_UID = "self_uid";

    private static final int REQUEST_PERMISSIONS = 1001;

    private FrameLayout flRemotePreview;
    private FrameLayout flLocalPreview;
    private View ivMute;
    private View ivCloseCamera;

    private NRtcEx nrtcEx;
    private EglContextWrapper eglContext;
    protected CameraCapturer cameraCapturer;
    private LongSparseArray<IVideoRender> userRenders = new LongSparseArray<>();

    private ChannelBaseInfo channelInfo;
    private long selfUid;
    private long remoteUid;
    private boolean isCallEstablished;

    private static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private Observer<ChannelCommonEvent> onlineObserver = new Observer<ChannelCommonEvent>() {
        @Override
        public void onEvent(ChannelCommonEvent event) {
            onlineEvent(event);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CacheInfo.setBusy(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chationg);
        setupView();
        parseIntent();
        checkAllPermission();
        registerObserver(true);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        selfUid = intent.getLongExtra(SELF_UID, 0);
        channelInfo = (ChannelBaseInfo) intent.getSerializableExtra(CHANNEL_INFO);
    }

    private void setupView() {
        flRemotePreview = findViewById(R.id.fl_remote_render);
        flLocalPreview = findViewById(R.id.fl_local_render);
        ivMute = findViewById(R.id.iv_mute);
        ivCloseCamera = findViewById(R.id.iv_close_camera);

        ivMute.setOnClickListener(this);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
        ivCloseCamera.setOnClickListener(this);
    }


    private void createNRTC() {
        nrtcEx = (NRtcEx) NRtc.create(this, CacheInfo.getAppKey(), new InnerNRtcCallback());
        eglContext = EglContextWrapper.createEglContext();
        if (eglContext.isEGL14Supported()) {
            nrtcEx.updateSharedEGLContext((EGLContext) eglContext.getEglContext());
        } else {
            nrtcEx.updateSharedEGLContext((javax.microedition.khronos.egl.EGLContext) eglContext.getEglContext());
        }
    }

    private void registerObserver(boolean register) {
        //在线通知
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(onlineObserver, register);
    }


    /**
     * 收到在线通知
     */
    private void onlineEvent(ChannelCommonEvent event) {
        if (event instanceof ControlEvent) {
            String account = event.getFromAccountId();
            String customInfo = event.getCustomInfo();
            ToastHelper.showToast(this, "收到来自于 " + account + " 的控制命令 ，" + customInfo);
        }
    }

    private void checkAllPermission() {

        ArrayList<String> permissionDenied = new ArrayList<>();
        for (String permission : ALL_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                permissionDenied.add(permission);
            }
        }
        if (permissionDenied.size() == 0) {
            allPermissionGranted();
            return;
        }
        String[] deniedArr = new String[0];
        deniedArr = permissionDenied.toArray(deniedArr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(deniedArr, REQUEST_PERMISSIONS);
        }
    }

    private void allPermissionGranted() {
        ToastHelper.showToast(this, " All permission granted ");
        createNRTC();
        enableNrtc();
    }

    @Override
    protected void onPause() {
        nrtcEx.stopVideoPreview();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        registerObserver(false);
        shutDown(false);
        super.onDestroy();
    }

    private void shutDown(boolean needFinish) {
        if (!CacheInfo.isBusy()) {
            return;
        }
        CacheInfo.setBusy(false);
        //离开 信令频道
        NIMClient.getService(SignallingService.class).leave(channelInfo.getChannelId(), false, null);
        //离开音视频房间
        nrtcEx.leaveChannel();
        nrtcEx.dispose();
        eglContext.release();
        if (needFinish) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.iv_cancel) {
            shutDown(true);
            return;
        }

        if (!isCallEstablished) {
            ToastHelper.showToast(this, "音频通道还未完全建立");
            return;
        }

        // 关闭/打开本地语音发送
        if (id == R.id.iv_mute) {
            boolean isMute = !ivMute.isSelected();
            nrtcEx.muteLocalAudioStream(isMute);
            ivMute.setSelected(isMute);
            NIMClient.getService(SignallingService.class).sendControl(channelInfo.getChannelId(), null, isMute ? "我关闭语音" : "我打开了语音");
            return;
        }

        // 关闭/打开地视频发送
        if (id == R.id.iv_close_camera) {
            boolean isClose = !ivCloseCamera.isSelected();
            nrtcEx.muteLocalVideoStream(isClose);
            ivCloseCamera.setSelected(isClose);
            NIMClient.getService(SignallingService.class).sendControl(channelInfo.getChannelId(), null, isClose ? "我关闭视频" : "我打开了视频");
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSIONS) {
            return;
        }
        int len = grantResults.length;
        if (len == 0) {
            allPermissionGranted();
            return;
        }
        for (int index = 0; index < len; ++index) {
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast(this, permissions[index] + " 权限获取失败");
            }
        }
    }


    private void enableNrtc() {

        cameraCapturer = VideoCapturerFactory.createCameraCapturer();
        nrtcEx.setupVideoCapturer(cameraCapturer);
        nrtcEx.enableVideo();
        IVideoRender videoRender = new SurfaceViewRender(this);
        userRenders.put(selfUid, videoRender);
        nrtcEx.setupLocalVideoRenderer(videoRender, 0, false);

        flRemotePreview.removeAllViews();
        ((SurfaceView) videoRender).setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        flRemotePreview.addView((SurfaceView) videoRender);
        nrtcEx.startVideoPreview();

        nrtcEx.joinChannel(null, channelInfo.getChannelId(), selfUid);
    }


    private class InnerNRtcCallback extends SimpleNRtcCallback {


        // 会话建立，当除自己外的第一个用户加入进来时触发.
        @Override
        public void onCallEstablished() {
            super.onCallEstablished();
            flRemotePreview.removeAllViews();
            flLocalPreview.removeAllViews();
            SurfaceViewRender localRender = (SurfaceViewRender) userRenders.get(selfUid);
            localRender.setZOrderMediaOverlay(true);
            flLocalPreview.addView(localRender);
            isCallEstablished = true;
        }

        @Override
        public void onUserJoined(long uid) {
            super.onUserJoined(uid);
            remoteUid = uid;
            IVideoRender remoteRender = userRenders.get(uid);
            if (remoteRender == null) {
                remoteRender = new SurfaceViewRender(ChattingActivity.this);
                ((SurfaceView) remoteRender).setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                userRenders.put(uid, remoteRender);
                ((SurfaceViewRender) remoteRender).setZOrderMediaOverlay(false);
            }
            nrtcEx.setupRemoteVideoRenderer(remoteRender, uid, 0, false);

            flRemotePreview.removeAllViews();
            flRemotePreview.addView((SurfaceView) remoteRender);
        }

        @Override
        public void onUserLeft(long uid, RtcStats stats, int event) {
            super.onUserLeft(uid, stats, event);
            //目前只是p2p ， 如果多人的话，不能这样写
            if (remoteUid == uid) {
                ToastHelper.showToast(ChattingActivity.this, "对方离开了");
                shutDown(true);
            }
        }
    }
}
