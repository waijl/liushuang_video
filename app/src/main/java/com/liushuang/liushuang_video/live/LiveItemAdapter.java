package com.liushuang.liushuang_video.live;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.player.PlayActivity;

public class LiveItemAdapter extends RecyclerView.Adapter<LiveItemAdapter.ViewHolder> {

    private Context mContext;
    // 数据集
    private String[] mDataList = new String[] {
            "CCTV-1 综合","CCTV-2 财经","CCTV-3 综艺","CCTV-4 中文国际(亚)","CCTV-5 体育",
            "CCTV-6 电影","CCTV-7 军事农业","CCTV-8 电视剧", "CCTV-9 纪录","CCTV-10 科教",
            "CCTV-11 戏曲","CCTV-12 社会与法","CCTV-13 新闻","CCTV-14 少儿","CCTV-15 音乐",
            "湖南卫视","北京卫视","天津卫视","湖北卫视","东方卫视",
    };

    private int[] mIconList = new int[] {
            R.drawable.cctv_1, R.drawable.cctv_2, R.drawable.cctv_3, R.drawable.cctv_4, R.drawable.cctv_5,
            R.drawable.cctv_6, R.drawable.cctv_7, R.drawable.cctv_8, R.drawable.cctv_9, R.drawable.cctv_10,
            R.drawable.cctv_11, R.drawable.cctv_12, R.drawable.cctv_13, R.drawable.cctv_14, R.drawable.cctv_15,
            R.drawable.hunan_tv,R.drawable.beijing_tv,R.drawable.tianjing_tv,R.drawable.hubei_tv,R.drawable.dongfang_tv,
    };

    private String [] mUrlList = new String[]{
            "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv2.m3u8",
            "http://117.169.124.36:6610/ysten-businessmobile/live/cctv-3/1.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv4.m3u8",
            "http://117.169.124.46:6410/ysten-businessmobile/live/hdcctv05plus/1.m3u8",
            "http://117.169.124.36:6610/ysten-businessmobile/live/cctv-6/1.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv7.m3u8",
            "http://117.169.124.36:6610/ysten-businessmobile/live/cctv-8/1.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv9.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv10.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv11.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv12.m3u8",
            "http://223.110.241.130:6610/gitv/live1/G_CCTV-13-HQ/.m3u8",
            "http://117.148.187.37/PLTV/88888888/224/3221226126/index.m3u8",
            "http://ivi.bupt.edu.cn/hls/cctv15.m3u8",
            "http://112.17.40.140/PLTV/88888888/224/3221226553/index.m3u8",
            "http://ivi.bupt.edu.cn/hls/btv1.m3u8",
            "http://112.17.40.140/PLTV/88888888/224/3221226412/index.m3u8",
            "http://223.110.243.171/PLTV/3/224/3221227211/index.m3u8",
            "http://ivi.bupt.edu.cn/hls/dfhd.m3u8"
    };

    public LiveItemAdapter(Context context) {
        mContext = context;
    }
    // view 相关
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // 数据相关
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mIcon.setImageResource(mIconList[position]);
        holder.mTitle.setText(mDataList[position]);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.launch((Activity)mContext, mUrlList[position], mDataList[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIcon;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_live_icon);
            mTitle = (TextView) itemView.findViewById(R.id.tv_live_title);
        }
    }

}
