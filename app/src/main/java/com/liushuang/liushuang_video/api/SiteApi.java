package com.liushuang.liushuang_video.api;

import android.content.Context;

import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.Site;

public class SiteApi {
    public static void onGetChannelAlbums(Context context, int pageNum, int pageSize, int siteId, int channelId, OnGetChannelAlbumListener listener){
        switch (siteId){
            case Site.LETV:
                new LetvApi().onGetChannelAlbums(new Channel(channelId, context), pageNum, pageSize, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetChannelAlbums(new Channel(channelId, context), pageNum, pageSize, listener);
                break;
        }
    }

    public static void onGetAlbumDetail(Album album, OnGetAlbumDetailListener listener){
        int siteId = album.getSite().getSiteId();

        switch (siteId){
            case Site.LETV:
                new LetvApi().onGetAlbumDetail(album, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetAlbumDetail(album, listener);
                break;
        }
    }
}
