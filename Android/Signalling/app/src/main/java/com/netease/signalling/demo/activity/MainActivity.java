package com.netease.signalling.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.SignallingServiceObserver;
import com.netease.nimlib.sdk.avsignalling.builder.CallParamBuilder;
import com.netease.nimlib.sdk.avsignalling.builder.InviteParamBuilder;
import com.netease.nimlib.sdk.avsignalling.constant.ChannelType;
import com.netease.nimlib.sdk.avsignalling.constant.InviteAckStatus;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.InviteAckEvent;
import com.netease.nimlib.sdk.avsignalling.event.InvitedEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo;
import com.netease.nimlib.sdk.avsignalling.model.ChannelFullInfo;
import com.netease.signalling.demo.R;
import com.netease.signalling.demo.model.CacheInfo;
import com.netease.signalling.demo.utils.ToastHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editAccount;
    private TextView tvCallingHint;
    private View beInviteContainer;
    private TextView tvBeInviteHint;
    private TextView tvAcceptInvite;
    private TextView tvRejectInvite;
    private long selfUid;
    private InvitedEvent invitedEvent;
    private ChannelBaseInfo channelInfo;

    private Observer<ChannelCommonEvent> onlineObserver = new Observer<ChannelCommonEvent>() {
        @Override
        public void onEvent(ChannelCommonEvent event) {
            onlineEvent(event);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheInfo.setBusy(false);
        selfUid = System.nanoTime();
        setContentView(R.layout.activity_main);
        setupView();
        registerObserver(true);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_login_out) {
            NIMClient.getService(AuthService.class).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if (id == R.id.tv_call) {
            call();
            return;
        }
        if (id == R.id.tv_accept_invite) {
            acceptInvite();
            return;
        }

        if (id == R.id.tv_reject_invite) {
            rejectInvite("任性不想接");
            return;
        }

    }

    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();
    }

    private void setupView() {
        editAccount = findViewById(R.id.edt_call_account);
        tvCallingHint = findViewById(R.id.tv_calling_hint);
        beInviteContainer = findViewById(R.id.rl_be_invite_container);
        tvBeInviteHint = findViewById(R.id.tv_be_invite_hint);
        tvAcceptInvite = findViewById(R.id.tv_accept_invite);
        tvRejectInvite = findViewById(R.id.tv_reject_invite);

        findViewById(R.id.tv_login_out).setOnClickListener(this);
        findViewById(R.id.tv_call).setOnClickListener(this);
        tvAcceptInvite.setOnClickListener(this);
        tvRejectInvite.setOnClickListener(this);

    }

    private void registerObserver(boolean register) {
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(onlineObserver, register);
    }

    /**
     * 处理在线通知事件
     */
    private void onlineEvent(ChannelCommonEvent event) {

        // 对方应答了你的邀请
        if (event instanceof InviteAckEvent) {
            InviteAckEvent ackEvent = (InviteAckEvent) event;
            if (ackEvent.getAckStatus() == InviteAckStatus.ACCEPT) {
                goChatting();
            } else {
                ToastHelper.showToast(this, "对方拒绝了你的邀请");
            }
            tvCallingHint.setVisibility(View.GONE);
            return;
        }

        //你被别人邀请了
        if (event instanceof InvitedEvent) {
            beInvited(((InvitedEvent) event));
            return;
        }
    }

    private void beInvited(InvitedEvent event) {
        invitedEvent = event;
        if (CacheInfo.isBusy()) {
            ToastHelper.showToast(this, "有人邀请你 ， 已主动拒绝");
            rejectInvite("正忙");
            return;
        }
        beInviteContainer.setVisibility(View.VISIBLE);
        enableClick(true);
        tvBeInviteHint.setText(event.getFromAccountId() + "邀请你通话");
    }

    /**
     * 接受对方的的邀请并加入频道
     */
    private void acceptInvite() {
        enableClick(false);
        InviteParamBuilder inviteParam = new InviteParamBuilder(invitedEvent.getChannelBaseInfo().getChannelId(),
                invitedEvent.getFromAccountId(),
                invitedEvent.getRequestId());
        NIMClient.getService(SignallingService.class).acceptInviteAndJoin(inviteParam, selfUid).setCallback(new RequestCallbackWrapper<ChannelFullInfo>() {
            @Override
            public void onResult(int code, ChannelFullInfo channelFullInfo, Throwable throwable) {
                enableClick(true);
                //参考官方文档中关于api以及错误码的说明
                if (code == ResponseCode.RES_SUCCESS) {
                    ToastHelper.showToast(MainActivity.this, "接收邀请成功");
                    channelInfo = channelFullInfo.getChannelBaseInfo();
                    goChatting();
                } else {
                    ToastHelper.showToast(MainActivity.this, "接收邀请返回的结果 ， code = " + code +
                            (throwable == null ? "" : ", throwable = " + throwable.getMessage()));
                }
                beInviteContainer.setVisibility(View.GONE);
            }
        });
    }

    private void enableClick(boolean enable) {
        beInviteContainer.setSelected(enable);
        tvAcceptInvite.setClickable(enable);
        tvRejectInvite.setClickable(enable);
    }

    /**
     * 拒绝对方的邀请
     */
    private void rejectInvite(String customInfo) {
        beInviteContainer.setVisibility(View.GONE);
        enableClick(true);
        InviteParamBuilder inviteParam = new InviteParamBuilder(invitedEvent.getChannelBaseInfo().getChannelId(),
                invitedEvent.getFromAccountId(),
                invitedEvent.getRequestId());
        if (!TextUtils.isEmpty(customInfo)) {
            inviteParam.customInfo(customInfo);
        }
        NIMClient.getService(SignallingService.class).rejectInvite(inviteParam);
    }


    /**
     * 呼叫对方
     */
    private void call() {
        String account = editAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastHelper.showToast(this, "请输入对方帐号");
            return;
        }
        tvCallingHint.setText("正在呼叫" + account);
        tvCallingHint.setVisibility(View.VISIBLE);
        String requestId = String.valueOf(System.currentTimeMillis());
        CallParamBuilder paramBuilder = new CallParamBuilder(ChannelType.VIDEO, account, requestId);
        paramBuilder.selfUid(selfUid);
        NIMClient.getService(SignallingService.class).call(paramBuilder).setCallback(new RequestCallbackWrapper<ChannelFullInfo>() {
            @Override
            public void onResult(int code, ChannelFullInfo channelFullInfo, Throwable throwable) {
                //参考官方文档中关于api以及错误码的说明
                if (code == ResponseCode.RES_SUCCESS) {
                    channelInfo = channelFullInfo.getChannelBaseInfo();
                    ToastHelper.showToast(MainActivity.this, "邀请成功，等待对方接听");
                } else {
                    tvCallingHint.setVisibility(View.GONE);
                    ToastHelper.showToast(MainActivity.this, "邀请返回的结果 ， code = " + code +
                            (throwable == null ? "" : ", throwable = " + throwable.getMessage()));
                }
            }
        });


    }

    private void goChatting() {
        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra(ChattingActivity.CHANNEL_INFO, channelInfo);
        intent.putExtra(ChattingActivity.SELF_UID, selfUid);
        startActivity(intent);
    }
}
