 package com.liushuang.liushuang_video.player;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

 public class PlayActivity extends BaseActivity {

    private static final String TAG = PlayActivity.class.getSimpleName();
    private String mUrl;
    private int mStreamType;
    private int mCurrentPosition;
    private Video mVideo;
    private IjkVideoView mVideoView;
    private RelativeLayout mLoadingLayout;
    private TextView mLoadingText;

     @Override
     protected int getLayoutId() {
         return R.layout.activity_play;
     }

     @Override
     protected void initView() {
         mUrl = getIntent().getStringExtra("url");
         mStreamType = getIntent().getIntExtra("type", 0);
         mCurrentPosition = getIntent().getIntExtra("currentPosition", 0);
         mVideo = getIntent().getParcelableExtra("video");

         Log.d(TAG, ">> url " + mUrl + ", mStreamType " + mStreamType + ", mCurrentPosition " + mCurrentPosition);
         Log.d(TAG, ">> video " + mVideo);

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

     @Override
     protected void initData() {

     }
 }