package com.liushuang.liushuang_video.api;

import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.AlbumList;
import com.liushuang.liushuang_video.model.ErrorInfo;

public interface OnGetAlbumDetailListener {
    void onGetAlbumDetailSuccess(Album album);
    void onGetAlbumDetailFailed(ErrorInfo errorInfo);
}
