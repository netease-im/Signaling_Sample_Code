package com.netease.signalling.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int REQUEST_PERMISSIONS = 1001;
    private static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private EditText edtLoginUserAccount;
    private EditText edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
        checkAllPermission();
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

    private void checkAllPermission() {

        ArrayList<String> permissionDenied = new ArrayList<>();
        for (String permission : ALL_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                permissionDenied.add(permission);
            }
        }
        if (permissionDenied.size() == 0) {
            return;
        }
        String[] deniedArr = new String[0];
        deniedArr = permissionDenied.toArray(deniedArr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(deniedArr, REQUEST_PERMISSIONS);
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
        boolean isDemo = CacheInfo.getAppKey().contains("0c8e8a7") || CacheInfo.getAppKey().contains("45c6af3");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSIONS) {
            return;
        }
        int len = grantResults.length;
        if (len == 0) {
            return;
        }
        for (int index = 0; index < len; ++index) {
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast(this, permissions[index] + " 权限获取失败");
                finish();
                return;
            }
        }
    }
}
