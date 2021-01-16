package com.liushuang.liushuang_video.utils;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.WindowManager;

public class SysUtils {
    public static int getBrightness(Context context){
        return Settings.System.getInt(context.getContentResolver(), "screen_brightness", -1);
    }

    public static void setBrightness(Context context, int param){
        Settings.System.putInt(context.getContentResolver(), "screen_brightness", param);
    }


    //只能调节当前窗口的亮度
    public static void setLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    public static int getLight(Activity context){
        float screenBrightness = context.getWindow().getAttributes().screenBrightness;
        screenBrightness = screenBrightness * 255f;
        return (int) screenBrightness;
    }
    public static int getDefaultBrightness(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("shared_preferences_light", -1);
    }
}
