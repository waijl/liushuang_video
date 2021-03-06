package com.liushuang.liushuang_video.api;


import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.sohu.Video;

/**
 * Created by hejunlin on 17/4/1.
 */

public interface OnGetVideoPlayUrlListener {

    void onGetSuperUrl(Video video, String url);//蓝光url

    void onGetNoramlUrl(Video video, String url);//高清url

    void onGetHighUrl(Video video, String url);//超清url

    void onGetFailed(ErrorInfo info);

    void onGetFluentUrl(Video video, String realUrl);//流畅url
}
