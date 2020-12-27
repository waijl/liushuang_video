package com.liushuang.liushuang_video.api;

import com.liushuang.liushuang_video.model.AlbumList;
import com.liushuang.liushuang_video.model.ErrorInfo;

public interface OnGetChannelAlbumListener {
    void onGetChannelAlbumSuccess(AlbumList albumList);
    void onGetChannelAlbumFailed(ErrorInfo errorInfo);
}
