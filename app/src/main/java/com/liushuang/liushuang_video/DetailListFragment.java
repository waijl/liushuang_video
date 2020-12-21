package com.liushuang.liushuang_video;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.liushuang.liushuang_video.base.BaseFragment;

public class DetailListFragment extends BaseFragment {

    private int mSiteId;
    private int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";

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
    protected int getLayoutId() {
        return R.layout.fragment_detailist;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
