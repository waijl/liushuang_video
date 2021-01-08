package com.liushuang.liushuang_video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liushuang.liushuang_video.api.OnGetAlbumDetailListener;
import com.liushuang.liushuang_video.api.OnGetVideoPlayUrlListener;
import com.liushuang.liushuang_video.api.SiteApi;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.detail.AlbumPlayGridFragment;
import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.player.PlayActivity;
import com.liushuang.liushuang_video.utils.ImageUtils;

public class AlbumDetailActivity extends BaseActivity {

    private static final String TAG = AlbumDetailActivity.class.getSimpleName();
    private Album mAlbum;
    private boolean mIsShowDesc;
    private int mVideoNo;
    private ImageView mAlbumImg;
    private TextView mAlbumName;
    private TextView mDirector;
    private TextView mMainActor;
    private TextView mAlbumDesc;
    private boolean mIsFavor;
    private AlbumPlayGridFragment mFragment;
    private Button mSuperBitstreamButton;
    private Button mNormalBitstreamButton;
    private Button mHighBitstreamButton;
    private int mCurrentVideoPosition;
    private boolean mIsFirstUseMobileNetwork = true;
    /*private CommonDBHelper mFavoriteDBHelper;
    private CommonDBHelper mHistoryDBHelper;*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        mAlbum = getIntent().getParcelableExtra("album");
        mVideoNo = getIntent().getIntExtra("videoNo", 0);
        mIsShowDesc = getIntent().getBooleanExtra("isShowDesc", false);
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(mAlbum.getTitle());//显示标题

       /* mFavoriteDBHelper = new CommonDBHelper(this);
        mFavoriteDBHelper.setParams("favorite");
        mHistoryDBHelper = new CommonDBHelper(this);
        mHistoryDBHelper.setParams("history");
        mIsFavor = mFavoriteDBHelper.getAlbumById(mAlbum.getAlbumId(), mAlbum.getSite().getSiteId()) != null ;*/
        mAlbumImg = bindViewId(R.id.iv_album_image);
        mAlbumName = bindViewId(R.id.tv_album_name);
        mDirector = bindViewId(R.id.tv_album_director);
        mMainActor = bindViewId(R.id.tv_album_mainactor);
        mAlbumDesc = bindViewId(R.id.tv_album_desc);
        mSuperBitstreamButton = bindViewId(R.id.bt_super);
        mSuperBitstreamButton.setOnClickListener(mOnSuperClickListener);
        mNormalBitstreamButton = bindViewId(R.id.bt_normal);
        mNormalBitstreamButton.setOnClickListener(mOnNormalClickListener);
        mHighBitstreamButton = bindViewId(R.id.bt_high);
        mHighBitstreamButton.setOnClickListener(mOnHighClickListener);
    }

    private View.OnClickListener mOnSuperClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener mOnNormalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener mOnHighClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    //三个button有共同点,tag设置的id是一样,value值不一样
    private void handleButtonClick(View v) {
        Button button =  (Button) v;
        String url = (String) button.getTag(R.id.key_video_url);
        int type = (int) button.getTag(R.id.key_video_stream);//码流类型
        Video video = (Video) button.getTag(R.id.key_video);
        Log.d(TAG, ">> handleButtonClick video " + video);
        int currentPosition = (int) button.getTag(R.id.key_current_video_number);
        if (AppManager.isNetWorkAvailable()) {
            if (AppManager.isNetworkWifiAvailable()) {
                /*mHistoryDBHelper.add(mAlbum);*/
                Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("type",type);
                intent.putExtra("currentPosition",currentPosition);
                intent.putExtra("video",video);
                startActivity(intent);
            }else{
                Log.d(TAG, "使用流量");
                if (mIsFirstUseMobileNetwork){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(AlbumDetailActivity.this).create();
                            alertDialog.setIcon(R.drawable.warning);
                            alertDialog.setTitle("使用流量提醒：");
                            alertDialog.setMessage("您即将使用流量观看视频");
                            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mIsFirstUseMobileNetwork = false;
                                    Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                                    intent.putExtra("url",url);
                                    intent.putExtra("type",type);
                                    intent.putExtra("currentPosition",currentPosition);
                                    intent.putExtra("video",video);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();
                        }
                    });

                }else {
                    Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("type",type);
                    intent.putExtra("currentPosition",currentPosition);
                    intent.putExtra("video",video);
                    startActivity(intent);
                }
            }
        }

    }
    private void updateInfo() {
        mAlbumName.setText(mAlbum.getTitle());
        //导演
        if (!TextUtils.isEmpty(mAlbum.getDirector())) {
            mDirector.setText(getResources().getString(R.string.director) + mAlbum.getDirector());
            mDirector.setVisibility(View.VISIBLE);
        } else {
            mDirector.setVisibility(View.GONE);
        }
        //主演
        if (!TextUtils.isEmpty(mAlbum.getMainActor())) {
            mMainActor.setText(getResources().getString(R.string.mainactor) + mAlbum.getMainActor());
            mMainActor.setVisibility(View.VISIBLE);
        } else {
            mMainActor.setVisibility(View.GONE);
        }
        //描述
        if (!TextUtils.isEmpty(mAlbum.getAlbumDesc())) {
            mAlbumDesc.setText(mAlbum.getAlbumDesc());
            mAlbumDesc.setVisibility(View.VISIBLE);
        } else {
            mAlbumDesc.setVisibility(View.GONE);
        }
        //海报图
        if (!TextUtils.isEmpty(mAlbum.getVerImgUrl())) {
            ImageUtils.disPlayImage(mAlbumImg, mAlbum.getVerImgUrl());
        } else if (!TextUtils.isEmpty(mAlbum.getHorImgUrl())) {
            ImageUtils.disPlayImage(mAlbumImg, mAlbum.getHorImgUrl());
        }
    }

    @Override
    protected void initData() {
        updateInfo();
        //补全详情页数据
        SiteApi.onGetAlbumDetail(mAlbum, new OnGetAlbumDetailListener() {
            @Override
            public void onGetAlbumDetailSuccess(Album album) {
                Log.d(TAG, ">> onGetAlbumDetailSuccess album" + album.getVideoTotal());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateInfo();
                        mFragment = AlbumPlayGridFragment.newInstance(mAlbum, mIsShowDesc, 0);
                        mFragment.setPlayVideoSelectedListener(mPlayVideoSelectedListener);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container,mFragment);
                        ft.commit();
                        getFragmentManager().executePendingTransactions();

                    }
                });


            }

            @Override
            public void onGetAlbumDetailFailed(ErrorInfo errorInfo) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://actionbar 左边箭头id
                finish();
                return true;
            case R.id.action_favor_item:
                if (mIsFavor) {
                    mIsFavor = false;
                    // 收藏状态更新
//                    mFavoriteDBHelper.delete(mAlbum.getAlbumId(), mAlbum.getSite().getSiteId());
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_unfavor_item:
                if (!mIsFavor) {
                    mIsFavor = true;
                    // 收藏状态更新
//                    mFavoriteDBHelper.add(mAlbum);
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已添加收藏", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_detail_menu,menu);
        return true;
    }

    /**
     * 必须先创建menu,否则会报空指针
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favitem = menu.findItem(R.id.action_favor_item);
        MenuItem unfavitem = menu.findItem(R.id.action_unfavor_item);
        favitem.setVisible(mIsFavor);
        unfavitem.setVisible(!mIsFavor);
        return super.onPrepareOptionsMenu(menu);
    }

    public static void launch(Activity activity, Album album, int vidoeNo, boolean isShowDesc) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("album", album);
        intent.putExtra("videoNo", vidoeNo);
        intent.putExtra("isShowDesc",isShowDesc);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, Album album) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("album", album);
        activity.startActivity(intent);
    }

    private AlbumPlayGridFragment.OnPlayVideoSelectedListener mPlayVideoSelectedListener = new AlbumPlayGridFragment.OnPlayVideoSelectedListener() {
        @Override
        public void OnPlayVideoSelected(Video video, int position) {
            mCurrentVideoPosition = position;
            SiteApi.onGetVideoPlayUrl(video, mVideoUrlListener);
        }
    };


    public class StreamType {
        public static final int SUPER = 1;
        public static final int NORMAL = 2;
        public static final int HIGH = 3;
    }

    private OnGetVideoPlayUrlListener mVideoUrlListener = new OnGetVideoPlayUrlListener() {
        @Override
        public void onGetSuperUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetSuperUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSuperBitstreamButton.setVisibility(View.VISIBLE);
                    mSuperBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mSuperBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mSuperBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mSuperBitstreamButton.setTag(R.id.key_video_stream, StreamType.SUPER); //码流
                }
            });
        }
        @Override
        public void onGetNoramlUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetNoramlUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNormalBitstreamButton.setVisibility(View.VISIBLE);
                    mNormalBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mNormalBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mNormalBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mNormalBitstreamButton.setTag(R.id.key_video_stream, StreamType.NORMAL); //码流
                }
            });
        }

        @Override
        public void onGetHighUrl(final Video video, final String url) {
            Log.d(TAG,">> onGetHighUrl url " + url + ", video " + video);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHighBitstreamButton.setVisibility(View.VISIBLE);
                    mHighBitstreamButton.setTag(R.id.key_video_url, url); //视频url
                    mHighBitstreamButton.setTag(R.id.key_video, video);//视频info
                    mHighBitstreamButton.setTag(R.id.key_current_video_number, mCurrentVideoPosition);//当前视频
                    mHighBitstreamButton.setTag(R.id.key_video_stream, StreamType.HIGH); //码流
                }
            });
        }

        @Override
        public void onGetFailed(ErrorInfo info) {
            Log.d(TAG,">> onGetFailed url " + info.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideAllButton();//请求播放源失败,不展示s
                }
            });
        }
    };

    private void hideAllButton() {
        mSuperBitstreamButton.setVisibility(View.GONE);
        mNormalBitstreamButton.setVisibility(View.GONE);
        mHighBitstreamButton.setVisibility(View.GONE);
    }
}