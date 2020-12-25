package com.liushuang.liushuang_video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.liushuang.liushuang_video.base.BaseFragment;
import com.liushuang.liushuang_video.model.Site;
import com.liushuang.liushuang_video.widget.PullLoadRecyclerView;

public class DetailListFragment extends BaseFragment {

    private int mSiteId;
    private int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private PullLoadRecyclerView mRecyclerView;
    private TextView mTextView;
    private int mColumns;
    private DetailListAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final int REFRESH_DURATION = 1500;
    private static final int LOADMORE_DURATION = 3000;

    public DetailListFragment(){

    }

    public static Fragment newInstance(int siteId, int channelId){
        DetailListFragment fragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SITE_ID, siteId);
        bundle.putInt(CHANNEL_ID, channelId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new DetailListAdapter();
        loadData();
        if (mSiteId == Site.LETV){
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        }else{
            mColumns = 3;
            mAdapter.setColumns(mColumns);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detailist;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mRecyclerView = bindViewId(R.id.pullLoadRecyclerView);
        mRecyclerView.setGridLayout(3);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    private void reFreshData(){

    }

    private void loadData(){

    }
    class PullLoadMoreListener implements PullLoadRecyclerView.OnPullLoadMoreListener{

        @Override
        public void reRresh() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reFreshData();
                    mRecyclerView.setRefreshCompleted();
                }
            }, REFRESH_DURATION);
        }

        @Override
        public void loadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    mRecyclerView.setLoadMoreCompleted();
                }
            }, LOADMORE_DURATION);
        }
    }

    class DetailListAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public void setColumns(int columns){
            mColumns = columns;
        }
    }
}
