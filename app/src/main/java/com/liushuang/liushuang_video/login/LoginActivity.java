package com.liushuang.liushuang_video.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private ImageView mClose;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mLogin;
    private TextView mRegister;
    private SharedPreferences mSharedPreferences;

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "password";
    private String username;
    private String password;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        initIntent(getIntent());
        mClose = bindViewId(R.id.id_iv_close);
        mUsername = bindViewId(R.id.edt_username);
        mPassword = bindViewId(R.id.edt_pwd);
        mLogin = bindViewId(R.id.btn_login);
        mRegister = bindViewId(R.id.btn_register);
    }

    @Override
    protected void initData() {
        mSharedPreferences = getSharedPreferences(RegisterActivity.USER_INFO_PREFENCES, MODE_PRIVATE);
        username = mSharedPreferences.getString(RegisterActivity.USER_NAME, username);
        password = mSharedPreferences.getString(RegisterActivity.PASSWORD, password);
        if (username != null && password != null) {
            mUsername.setText(username);
            mPassword.setText(password);
        }

        initEvent();
    }

    private void initEvent() {
        mClose.setOnClickListener(new View.OnClickListener() {
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

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        setIntent(intent);
        Intent intent1 = getIntent();
        if (intent1 != null) {
            username = intent1.getStringExtra(KEY_USERNAME);
            password = intent1.getStringExtra(KEY_PASSWORD);

            if (username != null) {
                mUsername.setText(username);
            }

            if (password != null) {
                mPassword.setText(password);
            }
        }
    }

    public static void launch(Context context, String registerName, String password){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_USERNAME, registerName);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);

    }
}
