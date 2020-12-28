package com.liushuang.liushuang_video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.liushuang.liushuang_video.api.OnGetChannelAlbumListener;
import com.liushuang.liushuang_video.api.SiteApi;
import com.liushuang.liushuang_video.base.BaseFragment;
import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.AlbumList;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.Site;
import com.liushuang.liushuang_video.utils.ImageUtils;
import com.liushuang.liushuang_video.widget.PullLoadRecyclerView;

import org.w3c.dom.Text;

public class DetailListFragment extends BaseFragment {

    private static final String TAG = DetailListFragment.class.getSimpleName();
    private int mSiteId;
    private int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private PullLoadRecyclerView mRecyclerView;
    private TextView mEmptyView;
    private int mColumns;
    private DetailListAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final int REFRESH_DURATION = 1500;
    private static final int LOADMORE_DURATION = 3000;
    private int pageNum;
    private int pageSize = 30;

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

        if (getArguments() != null){
            mSiteId = getArguments().getInt(SITE_ID);
            mChannelId = getArguments().getInt(CHANNEL_ID);
        }
        pageNum = 0;
        mAdapter = new DetailListAdapter(getActivity(), new Channel(mChannelId, getActivity()));
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
        mEmptyView = bindViewId(R.id.tv_empty);
        mEmptyView.setText(getActivity().getResources().getString(R.string.load_more_text));
        mRecyclerView = bindViewId(R.id.pullLoadRecyclerView);
        mRecyclerView.setGridLayout(3);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    private void reFreshData(){

    }

    private void loadData(){
        pageNum ++;
        SiteApi.onGetChannelAlbums(getActivity(), pageNum, pageSize, mSiteId, mChannelId, new OnGetChannelAlbumListener() {
            @Override
            public void onGetChannelAlbumSuccess(AlbumList albumList) {
                /*for (Album album : albumList) {
                    Log.d(TAG, ">> album " + album.toString());
                }*/

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyView.setVisibility(View.GONE);
                    }
                });

                for (Album album : albumList){
                    mAdapter.setData(album);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onGetChannelAlbumFailed(ErrorInfo errorInfo) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyView.setText(getActivity().getResources().getString(R.string.data_failed_tip));
                    }
                });
            }
        });
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

        private Context mContext;
        private Channel mChannel;
        private AlbumList mAlbumList = new AlbumList();
        private int mColumns;

        public DetailListAdapter(Context context, Channel channel){
            mContext = context;
            mChannel = channel;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.detaillist_item, null);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            view.setTag(itemViewHolder);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (mAlbumList.size() == 0){
                return;
            }
            final Album album = getItem(position);
            if (holder instanceof ItemViewHolder){
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.albumName.setText(album.getTitle());
                if (album.getTip().isEmpty()){
                    itemViewHolder.albumTip.setVisibility(View.GONE);
                }else {
                    itemViewHolder.albumTip.setText(album.getTip());
                }

                Point point = null;
                if (mColumns == 2){
                    point = ImageUtils.getHorPostSize(mContext, mColumns);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
                    itemViewHolder.albumPoster.setLayoutParams(params);
                }else {
                    point = ImageUtils.getVerPostSize(mContext, mColumns);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
                    itemViewHolder.albumPoster.setLayoutParams(params);
                }

                if (album.getVerImgUrl() != null){
                    ImageUtils.disPlayImage(itemViewHolder.albumPoster, album.getVerImgUrl(), point.x, point.y);
                }else if (album.getHorImgUrl() != null){
                    ImageUtils.disPlayImage(itemViewHolder.albumPoster, album.getHorImgUrl(), point.x, point.y);
                }

                itemViewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }



        }

        private Album getItem(int position){
            return mAlbumList.get(position);
        }

        @Override
        public int getItemCount() {
            if (mAlbumList.size() > 0){
                return mAlbumList.size();
            }
            return 0;
        }

        public void setColumns(int columns){
            mColumns = columns;
        }

        public void setData(Album album){
            mAlbumList.add(album);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{

            public LinearLayout resultContainer;
            public ImageView albumPoster;
            public TextView albumTip;
            public TextView albumName;

            public ItemViewHolder(@NonNull View view) {
                super(view);
                resultContainer = (LinearLayout) view.findViewById(R.id.album_container);
                albumPoster = (ImageView) view.findViewById(R.id.iv_album_poster);
                albumTip = (TextView) view.findViewById(R.id.tv_album_tip);
                albumName = (TextView) view.findViewById(R.id.tv_album_name);
            }
        }
    }
}
