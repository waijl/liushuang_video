package com.liushuang.liushuang_video.api;

import android.content.Context;

import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.Site;

public class SiteApi {
    public void onGetChannelAlbums(Context context, int pageNum, int pageSize, int siteId, int channelId, OnGetChannelAlbumListener listener){
        switch (siteId){
            case Site.LETV:
                new LetvApi().onGetChannelAlbums(new Channel(channelId, context), pageNum, pageSize, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetChannelAlbums(new Channel(channelId, context), pageNum, pageSize, listener);
                break;
        }
    }
}
