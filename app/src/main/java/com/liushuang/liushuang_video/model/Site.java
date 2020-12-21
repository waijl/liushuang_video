package com.liushuang.liushuang_video.model;

import android.content.Context;

/**
 * Created by hejunlin on 17/3/28.
 */

public class Site {

    public static final int SOHU = 1;
    public static final int LETV = 2;

    private int siteId;
    private String siteName;
    public static final int MAX_SITE = 2;

    public Site(int id) {
        siteId = id;
        switch (siteId) {
            case SOHU:
                siteName = "搜狐视频";
                break;
            case LETV:
                siteName = "乐视视频";
                break;
        }
    }

    public int getSiteId() {
        return siteId;
    }

    public String getSiteName(){
        return siteName;
    }

}
