package com.liushuang.liushuang_video.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.login.HeaderActivity;
import com.liushuang.liushuang_video.login.LoginActivity;
import com.liushuang.liushuang_video.video.LocalVideoActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 0x01;
    public static final int REQUEST_CODE1 = 0x02;
    private CircleImageView mButton;
    private TextView mUsername;
    private ImageView mChangeTouXiang;
    private String username;
    private Bundle mBundle;
    private LinearLayout mLlReadRecord;
    private LinearLayout mLlMyDownload;
    private LinearLayout mLlLocalVideo;
    private LinearLayout mLlMySettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mButton = getView().findViewById(R.id.btn_login);
        mUsername = getView().findViewById(R.id.id_tv_username);
        mChangeTouXiang = getView().findViewById(R.id.id_iv_changeTouXiang);
        mLlReadRecord = getView().findViewById(R.id.id_ll_readRecord);
        mLlMyDownload = getView().findViewById(R.id.id_ll_myDownload);
        mLlLocalVideo = getView().findViewById(R.id.id_ll_localVideo);
        mLlMySettings = getView().findViewById(R.id.id_ll_mySettings);

        mLlReadRecord.setOnClickListener(this);
        mLlMyDownload.setOnClickListener(this);
        mLlLocalVideo.setOnClickListener(this);
        mLlMySettings.setOnClickListener(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == REQUEST_CODE){
            mBundle = data.getExtras();
            username = mBundle.getString(LoginActivity.BUNDLE_USERNAME);
            mUsername.setText(username);
            mButton.setImageResource(R.drawable.waiwai);
        }

        if (requestCode == 0x11 && resultCode == 0x11){
            mBundle = data.getExtras();
            int imgId = mBundle.getInt("imageId");
            mButton.setImageResource(imgId);
        }

        if (username != null){
            mChangeTouXiang.setVisibility(View.VISIBLE);
            mChangeTouXiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HeaderActivity.class);
                    startActivityForResult(intent, 0x11);
                }
            });
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_ll_readRecord:
                // TODO: 2021/3/30
                break;
            case R.id.id_ll_myDownload:
                // TODO: 2021/3/30
                break;
            case R.id.id_ll_localVideo:
                LocalVideoActivity.launch(getActivity());
                break;
            case R.id.id_ll_mySettings:
                // TODO: 2021/3/30
                break;

        }
    }
}