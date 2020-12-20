package com.liushuang.liushuang_video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected View mContextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContextView = getActivity().getLayoutInflater().inflate(getLayoutId(), container, false);
        initView();
        initData();
        return mContextView;
    }

    protected abstract int getLayoutId();
    protected abstract void initData();
    protected abstract void initView();

    protected <T extends View> T bindViewId(int resId){
        return (T) mContextView.findViewById(resId);
    }
}
