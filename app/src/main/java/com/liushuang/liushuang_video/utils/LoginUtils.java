package com.liushuang.liushuang_video.utils;

import com.liushuang.liushuang_video.login.OnLoginListener;
import com.liushuang.liushuang_video.login.OnTouXiangSelectedListener;

public class LoginUtils {
    private OnTouXiangSelectedListener mOnTouXiangSelectedListener;
    private OnLoginListener mOnLoginListener;

    public LoginUtils(){}

    public void setOnTouXiangSelectedListener(OnTouXiangSelectedListener onTouXiangSelectedListener){
        mOnTouXiangSelectedListener = onTouXiangSelectedListener;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener){
        mOnLoginListener = onLoginListener;
    }

    public void onTouXiangSelected(int imgId){
        if (mOnTouXiangSelectedListener != null){
            mOnTouXiangSelectedListener.onTouXiangSelected(imgId);
        }
    }

    public void onLogin(String username){
        if(mOnLoginListener != null){
            mOnLoginListener.onLogin(username);
        }
    }
}
