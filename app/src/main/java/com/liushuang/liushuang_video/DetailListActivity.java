package com.liushuang.liushuang_video;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.Site;

import java.util.HashMap;

public class DetailListActivity extends BaseActivity {

    private static final String CHANNEL_ID = "channelId";
    private int mChannelId;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null){
            mChannelId = intent.getIntExtra(CHANNEL_ID, 0);
        }

        Channel channel = new Channel(mChannelId, this);
        String titleName = channel.getChannelName();

        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(titleName);

        mViewPager = bindViewId(R.id.pager);
        mViewPager.setAdapter(new SitePagerAdapter(getSupportFragmentManager(), this, mChannelId));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    class SitePagerAdapter extends FragmentPagerAdapter{

        private Context mContext;
        private int mChannelID;
        private HashMap<Integer, DetailListFragment> mPagerMap;
        public SitePagerAdapter(@NonNull FragmentManager fm, Context context, int channelId) {
            super(fm);
            mContext = context;
            mChannelID = channelId;
            mPagerMap = new HashMap<>();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object obj =  super.instantiateItem(container, position);
            if (obj instanceof DetailListFragment){
                mPagerMap.put(position, (DetailListFragment)obj);
            }
            return obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
            mPagerMap.remove(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = DetailListFragment.newInstance(position + 1, mChannelID);
            return fragment;
        }

        @Override
        public int getCount() {
            return Site.MAX_SITE;
        }
    }
    public static void launchDetailListActivity(Context context, int channelId){
        Intent intent = new Intent(context, DetailListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHANNEL_ID, channelId);
        context.startActivity(intent);
    }
}