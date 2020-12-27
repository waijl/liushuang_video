package com.liushuang.liushuang_video;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class AppManager extends Application {

    private static Gson mGson;
    private static OkHttpClient mOkHttpClient;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
        mContext = this;
    }

    public static Gson getGson(){
        return mGson;
    }

    public static OkHttpClient getHttpClient(){
        return mOkHttpClient;
    }

    public static Context getContext(){
        return mContext;
    }

    public static Resources getResource(){
        return mContext.getResources();
    }

    public static boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
