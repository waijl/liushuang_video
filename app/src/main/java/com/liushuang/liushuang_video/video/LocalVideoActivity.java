package com.liushuang.liushuang_video.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.model.media.MediaItem;
import com.liushuang.liushuang_video.player.SystemPlayerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocalVideoActivity extends BaseActivity {

    private static final String TAG = "LocalVideoActivity";
    public static final int WHAT = 0x20;
    private ListView mLvLocalVideo;
    private TextView mTvNoVideo;
    private ProgressBar mLoadingProgress;

    private List<MediaItem> mediaItems;
    private LocalVideoAdapter mAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0){
                mAdapter = new LocalVideoAdapter(LocalVideoActivity.this, mediaItems);
                mLvLocalVideo.setAdapter(mAdapter);
                mTvNoVideo.setVisibility(View.GONE);
                Log.d(TAG, "handleMessage: mediaItems = " + mediaItems);
            }else {
                // TODO: 2021/3/30
                mTvNoVideo.setVisibility(View.VISIBLE);
            }

            mLoadingProgress.setVisibility(View.GONE);

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_video;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setTitle("本地视频");
        setSupportArrowActionBar(true);

        mLvLocalVideo = bindViewId(R.id.id_lv_localListView);
        mTvNoVideo = bindViewId(R.id.id_tv_noLocalVideo);
        mLoadingProgress = bindViewId(R.id.id_pb_loadingProgress);

        mLvLocalVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem mediaItem = mediaItems.get(position);

                //隐式调用--即此时会出现选择播放器面板
                /*Intent intent = new Intent();
                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
                startActivity(intent);*/

                //显示调用--调用自己写的播放器
               /* Intent intent = new Intent(LocalVideoActivity.this, SystemPlayerActivity.class);
                intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
                startActivity(intent);*/

                Intent intent = new Intent(LocalVideoActivity.this, SystemPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist", (Serializable) mediaItems);
//                intent.putExtra("videolist", (Serializable) mediaItems);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaItems = new ArrayList<>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST//歌曲的演唱者
                };

                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null){
                    while (cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();

                        String name = cursor.getString(0);
                        mediaItem.setName(name);
                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);
                        String data = cursor.getString(3);
                        mediaItem.setData(data);
                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);

                        mediaItems.add(mediaItem);
                    }

                    cursor.close();
                }

                mHandler.sendEmptyMessage(WHAT);
            }
        }).start();
    }

    public static void launch(Context context){
        Intent intent = new Intent(context, LocalVideoActivity.class);
        context.startActivity(intent);
    }
}