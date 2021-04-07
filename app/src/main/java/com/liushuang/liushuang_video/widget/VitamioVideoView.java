package com.liushuang.liushuang_video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.widget.VideoView;

public class VitamioVideoView extends VideoView {

    /**
     * 在代码中创建时一般调用
     * @param context
     */
    public VitamioVideoView(Context context) {
        this(context, null);
    }

    /**
     * 当这个类在布局文件时，系统通过该构造方法实例化该类
     * @param context
     * @param attrs
     */
    public VitamioVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设置样式的时候调用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VitamioVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置视频的宽和高
     * @param width
     * @param height
     */
    public void setVideoSize(int width, int height){
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }
}
