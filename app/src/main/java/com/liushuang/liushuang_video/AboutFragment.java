package com.liushuang.liushuang_video;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.liushuang.liushuang_video.base.BaseFragment;

public class AboutFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        TextView textView = (TextView) bindViewId(R.id.tv_app_des);
        textView.setAutoLinkMask(Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
