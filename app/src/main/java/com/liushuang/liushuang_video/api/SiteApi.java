package com.liushuang.liushuang_video.api;

import android.content.Context;

import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.Site;
import com.liushuang.liushuang_video.model.sohu.Video;

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

    /**
     * 取video相关信息
     * @param album
     * @param listener
     */
    public static void onGetVideo(int pageSize, int pageNo, Album album, OnGetVideoListener listener) {
        int siteId = album.getSite().getSiteId();
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetVideo(album, pageSize, pageNo, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetVideo(album,  pageSize, pageNo, listener);
                break;
        }
    }

    public static void onGetVideoPlayUrl(Video video, OnGetVideoPlayUrlListener listener) {
        int siteId = video.getSite();
        switch (siteId){
            case Site.LETV:
                new LetvApi().onGetVideoPlayUrl(video, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetVideoPlayUrl(video, listener);
                break;
        }
    }
}
