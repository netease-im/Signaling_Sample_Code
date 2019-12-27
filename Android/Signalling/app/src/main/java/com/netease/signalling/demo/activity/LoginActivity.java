package com.netease.signalling.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.signalling.demo.R;
import com.netease.signalling.demo.model.CacheInfo;
import com.netease.signalling.demo.utils.BaseUtil;
import com.netease.signalling.demo.utils.ToastHelper;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUserAccount;
    private EditText edtLoginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        edtLoginUserAccount = findViewById(R.id.edt_login_user_account);
        edtLoginPassword = findViewById(R.id.edt_login_user_password);
        findViewById(R.id.tv_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.tv_login) {
            String account = edtLoginUserAccount.getText().toString();
            String password = edtLoginPassword.getText().toString();
            login(account, password);
        }

    }


    private boolean isInLogin;

    private void login(final String account, String password) {

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            ToastHelper.showToast(this, "相关信息不能为空");
            return;
        }
        if (isInLogin) {
            ToastHelper.showToast(this, "正在登录中，请勿重复提交");
            return;
        }
        boolean isDemo = CacheInfo.getAppKey().contains("0c8e8a7");
        password = isDemo ? BaseUtil.getStringMD5(password) : password;
        isInLogin = true;
        NIMClient.getService(AuthService.class).login(new LoginInfo(account, password)).setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo o) {
                isInLogin = false;
                CacheInfo.setAccount(account);
                ToastHelper.showToast(LoginActivity.this, "登录成功");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code) {
                isInLogin = false;
                ToastHelper.showToast(LoginActivity.this, "登录失败，code = " + code);
            }

            @Override
            public void onException(Throwable throwable) {
                isInLogin = false;
                ToastHelper.showToast(LoginActivity.this, "登录异常");
            }
        });
    }
}
