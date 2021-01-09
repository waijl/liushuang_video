package com.liushuang.liushuang_video.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String format = simpleDateFormat.format(date);
        return format;
    }
}
