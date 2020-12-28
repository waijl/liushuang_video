package com.liushuang.liushuang_video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * app启动时的主入口activity
 */
public class MainActivity extends Activity {

    private SharedPreferences mSharedPreferences;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private static final int ENTEER_DURATION = 2000;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                case GO_HOME:
                    startHomeActivity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        init();
    }

    /**
     * 初始化时，查看是不是第一次运行app,若是，则会开启启动页；若不是，之后直接进入首页
     */
    private void init() {
        Boolean isFirstIn = mSharedPreferences.getBoolean("mIsFirstIn", true);
        if (isFirstIn){
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, ENTEER_DURATION);
        }else{
            mHandler.sendEmptyMessageDelayed(GO_HOME, ENTEER_DURATION);
        }
    }

    private void startGuideActivity() {
        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



}