package com.liushuang.liushuang_video.detail;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.api.OnGetVideoListener;
import com.liushuang.liushuang_video.api.SiteApi;
import com.liushuang.liushuang_video.base.BaseFragment;
import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.model.sohu.VideoList;

public class AlbumPlayGridFragment extends BaseFragment {
    private static final String TAG = AlbumPlayGridFragment.class.getSimpleName();
    private static final String ARGS_ALBUM = "album";
    private static final String ARGS_is_SHOWDESC = "isShowDesc";
    private static final String ARGS_INIT_POSITION = "initVideoPosition";
    private Album mAlbum;
    private boolean mIsShowDesc;
    private int mInitVideoPosition;
    private int mPageNo;
    private int mPageSize;
    private VideoItemAdapter mVideoItemAdapter;

    public AlbumPlayGridFragment(){

    }

    public static AlbumPlayGridFragment newInstance(Album album, boolean isShowDesc, int initVideoPosition){
        AlbumPlayGridFragment fragment = new AlbumPlayGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ALBUM, album);
        bundle.putInt(ARGS_INIT_POSITION, initVideoPosition);
        bundle.putBoolean(ARGS_is_SHOWDESC, isShowDesc);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ">> onCreate");
        if (getArguments() != null){
            mAlbum = getArguments().getParcelable(ARGS_ALBUM);
            mIsShowDesc = getArguments().getBoolean(ARGS_is_SHOWDESC);
            mInitVideoPosition = getArguments().getInt(ARGS_INIT_POSITION);

            mPageNo = 0;
            mPageSize = 50;
            mVideoItemAdapter = new VideoItemAdapter(getActivity(), mAlbum.getVideoTotal(), mVideoSelectedListener);
            loadData();
        }
    }

    private OnVideoSelectedListener mVideoSelectedListener = new OnVideoSelectedListener() {
        @Override
        public void onVideoSelected(Video video, int position) {

        }
    };

    private void loadData(){
        Log.d(TAG, ">> loadData");
        mPageNo ++;
        SiteApi.onGetVideo(mPageSize, mPageNo, mAlbum, new OnGetVideoListener() {


            @Override
            public void onGetVideoSuccess(VideoList videoList) {
                for (Video video : videoList){
                    mVideoItemAdapter.addVideo(video);
                    Log.d(TAG, ">> onGetVideoSuccess" + video.toString());
                }

            }
            @Override
            public void onGetVideoFailed(ErrorInfo errorInfo) {

            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album_desc;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
