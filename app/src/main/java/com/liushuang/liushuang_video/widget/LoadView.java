package com.liushuang.liushuang_video.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.liushuang.liushuang_video.R;

public class LoadView extends LinearLayout {
    public LoadView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.loading_view_layout, this);
    }
}
