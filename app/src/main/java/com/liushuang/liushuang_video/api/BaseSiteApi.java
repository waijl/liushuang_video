package com.liushuang.liushuang_video.api;

import com.liushuang.liushuang_video.model.Channel;

public abstract class BaseSiteApi {

    public abstract void onGetChannelAlbums(Channel channel, int pageNum, int pageSize, OnGetChannelAlbumListener listener);
}
