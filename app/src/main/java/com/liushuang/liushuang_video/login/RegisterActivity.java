package com.liushuang.liushuang_video.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private EditText mRegisterName;
    private EditText mPassword;
    private EditText mRePassword;
    private TextView mBtnRegister;
    private SharedPreferences mSharedPreferences;

    public static final String USER_INFO_PREFENCES = "userinfo";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle("注册");

        mRegisterName = bindViewId(R.id.id_et_registerName);
        mPassword = bindViewId(R.id.id_et_password);
        mRePassword = bindViewId(R.id.id_et_rePassword);
        mBtnRegister = bindViewId(R.id.id_btn_register);
    }

    @Override
    protected void initData() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerName = mRegisterName.getText().toString();
                String password = mPassword.getText().toString();
                String rePassword = mRePassword.getText().toString();

                if (TextUtils.isEmpty(registerName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)){
                    Toast.makeText(RegisterActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)){
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                mSharedPreferences = getSharedPreferences(USER_INFO_PREFENCES, MODE_PRIVATE);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(USER_NAME, registerName);
                edit.putString(PASSWORD, password);
                edit.commit();

                LoginActivity.launch(RegisterActivity.this, registerName, password);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}