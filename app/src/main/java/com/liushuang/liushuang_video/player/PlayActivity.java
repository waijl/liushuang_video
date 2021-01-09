 package com.liushuang.liushuang_video.player;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liushuang.liushuang_video.AlbumDetailActivity;
import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.utils.DateUtils;
import com.liushuang.liushuang_video.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

 public class PlayActivity extends BaseActivity {

     private static final String TAG = PlayActivity.class.getSimpleName();
     private static final int CHECK_TIME = 1;
     private static final int CHECK_BATTERY = 2;
     private static final int CHECK_PROGRESS = 3;
     private static final int AUTO_HIDE_TIME = 10000;
     private static final int AFTER_DRAGGLE_HIDE_TIME = 3000;
     private String mUrl;
     private int mStreamType;
     private int mCurrentPosition;
     private Video mVideo;
     private IjkVideoView mVideoView;
     private RelativeLayout mLoadingLayout;
     private TextView mLoadingText;
     private FrameLayout mTopLayout;
     private LinearLayout mBottomLayout;
     private ImageView mBackButton;
     private TextView mVideoNameView;
     private TextView mSysTimeView;
     private ImageView mBigPauseButton;
     private CheckBox mPlayOrPauseButton;
     private TextView mVideoCurrentTime;
     private TextView mVideoTotalTime;
     private TextView mBitStreamView;
     private EventHandler mEventHandler;
     private boolean mIsPanelShowing = false;
     private int mBatteryLevel;
     private ImageView mBatteryView;
     private boolean mIsMove = false;//是否在屏幕上滑动
     private SeekBar mSeekBar;
     /*private Formatter mFormatter;
     private StringBuilder mFormatterBuilder;
     private boolean mIsDragging;
     private GestureDetectorController mGestureController;
     private TextView mDragHorizontalView;
     private TextView mDragVerticalView;
     private long mScrollProgress;
     private boolean mIsHorizontalScroll;
     private boolean mIsVerticalScroll;
     private int mCurrentLight;
     private int mMaxLight = 255;
     private int mCurrentVolume;
     private int mMaxVolume = 10;
     private AudioManager mAudioManager;
     private String mLiveTitle;//直播节目标题*/

     @Override
     protected int getLayoutId() {
         return R.layout.activity_play;
     }

     class EventHandler extends Handler{
         public EventHandler(Looper looper){
             super(looper);
         }

         @Override
         public void handleMessage(@NonNull Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case CHECK_TIME:
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             mSysTimeView.setText(DateUtils.getCurrentTime());
                         }
                     });
                     break;
                 case CHECK_BATTERY:
                     //TODO
                     break;
                 case CHECK_PROGRESS:
                     //TODO
                     break;

             }

         }
     }
     @Override
     protected void initView() {
         mUrl = getIntent().getStringExtra("url");
         mStreamType = getIntent().getIntExtra("type", 0);
         mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
         mVideo = getIntent().getParcelableExtra("video");

         Log.d(TAG, ">> url " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
         Log.d(TAG, ">> video " + mVideo);
         mEventHandler = new EventHandler(Looper.myLooper());

         initTopAndBottomView();
//         initCenterView();
         initListener();
         //init ijkPlayer
         mVideoView = bindViewId(R.id.video_view);
         IjkMediaPlayer.loadLibrariesOnce(null);
         IjkMediaPlayer.native_profileBegin("libijkplayer.so");
         mLoadingLayout = bindViewId(R.id.rl_loading_layout);
         mLoadingText = bindViewId(R.id.tv_loading_info);
         mLoadingText.setText("正在加载中...");
         mVideoView.setVideoURI(Uri.parse(mUrl));
         mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
             @Override
             public void onPrepared(IMediaPlayer iMediaPlayer) {
                 mVideoView.start();
             }
         });

         mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
             @Override
             public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                 switch (what){
                     case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                         mLoadingLayout.setVisibility(View.VISIBLE);
                         break;
                     case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                     case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                         mLoadingLayout.setVisibility(View.GONE);
                         break;
                 }
                 return false;
             }
         });
     }

     private void initTopAndBottomView() {
         mTopLayout = bindViewId(R.id.fl_player_top_container);
         mBottomLayout = bindViewId(R.id.ll_player_bottom_layout);
         mBackButton = bindViewId(R.id.iv_player_close);//返回按钮
         mVideoNameView = bindViewId(R.id.tv_player_video_name);//video标题
         mBatteryView = bindViewId(R.id.iv_battery);
         mSysTimeView = bindViewId(R.id.tv_sys_time);//系统时间
         mBigPauseButton = bindViewId(R.id.iv_player_center_pause);//屏幕中央暂停按钮
         mPlayOrPauseButton = bindViewId(R.id.cb_play_pause);//底部播放暂停按钮
         mVideoCurrentTime = bindViewId(R.id.tv_current_video_time);//当前播放进度
         mVideoTotalTime = bindViewId(R.id.tv_total_video_time);//视频总时长
         mBitStreamView = bindViewId(R.id.tv_bitstream);//码流
         mSeekBar = bindViewId(R.id.sb_player_seekbar);
         mSeekBar.setMax(100);

     }

     private void initListener() {
         mBackButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mVideoView.stopPlayback();
                 mVideoView.stopBackgroundPlay();
                 mVideoView.release(true);
                 finish();
             }
         });

         mBigPauseButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mVideoView.start();
                 //TODO
             }
         });

         mPlayOrPauseButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 handlePlayPause();
             }
         });
     }

     private void toggleTopAndBottomLayout(){
         if (mIsPanelShowing){
             hideTopAndBottomLayout();
         }else {
             showTopAndBottomLayout();
             //先显示，如果没有其他操作，就10s后隐藏
             mEventHandler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     hideTopAndBottomLayout();
                 }
             }, AUTO_HIDE_TIME);
         }
     }
     private void showTopAndBottomLayout(){
         mIsPanelShowing = true;
         mTopLayout.setVisibility(View.VISIBLE);
         mBottomLayout.setVisibility(View.VISIBLE);
         //TODO
         if (mEventHandler != null){
             mEventHandler.removeMessages(CHECK_TIME);
             Message message = mEventHandler.obtainMessage(CHECK_TIME);
             mEventHandler.sendMessage(message);

             //TODO
         }

         switch (mStreamType){
             case AlbumDetailActivity.StreamType.SUPER:
                 mBitStreamView.setText(getResources().getString(R.string.stream_super));
                 break;
             case AlbumDetailActivity.StreamType.NORMAL:
                 mBitStreamView.setText(getResources().getString(R.string.stream_normal));
                 break;
             case AlbumDetailActivity.StreamType.HIGH:
                 mBitStreamView.setText(getResources().getString(R.string.stream_high));
                 break;
             default:
                 break;
         }

     }

     private void hideTopAndBottomLayout(){
         mIsPanelShowing = false;
         mTopLayout.setVisibility(View.GONE);
         mBottomLayout.setVisibility(View.GONE);
     }


     private void handlePlayPause() {
         if (mVideoView.isPlaying()){
             mVideoView.pause();
             //TODO
         }else{
             mVideoView.start();
             //TODO
         }
     }

     @Override
     protected void initData() {
         Log.d(TAG, ">> initData mVideo=" + mVideo);
         if (mVideo != null){
             Log.d(TAG, ">>initData mVideoName" + mVideoNameView);
             mVideoNameView.setText(mVideo.getVideoName());
         }

         //TODO
     }
 }