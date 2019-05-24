package com.netease.signalling.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avsignalling.SignallingService;
import com.netease.nimlib.sdk.avsignalling.SignallingServiceObserver;
import com.netease.nimlib.sdk.avsignalling.event.ChannelCommonEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserJoinEvent;
import com.netease.nimlib.sdk.avsignalling.event.UserLeaveEvent;
import com.netease.nimlib.sdk.avsignalling.model.ChannelBaseInfo;
import com.netease.nimlib.sdk.avsignalling.model.ChannelFullInfo;
import com.netease.nimlib.sdk.avsignalling.model.MemberInfo;
import com.netease.signalling.demo.R;
import com.netease.signalling.demo.model.CacheInfo;
import com.netease.signalling.demo.model.MemberInfoImpl;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "RoomActivity";

    public static final String CHANNEL_INFO = "channel_info";

    private TextView roomInfo;

    private TextView leaveRoom;

    private RecyclerView recyclerView;

    private ChannelBaseInfo channelInfo;

    private String channelId;

    private long selfUid;

    private RecyclerView.Adapter adapter;

    private List<MemberInfoImpl> memberInfos;

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
        setContentView(R.layout.activity_room);
        parseIntent();
        findViews();
        setupViews();
        joinRoom();
        initRV();
        registerObserver(true);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        channelInfo = (ChannelBaseInfo) intent.getSerializableExtra(CHANNEL_INFO);
        channelId = channelInfo.getChannelId();
    }

    private void findViews() {
        roomInfo = findViewById(R.id.room_info);
        leaveRoom = findViewById(R.id.tv_leave);
        recyclerView = findViewById(R.id.member_info);
        leaveRoom.setOnClickListener(this);
    }

    private void joinRoom() {
        selfUid = System.nanoTime();
        NIMClient.getService(SignallingService.class).join(channelId, selfUid, "", false).setCallback(
                new RequestCallbackWrapper<ChannelFullInfo>() {

                    @Override
                    public void onResult(int i, ChannelFullInfo channelFullInfo, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            memberInfos.add(new MemberInfoImpl(MemberInfoImpl.JOIN_TYPE, CacheInfo.getAccount()));
                            adapter.notifyDataSetChanged();
                            showToast("加入房间成功");
                        } else if (i == ResponseCode.RES_CHANNEL_MEMBER_HAS_EXISTS) {
                            showToast("已经在房间中");
                        } else {
                            showToast("加入房间失败 code=" + i);
                        }
                    }
                });
    }

    private void initRV() {
        memberInfos = new ArrayList<>();
        adapter = new RecyclerView.Adapter() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_info_item, viewGroup,
                                                                             false);
                return new RoomViewHolder(v);
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                MemberInfoImpl member = memberInfos.get(i);
                RoomViewHolder viewHolder1 = (RoomViewHolder) viewHolder;
                StringBuilder sb = new StringBuilder();
                if (TextUtils.equals(member.account, CacheInfo.getAccount())) {
                    sb.append("你");
                } else {
                    sb.append(member.account);
                }
                if (member.type == MemberInfoImpl.JOIN_TYPE) {
                    sb.append("加入了房间");
                } else {
                    sb.append("离开了房间");
                }
                viewHolder1.textView.setText(sb.toString());
            }

            @Override
            public int getItemCount() {
                return memberInfos.size();
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class RoomViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.member_info);
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private void setupViews() {
        roomInfo.setText(channelInfo != null ? channelInfo.getChannelName() : "");
    }

    private void registerObserver(boolean register) {
        //在线通知
        NIMClient.getService(SignallingServiceObserver.class).observeOnlineNotification(onlineObserver, register);
    }


    /**
     * 收到在线通知
     */
    private void onlineEvent(ChannelCommonEvent event) {
        if (event instanceof UserJoinEvent) {
            MemberInfo memberInfo = ((UserJoinEvent) event).getMemberInfo();
            String accountId = memberInfo != null ? memberInfo.getAccountId() : "";
            memberInfos.add(new MemberInfoImpl(MemberInfoImpl.JOIN_TYPE, accountId));
        } else if (event instanceof UserLeaveEvent) {
            memberInfos.add(new MemberInfoImpl(MemberInfoImpl.LEAVE_TYPE, event.getFromAccountId()));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        registerObserver(false);
        leave();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_leave:
                finish();
                break;
        }
    }

    private void leave() {
        NIMClient.getService(SignallingService.class).leave(channelId, false, null).setCallback(
                new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            showToast("离开房间成功");
                        } else {
                            showToast("离开房间失败 code=" + i);
                        }
                    }
                });
    }
}
