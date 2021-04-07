package com.liushuang.liushuang_video.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.media.MediaItem;
import com.liushuang.liushuang_video.utils.DateUtils;
import com.liushuang.liushuang_video.utils.Utils;
import com.liushuang.liushuang_video.widget.VideoView;
import com.liushuang.liushuang_video.widget.VitamioVideoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    public static final int PROGRESS = 1;
    public static final int DELAY_MILLIS = 1000;
    private static final String TAG = "SystemPlayerActivity";
    public static final int HIDE_MEDIACONTROLLER = 2;

    public static final int DELAY_MILLIS1 = 5000;
    private static final int FULL_SCREEN = 1;
    private static final int DEFAULT_SCREEN = 2;
    private static final int SPEED = 3;
    public static final int DELAY_MILLIS2 = 2000;

    private RelativeLayout mMediaController;
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
    private LinearLayout mLlBuffer;
    private TextView mTvBufferNetSpeed;
    private TextView mTvLoadingNetSpeed;
    private LinearLayout mLlLoading;


    private Utils mUtils;
    private int mBatteryLevel;
    private List<MediaItem> mMediaItems;
    private GestureDetector mGestureDetector;
    private boolean mIsShowMediaController;

    private boolean mIsUseSystem = true;
    /**
     * 是否全屏
     */
    private boolean mIsFullScreen = false;

    /**
     * 屏幕的宽
     */
    private int mScreenWidth = 0;

    /**
     * 屏幕的高
     */
    private int mScreenHeight = 0;

    /**
     * 真实视频的宽
     */
    private int mVideoWidth;
    /**
     * 真实视频的高
     */
    private int mVideoHeight;


    /**
     * 调用声音
     */
    private AudioManager mAudioManager;

    /**
     * 当前的音量
     */
    private int mCurrentVoice;

    /**
     * 0~15
     * 最大音量
     */
    private int mMaxVoice;
    /**
     * 是否是静音
     */
    private boolean mIsMute = false;
    /**
     * 是否是网络uri
     */
    private boolean mIsNetUri;

    /**
     * 上一次的播放进度
     */
    private int mPrecurrentPosition;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SPEED:
                    String netSpeed = mUtils.getNetSpeed(SystemPlayerActivity.this);

                    mTvLoadingNetSpeed.setText("玩命加载中..." + netSpeed);
                    mTvBufferNetSpeed.setText("缓冲中..." + netSpeed);

                    mHandler.removeMessages(SPEED);
                    mHandler.sendEmptyMessageDelayed(SPEED, DELAY_MILLIS2);
                    break;
                case PROGRESS:
                    int currentPosition = mVideoView.getCurrentPosition();
                    mSeekbarVideo.setProgress(currentPosition);
                    mTvCurrentTime.setText(mUtils.stringForTime(currentPosition));

                    mTvSystemTime.setText(DateUtils.getCurrentTime());

                    //缓冲进度的更新
                    if (mIsNetUri){
                        //只有网络资源才能设置缓冲
                        int bufferPercentage = mVideoView.getBufferPercentage();//0-100
                        int totalBuffer = bufferPercentage * mSeekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        mSeekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else {
                        mSeekbarVideo.setSecondaryProgress(0);
                    }

                    if (!mIsUseSystem){
                        if (mVideoView.isPlaying()){
                            int buffer = currentPosition - mPrecurrentPosition;
                            if (buffer < 500){
                                mLlBuffer.setVisibility(View.VISIBLE);
                            }else {
                                mLlBuffer.setVisibility(View.GONE);
                            }
                        }else {
                            mLlBuffer.setVisibility(View.GONE);
                        }
                    }
                    mPrecurrentPosition = currentPosition;

                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, DELAY_MILLIS);
                    break;

                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
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
    private float mStartY;
    private int mVol;
    private int mTouchAllRange;
    private float mEndY;
    private float mDistanceY;

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
            mIsNetUri = mUtils.isNetUri(mediaItem.getData());
            mVideoView.setVideoPath(mediaItem.getData());
        }else if (mUri != null){
            mTvName.setText(mUri.toString());
            mIsNetUri = mUtils.isNetUri(mUri.toString());
            mVideoView.setVideoURI(mUri);
        }else {
            Toast.makeText(this, "帅哥你没有传递数据", Toast.LENGTH_SHORT).show();
        }

        setButtonState();
    }

    private void getData() {
        mUri = getIntent().getData();
        mMediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
//        Log.d(TAG, "getData: mMediaItems = " + mMediaItems.toString());
        mPosition = getIntent().getIntExtra("position", 0);
//        Log.d(TAG, "getData: mPosition = " + mPosition);
    }

    private void initData() {
        mUtils = new Utils();
        registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (mIsShowMediaController){
                    hideMediaController();
                    mHandler.removeMessages(HIDE_MEDIACONTROLLER);
                }else {
                    showMediaController();
                    mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mCurrentVoice = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mMaxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mSeekbarVoice.setMax(mMaxVoice);
        mSeekbarVoice.setProgress(mCurrentVoice);
        // TODO: 2021/4/3

        mHandler.sendEmptyMessage(SPEED);

    }

    private void hideMediaController() {

        mMediaController.setVisibility(View.GONE);
        mIsShowMediaController = false;
    }

    private void showMediaController() {

        mMediaController.setVisibility(View.VISIBLE);
        mIsShowMediaController = true;
    }

    private void initEvent() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();

                mVideoView.start();

                int duration = mVideoView.getDuration();
                mSeekbarVideo.setMax(duration);
                mTvDuration.setText(mUtils.stringForTime(duration));

                int currentPosition = mVideoView.getCurrentPosition();
                mSeekbarVideo.setProgress(currentPosition);
                mTvCurrentTime.setText(mUtils.stringForTime(currentPosition));

                hideMediaController();
                mHandler.sendEmptyMessage(PROGRESS);

                setVideoType(DEFAULT_SCREEN);

                mLlLoading.setVisibility(View.GONE);

            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Toast.makeText(SystemPlayerActivity.this, "播放出错了哦", Toast.LENGTH_SHORT).show();
                startVitamioPlayer();
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

        mSeekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (progress > 0){
                        mIsMute = false;
                    }else {
                        mIsMute = true;
                    }
                    updateVoice(progress, mIsMute);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
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
                mHandler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
            }
        });

        if (mIsUseSystem){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1){
                mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what){
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                mLlBuffer.setVisibility(View.VISIBLE);
                                break;
                                
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                mLlBuffer.setVisibility(View.GONE);
                                break;
                        }
                        return true;
                    }
                });

            }
        }
        mBtnVoice.setOnClickListener(this);
        mBtnSwichPlayer.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mBtnVideoPre.setOnClickListener(this);
        mBtnVideoStartPause.setOnClickListener(this);
        mBtnVideoNext.setOnClickListener(this);
        mBtnVideoSiwchScreen.setOnClickListener(this);
    }

    private void startVitamioPlayer() {
        if (mVideoView != null){
            mVideoView.stopPlayback();
        }

        Intent intent = new Intent(SystemPlayerActivity.this, VitamioVideoView.class);
        if (mMediaItems != null && mMediaItems.size() > 0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", (Serializable) mMediaItems);
            intent.putExtras(bundle);

            intent.putExtra("position", mPosition);
        }else if (mUri != null){
            intent.setData(mUri);
        }

        startActivity(intent);
        finish();
    }

    private void updateVoice(int progress, boolean mIsMute) {
        if (mIsMute){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            mSeekbarVoice.setProgress(0);
        }else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            mSeekbarVoice.setProgress(progress);
            mCurrentVoice = progress;
        }
    }

    private void initView() {

        mMediaController = findViewById(R.id.media_controller);
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
        mLlBuffer = findViewById(R.id.ll_buffer);
        mTvBufferNetSpeed = findViewById(R.id.tv_buffer_netspeed);
        mLlLoading = findViewById(R.id.ll_loading);
        mTvLoadingNetSpeed = findViewById(R.id.tv_loading_netspeed);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_voice:
                // TODO: 2021/3/31
                mIsMute = !mIsMute;
                updateVoice(mCurrentVoice, mIsMute);
                break;
            case R.id.btn_swich_player:
                // TODO: 2021/3/31
                showSwitchPlayerDialog();
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
                setFullScreenAndDefault();
                break;
            default:
                break;

        }
        mHandler.removeMessages(HIDE_MEDIACONTROLLER);
        mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
    }

    private void showSwitchPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SystemPlayerActivity.this);
        builder.setTitle("系统播放器提醒您：");
        builder.setMessage("当您播放视频，有声音没有画面的时候，请切换万能播放器播放");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void setFullScreenAndDefault() {
        if (mIsFullScreen){
            setVideoType(DEFAULT_SCREEN);
        }else {
            setVideoType(FULL_SCREEN);
        }

    }

    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN:
                mVideoView.setVideoSize(mScreenWidth, mScreenHeight);
                mBtnVideoSiwchScreen.setImageResource(R.drawable.btn_video_siwch_screen_default_selector);
                mIsFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                int width = mScreenWidth;
                int height = mScreenHeight;
                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                mVideoView.setVideoSize(width, height);
                mBtnVideoSiwchScreen.setImageResource(R.drawable.btn_video_siwch_screen_full_selector);
                mIsFullScreen = false;
                break;

        }
    }

    private void playPreVideo() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            //播放下一个视频
            mPosition--;

            if (mPosition >= 0){
                mLlLoading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mMediaItems.get(mPosition);
                mTvName.setText(mediaItem.getName());
                mIsNetUri = mUtils.isNetUri(mediaItem.getData());
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
                mLlLoading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mMediaItems.get(mPosition);
                mTvName.setText(mediaItem.getName());
                mIsNetUri = mUtils.isNetUri(mediaItem.getData());
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
    public boolean onTouchEvent(MotionEvent event) {
        //3.把事件传递给手势识别器
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                mStartY = event.getY();
                mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mTouchAllRange = Math.min(mScreenWidth, mScreenHeight);
                mHandler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                mEndY = event.getY();
                mDistanceY = mStartY - mEndY;
                //改变声音 = （滑动屏幕的距离： 总距离）*音量最大值
                float delta = (mDistanceY / mTouchAllRange) * mMaxVoice;

                int voice = (int) Math.min(Math.max(mVol + delta, 0), mMaxVoice);
                if (delta != 0){
                    mIsMute = false;
                    updateVoice(voice, mIsMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            mCurrentVoice--;
            updateVoice(mCurrentVoice, false);
            mHandler.removeMessages(HIDE_MEDIACONTROLLER);
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            mCurrentVoice++;
            updateVoice(mCurrentVoice, false);
            mHandler.removeMessages(HIDE_MEDIACONTROLLER);
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, DELAY_MILLIS1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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