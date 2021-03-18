package com.liushuang.liushuang_video;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liushuang.liushuang_video.home.HomeActivity;

/**
 * app启动时的主入口activity
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    public static final int INTERVAL = 1000;
    private SharedPreferences mSharedPreferences;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private static final int ENTEER_DURATION = 5000;
    private TextView mTextView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            /*switch (msg.what){
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                case GO_HOME:
                    startHomeActivity();
                    break;
                default:
                    break;
            }*/
            int time = msg.arg1;
            Log.d(TAG, "handleMessage: time = " + time);
            if (msg.what == GO_GUIDE){
                mTextView.setText("跳过 " + msg.arg1 / INTERVAL);
                Message message = Message.obtain();
                message.what = GO_GUIDE;

                if (time > 1000){
                    message.arg1 = time - INTERVAL;
                    sendMessageDelayed(message, INTERVAL);
                }else {
                    startGuideActivity();
                }
            }else if (msg.what == GO_HOME){
                mTextView.setText("跳过 " + msg.arg1 / INTERVAL);
                Message message = Message.obtain();
                message.what = GO_HOME;

                if (time > 1000){
                    message.arg1 = time - INTERVAL;
                    sendMessageDelayed(message, INTERVAL);
                }else {
                    startHomeActivity();
                }
            }else{
                startHomeActivity();
            }
        }
    };
    private Boolean mIsFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTextView = findViewById(R.id.id_tv_splash);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        init();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFirst){
                    startGuideActivity();
                    mHandler.removeMessages(GO_GUIDE);
                }else {
                    startHomeActivity();
                    mHandler.removeMessages(GO_HOME);
                }
            }
        });
    }

    /**
     * 初始化时，查看是不是第一次运行app,若是，则会开启启动页；若不是，之后直接进入首页
     */
    private void init() {
        mIsFirst = mSharedPreferences.getBoolean("mIsFirstIn", true);
        Message message = Message.obtain();
        if (mIsFirst){
            message.what = GO_GUIDE;
            message.arg1 = ENTEER_DURATION;
            mHandler.sendMessage(message);
        }else{
            message.what = GO_HOME;
            message.arg1 = ENTEER_DURATION;
            mHandler.sendMessage(message);
        }
    }

    private void startGuideActivity() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



}