package com.liushuang.liushuang_video.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.media.MediaItem;
import com.liushuang.liushuang_video.utils.DateUtils;
import com.liushuang.liushuang_video.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    public static final int PROGRESS = 1;
    public static final int DELAY_MILLIS = 1000;
    private static final String TAG = "SystemPlayerActivity";

    private VideoView mVideoView;
    private Uri mUri;
    private LinearLayout mLlTop;
    private TextView mTvName;
    private ImageView mIvBattery;
    private TextView mTvSystemTime;
    private ImageButton mBtnVoice;
    private SeekBar mSeekbarVoice;
    private ImageButton mBtnSwichPlayer;
    private LinearLayout mLlBottom;
    private TextView mTvCurrentTime;
    private SeekBar mSeekbarVideo;
    private TextView mTvDuration;
    private ImageButton mBtnExit;
    private ImageButton mBtnVideoPre;
    private ImageButton mBtnVideoStartPause;
    private ImageButton mBtnVideoNext;
    private ImageButton mBtnVideoSiwchScreen;

    private Utils mUtils;
    private int mBatteryLevel;
    private List<MediaItem> mMediaItems;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    int currentPosition = mVideoView.getCurrentPosition();
                    mSeekbarVideo.setProgress(currentPosition);
                    mTvCurrentTime.setText(mUtils.stringForTime(currentPosition));

                    mTvSystemTime.setText(DateUtils.getCurrentTime());

                    removeMessages(PROGRESS);

                    sendEmptyMessageDelayed(PROGRESS, DELAY_MILLIS);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 通过广播获取系统电量情况
     */
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLevel = intent.getIntExtra("level", 0);
            Log.d(TAG, ">> mBatteryReceiver onReceive mBatteryLevel=" + mBatteryLevel);

            setCurrentBattery();
        }
    };
    private int mPosition;

    private void setCurrentBattery() {
        Log.d(TAG, ">> setCurrentBattery level " + mBatteryLevel);
        if ( 0 < mBatteryLevel && mBatteryLevel <= 10) {
            mIvBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (10 < mBatteryLevel && mBatteryLevel <= 20) {
            mIvBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (20 < mBatteryLevel && mBatteryLevel <= 50) {
            mIvBattery.setImageResource(R.drawable.ic_battery_50);
        } else if (50 < mBatteryLevel && mBatteryLevel <= 80) {
            mIvBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (80 < mBatteryLevel && mBatteryLevel <= 100) {
            mIvBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_player);

        initView();

        initData();

        initEvent();

        getData();
        setData();

//        mVideoView.setMediaController(new MediaController(this));

    }

    private void setData() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            MediaItem mediaItem = mMediaItems.get(mPosition);
            mTvName.setText(mediaItem.getName());
            // TODO: 2021/4/1
            mVideoView.setVideoPath(mediaItem.getData());
        }else if (mUri != null){
            mTvName.setText(mUri.toString());
            // TODO: 2021/4/1
            mVideoView.setVideoURI(mUri);
        }else {
            Toast.makeText(this, "帅哥你没有传递数据", Toast.LENGTH_SHORT).show();
        }

        setButtonState();
    }

    private void getData() {
        mUri = getIntent().getData();
        mMediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        Log.d(TAG, "getData: mMediaItems = " + mMediaItems.toString());
        mPosition = getIntent().getIntExtra("position", 0);
        Log.d(TAG, "getData: mPosition = " + mPosition);
    }

    private void initData() {
        mUtils = new Utils();
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void initEvent() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mVideoView.start();

                int duration = mVideoView.getDuration();
                mSeekbarVideo.setMax(duration);
                mTvDuration.setText(mUtils.stringForTime(duration));

                int currentPosition = mVideoView.getCurrentPosition();
                mSeekbarVideo.setProgress(currentPosition);
                mTvCurrentTime.setText(mUtils.stringForTime(currentPosition));

                mHandler.sendEmptyMessage(PROGRESS);

            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemPlayerActivity.this, "播放出错了哦", Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(SystemPlayerActivity.this, "播放结束了", Toast.LENGTH_SHORT).show();
                playNextVideo();
            }
        });

        mSeekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true){
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBtnVoice.setOnClickListener(this);
        mBtnSwichPlayer.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mBtnVideoPre.setOnClickListener(this);
        mBtnVideoStartPause.setOnClickListener(this);
        mBtnVideoNext.setOnClickListener(this);
        mBtnVideoSiwchScreen.setOnClickListener(this);
    }

    private void initView() {

        mVideoView = findViewById(R.id.id_vv_systemPlayer);
        mLlTop = (LinearLayout) findViewById(R.id.ll_top);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvBattery = (ImageView) findViewById(R.id.iv_battery);
        mTvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        mBtnVoice = (ImageButton) findViewById(R.id.btn_voice);
        mSeekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        mBtnSwichPlayer = (ImageButton) findViewById(R.id.btn_swich_player);
        mLlBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mTvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        mSeekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);
        mBtnExit = (ImageButton) findViewById(R.id.btn_exit);
        mBtnVideoPre = (ImageButton) findViewById(R.id.btn_video_pre);
        mBtnVideoStartPause = (ImageButton) findViewById(R.id.btn_video_start_pause);
        mBtnVideoNext = (ImageButton) findViewById(R.id.btn_video_next);
        mBtnVideoSiwchScreen = (ImageButton) findViewById(R.id.btn_video_siwch_screen);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_voice:
                // TODO: 2021/3/31
                break;
            case R.id.btn_swich_player:
                // TODO: 2021/3/31
                break;
            case R.id.btn_exit:
                // TODO: 2021/3/31
                finish();
                break;
            case R.id.btn_video_pre:
                // TODO: 2021/3/31
                playPreVideo();
                break;
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
            case R.id.btn_video_next:
                // TODO: 2021/3/31
                playNextVideo();
                break;
            case R.id.btn_video_siwch_screen:
                // TODO: 2021/3/31
                break;
            default:
                break;

        }
    }

    private void playPreVideo() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            //播放下一个视频
            mPosition--;

            if (mPosition >= 0){
                // TODO: 2021/4/1
                MediaItem mediaItem = mMediaItems.get(mPosition);
                mTvName.setText(mediaItem.getName());
                // TODO: 2021/4/1
                mVideoView.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
            }
        }else if ( mUri != null){
            //设置按钮状态-上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();
        }
    }

    private void playNextVideo() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            //播放下一个视频
            mPosition++;

            if (mPosition < mMediaItems.size()){
                // TODO: 2021/4/1
                MediaItem mediaItem = mMediaItems.get(mPosition);
                mTvName.setText(mediaItem.getName());
                // TODO: 2021/4/1
                mVideoView.setVideoPath(mediaItem.getData());
                //设置按钮状态
                setButtonState();
            }
        }else if ( mUri != null){
            //设置按钮状态-上一个和下一个按钮设置灰色并且不可以点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            if (mMediaItems.size() == 1){
                setEnable(false);
            }else if (mMediaItems.size() == 2){
                if (mPosition == 0){
                    mBtnVideoPre.setImageResource(R.drawable.btn_pre_gray);
                    mBtnVideoPre.setEnabled(false);

                    mBtnVideoNext.setImageResource(R.drawable.btn_video_next_selector);
                    mBtnVideoNext.setEnabled(true);
                }else if (mPosition == mMediaItems.size() - 1){
                    mBtnVideoNext.setImageResource(R.drawable.btn_next_gray);
                    mBtnVideoNext.setEnabled(false);

                    mBtnVideoPre.setImageResource(R.drawable.btn_video_pre_selector);
                    mBtnVideoPre.setEnabled(true);
                }
            }else {
                if (mPosition == 0){
                    mBtnVideoPre.setImageResource(R.drawable.btn_pre_gray);
                    mBtnVideoPre.setEnabled(false);
                }else if (mPosition == mMediaItems.size() - 1){
                    mBtnVideoNext.setImageResource(R.drawable.btn_next_gray);
                    mBtnVideoNext.setEnabled(false);
                }else{
                    setEnable(true);
                }
            }
        }else if (mUri != null){
            //两个按钮设置灰色
            setEnable(false);
        }
    }

    private void setEnable(boolean isEnable) {
        if (isEnable) {
            mBtnVideoPre.setImageResource(R.drawable.btn_video_pre_selector);
            mBtnVideoPre.setEnabled(true);
            mBtnVideoNext.setImageResource(R.drawable.btn_video_next_selector);
            mBtnVideoNext.setEnabled(true);
        } else {
            //两个按钮设置灰色
            mBtnVideoPre.setImageResource(R.drawable.btn_pre_gray);
            mBtnVideoPre.setEnabled(false);
            mBtnVideoNext.setImageResource(R.drawable.btn_next_gray);
            mBtnVideoNext.setEnabled(false);
        }
    }

    private void startAndPause() {

        if (mVideoView.isPlaying()){
            mVideoView.pause();
            mBtnVideoStartPause.setImageResource(R.drawable.btn_video_start_selector);
        }else {
            mVideoView.start();
            mBtnVideoStartPause.setImageResource(R.drawable.btn_video_pause_selector);
        }
    }

    @Override
    protected void onDestroy() {
        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
            mBatteryReceiver = null;
        }
        super.onDestroy();
    }
}