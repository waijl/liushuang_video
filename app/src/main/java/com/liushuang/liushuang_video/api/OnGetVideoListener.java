package com.liushuang.liushuang_video.api;

import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.sohu.VideoList;

public interface OnGetVideoListener {
    void onGetVideoSuccess(VideoList videoList);
    void onGetVideoFailed(ErrorInfo errorInfo);
}
