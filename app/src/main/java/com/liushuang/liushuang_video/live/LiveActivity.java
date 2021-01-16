package com.liushuang.liushuang_video.live;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;

public class LiveActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(getResources().getString(R.string.live_title));

        mRecyclerView = bindViewId(R.id.ry_live);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        manager.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MyDecoration(this));

        LiveItemAdapter adapter = new LiveItemAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scrollToPosition(0);



    }

    @Override
    protected void initData() {

    }

    public static void launch(Activity activity){
        Intent intent = new Intent(activity, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}