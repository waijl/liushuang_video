package com.liushuang.liushuang_video.utils;

import com.liushuang.liushuang_video.AppManager;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {

    private static final String REQUEST_TAG = "okhttp";

    public static Request buildRequest(String url){
        if (AppManager.isNetWorkAvailable()){
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
            return request;
        }

        return null;
    }

    public static void excute(String url, Callback callback){
        Request request = buildRequest(url);
        excute(request, callback);
    }

    public static void excute(Request request, Callback callback){
        AppManager.getHttpClient().newCall(request).enqueue(callback);
    }
}
