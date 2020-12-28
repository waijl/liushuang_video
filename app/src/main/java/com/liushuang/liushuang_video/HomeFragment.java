package com.liushuang.liushuang_video;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.liushuang.liushuang_video.base.BaseFragment;
import com.liushuang.liushuang_video.model.Channel;

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private GridView mGridView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Log.d(TAG, ">> initView");
        //轮播图插件LoopViewPager
        LoopViewPager viewPager = (LoopViewPager) bindViewId(R.id.looperviewpager);
        //轮播图中的圆点的定义
        CircleIndicator indicator = (CircleIndicator) bindViewId(R.id.indicator);
        viewPager.setAdapter(new HomePicAdapter(getActivity()));
        viewPager.setLooperPic(true);
        indicator.setViewPager(viewPager);

        mGridView = bindViewId(R.id.gv_channel);
        mGridView.setAdapter(new ChannelAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, ">> onItemClick" + (position+1));
                switch (position){
                    case 6:
                        //跳转直播
//                        LiveActivity.launch(getActivity());
                        break;
                    case 7:
                        //跳转收藏
//                        FavoriteActivity.launch(getActivity());
                        break;
                    case 8:
                        //跳转历史记录
//                        HistoryActivity.launch(getActivity());
                        break;
                    default:
                        //跳转对应频道
                        DetailListActivity.launchDetailListActivity(getActivity(), position + 1);
                        break;

                }
            }
        });
    }

    class ChannelAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Channel.MAX_COUNT;
        }

        @Override
        public Object getItem(int position) {
            return new Channel(position+1, getActivity());
        }

        @Override
        public long getItemId(int position) {
            return position+1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Channel channel = (Channel) getItem(position);
            ViewHolder holder = null;
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.home_grid_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_home_item_text);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_home_item_img);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(channel.getChannelName());
            int id = channel.getChannelId();
            int imgResId = -1;
            switch (id) {
                case Channel.SHOW:
                    imgResId = R.drawable.ic_show;
                    break;
                case Channel.MOVIE:
                    imgResId = R.drawable.ic_movie;
                    break;
                case Channel.COMIC:
                    imgResId = R.drawable.ic_comic;
                    break;
                case Channel.DOCUMENTRY:
                    imgResId = R.drawable.ic_movie;
                    break;
                case Channel.MUSIC:
                    imgResId = R.drawable.ic_music;
                    break;
                case Channel.VARIETY:
                    imgResId = R.drawable.ic_variety;
                    break;
                case Channel.LIVE:
                    imgResId = R.drawable.ic_live;
                    break;
                case Channel.FAVORITE:
                    imgResId = R.drawable.ic_bookmark;
                    break;
                case Channel.HISTORY:
                    imgResId = R.drawable.ic_history;
                    break;
            }
            holder.imageView.setImageDrawable(getActivity().getResources().getDrawable(imgResId));
            return convertView;
        }

    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
