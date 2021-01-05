package com.liushuang.liushuang_video.api;

import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.sohu.Video;

public abstract class BaseSiteApi {

    public abstract void onGetChannelAlbums(Channel channel, int pageNum, int pageSize, OnGetChannelAlbumListener listener);

    public abstract void onGetAlbumDetail(Album album, OnGetAlbumDetailListener listener);

    public abstract void onGetVideo(Album album, int pageSize, int pageNo, OnGetVideoListener listener);

    public abstract void onGetVideoPlayUrl(Video video, OnGetVideoPlayUrlListener listener);
}
