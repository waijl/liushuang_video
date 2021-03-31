package com.liushuang.liushuang_video.player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.liushuang.liushuang_video.utils.Utils;

public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    public static final int PROGRESS = 1;
    public static final int DELAY_MILLIS = 1000;

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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    int currentPosition = mVideoView.getCurrentPosition();
                    mSeekbarVideo.setProgress(currentPosition);
                    mTvCurrentTime.setText(mUtils.stringForTime(currentPosition));

                    removeMessages(PROGRESS);

                    sendEmptyMessageDelayed(PROGRESS, DELAY_MILLIS);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_player);

        initView();

        initData();

        initEvent();

//        mVideoView.setMediaController(new MediaController(this));

    }

    private void initData() {
        mUri = getIntent().getData();
        if (mUri != null) {
            mVideoView.setVideoURI(mUri);
        }
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
                Toast.makeText(SystemPlayerActivity.this, "播放结束了", Toast.LENGTH_SHORT).show();
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

        mUtils = new Utils();

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
                break;
            case R.id.btn_video_pre:
                // TODO: 2021/3/31
                break;
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
            case R.id.btn_video_next:
                // TODO: 2021/3/31
                break;
            case R.id.btn_video_siwch_screen:
                // TODO: 2021/3/31
                break;
            default:
                break;

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
}