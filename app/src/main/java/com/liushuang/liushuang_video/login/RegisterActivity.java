package com.liushuang.liushuang_video.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private EditText mRegisterName;
    private EditText mPassword;
    private EditText mRePassword;
    private TextView mBtnRegister;

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