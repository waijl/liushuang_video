package com.liushuang.liushuang_video.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.api.OnGetVideoListener;
import com.liushuang.liushuang_video.api.SiteApi;
import com.liushuang.liushuang_video.base.BaseFragment;
import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.model.sohu.VideoList;
import com.liushuang.liushuang_video.widget.CustomGridView;

public class AlbumPlayGridFragment extends BaseFragment {

    private static final String TAG = AlbumPlayGridFragment.class.getSimpleName();
    private static final String ARGS_ALBUM = "album";
    private static final String ARGS_IS_SHOWDESC = "isShowDesc";
    private static final String ARGS_INIT_POSITION = "initVideoPosition";

    private Album mAlbum;
    private int mInitPosition;
    private boolean mIsShowDesc;
    private int mPageNo;
    private int mPageSize;
    private VideoItemAdapter mVideoItemAdatper;
    private CustomGridView mCustomGridView;
    private int mPageTotal;
    private View mEmptyView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsFristSelection = true;
    private int mCurrentPosition;
    private OnPlayVideoSelectedListener mOnPlayVideoSelectedListener;

    public void setPlayVideoSelectedListener(OnPlayVideoSelectedListener listener) {
        mOnPlayVideoSelectedListener = listener;
    }
    public interface OnPlayVideoSelectedListener{
        void OnPlayVideoSelected(Video video, int position);
    }

    public AlbumPlayGridFragment() {

    }

    public static AlbumPlayGridFragment newInstance(Album album, boolean isShowDesc, int initVideoPosition) {
        AlbumPlayGridFragment fragment = new AlbumPlayGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ALBUM, album);
        bundle.putBoolean(ARGS_IS_SHOWDESC, isShowDesc);
        bundle.putInt(ARGS_INIT_POSITION, initVideoPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ">> oncreate ");
        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(ARGS_ALBUM);
            mIsShowDesc = getArguments().getBoolean(ARGS_IS_SHOWDESC);
            mInitPosition = getArguments().getInt(ARGS_INIT_POSITION);
            mCurrentPosition = mInitPosition;
            mPageNo = 0;
            mPageSize = 50;
            mVideoItemAdatper = new VideoItemAdapter(getActivity(), mAlbum.getVideoTotal(), mVideoSelectedListner);
            mVideoItemAdatper.setIsShowTitleContent(mIsShowDesc);
            mPageTotal = (mAlbum.getVideoTotal() + mPageSize -1)/ mPageSize;
            loadData();
        }
    }

    private OnVideoSelectedListener mVideoSelectedListner = new OnVideoSelectedListener() {
        @Override
        public void onVideoSelected(Video video, int position) {
            if (mCustomGridView != null) {
                mCustomGridView.setSelection(position);
                mCustomGridView.setItemChecked(position, true);
                mCurrentPosition = position;
                if (mOnPlayVideoSelectedListener != null) {
                    mOnPlayVideoSelectedListener.OnPlayVideoSelected(video, position);
                }
            }
        }
    };

    private void loadData() {
        Log.d(TAG, ">> loadData ");
        mPageNo ++;
        SiteApi.onGetVideo(mPageSize, mPageNo, mAlbum, new OnGetVideoListener() {
            @Override
            public void onGetVideoSuccess(VideoList videoList) {
                Log.d(TAG, ">> OnGetVideoSuccess " + videoList.size());
                for (Video video : videoList) {
                    mVideoItemAdatper.addVideo(video);
                    Log.d(TAG, ">> OnGetVideoSuccess video " + video.toString());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyView.setVisibility(View.GONE);
                        if (mVideoItemAdatper != null) {
                            mVideoItemAdatper.notifyDataSetChanged();
                        }
                        if (mVideoItemAdatper.getCount() > mInitPosition && mIsFristSelection) {
                            mCustomGridView.setSelection(mInitPosition);
                            mCustomGridView.setItemChecked(mInitPosition, true);
                            mIsFristSelection = false;
                            SystemClock.sleep(100);
                            mCustomGridView.smoothScrollToPosition(mInitPosition);
                        }
                    }
                });
            }

            @Override
            public void onGetVideoFailed(ErrorInfo info) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album_desc;
    }

    @Override
    protected void initView() {
        mEmptyView = bindViewId(R.id.tv_empty);
        mEmptyView.setVisibility(View.VISIBLE);
        mCustomGridView = bindViewId(R.id.gv_video_layout);
        //mIsShowDesc 表示同样是剧集,综艺节目是xx期,电视剧集是数字, 1表示综艺,或纪录片类,6表示动漫,电视剧
        mCustomGridView.setNumColumns(mIsShowDesc ? 1 : 6);
        mCustomGridView.setAdapter(mVideoItemAdatper);
        if (mAlbum.getVideoTotal() > 0 && mAlbum.getVideoTotal() > mPageSize) {
            mCustomGridView.setHasMoreItem(true);
        } else {
            mCustomGridView.setHasMoreItem(false);
        }
        mCustomGridView.setOnLoadMoreListener(new CustomGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMoreItems() {
                loadData();
            }
        });

    }

    @Override
    protected void initData() {

    }
}
