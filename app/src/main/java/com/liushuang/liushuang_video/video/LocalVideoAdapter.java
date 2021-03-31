package com.liushuang.liushuang_video.video;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.media.MediaItem;
import com.liushuang.liushuang_video.utils.Utils;

import java.util.List;

public class LocalVideoAdapter extends BaseAdapter {

    private List<MediaItem> mMediaItems;
    private Context mContext;
    private Utils mUtils;

    public LocalVideoAdapter(Context context, List<MediaItem> mediaItems){
        mContext = context;
        mMediaItems = mediaItems;
        mUtils = new Utils();

    }

    @Override
    public int getCount() {
        return mMediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MediaItem mediaItem = mMediaItems.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_localvideo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(mContext, mediaItem.getSize()));
        viewHolder.tv_time.setText(mUtils.stringForTime((int) mediaItem.getDuration()));

        return convertView;
    }

    static class ViewHolder{
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_size;

        public ViewHolder(View view){
            iv_icon = view.findViewById(R.id.id_iv_icon);
            tv_name = view.findViewById(R.id.id_tv_name);
            tv_time = view.findViewById(R.id.id_tv_time);
            tv_size = view.findViewById(R.id.id_tv_size);
        }
    }
}
