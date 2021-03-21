package com.liushuang.liushuang_video.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.home.HomeActivity;

public class LoginActivity extends BaseActivity {

    private ImageView mClose;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mLogin;
    private TextView mRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mClose = bindViewId(R.id.id_iv_close);
        mUsername = bindViewId(R.id.edt_username);
        mPassword = bindViewId(R.id.edt_pwd);
        mLogin = bindViewId(R.id.btn_login);
        mRegister = bindViewId(R.id.btn_register);
    }

    @Override
    protected void initData() {
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}