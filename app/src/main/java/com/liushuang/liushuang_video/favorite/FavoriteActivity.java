package com.liushuang.liushuang_video.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.common.CommonActivity;

public class FavoriteActivity extends CommonActivity {

    @Override
    public String getTitleText() {
        return getResources().getString(R.string.favorite_title);
    }

    @Override
    public String getEmptyText() {
        return getResources().getString(R.string.favorite_empty);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getTableName() {
        return "favorite";
    }

    public static void launch(Activity activity) {
        
        Intent intent = new Intent(activity, FavoriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}