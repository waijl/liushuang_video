package com.liushuang.liushuang_video.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.model.sohu.VideoList;

public class VideoItemAdapter extends BaseAdapter {

    private Context mContext;
    private int mTotalCount;
    private OnVideoSelectedListener mListener;
    private boolean mIsShowTitleContent;
    private VideoList mVideoList = new VideoList();
    private boolean mIsFirst = true;

    public VideoItemAdapter(Context context, int totalCount, OnVideoSelectedListener listener){
        mContext = context;
        mTotalCount = totalCount;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public Video getItem(int position) {
        if (mVideoList.size() > 0){
            return mVideoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, null);
        return null;
    }

    public void addVideo(Video video){
        mVideoList.add(video);
    }
}
