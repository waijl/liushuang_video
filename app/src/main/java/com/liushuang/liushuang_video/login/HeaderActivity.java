package com.liushuang.liushuang_video.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.liushuang.liushuang_video.AppManager;
import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;
import com.liushuang.liushuang_video.home.MeFragment;
import com.liushuang.liushuang_video.utils.LoginUtils;

public class HeaderActivity extends Activity {

    private static final String TAG = "HeaderActivity";
    private LoginUtils mLoginUtils;

    private int[] imageId = new int[]{R.drawable.touxiang1, R.drawable.touxiang2,
            R.drawable.touxiang3, R.drawable.touxiang4, R.drawable.touxiang5, R.drawable.waiwai,
            R.mipmap.waiwai1, R.mipmap.waiwai2,R.mipmap.waiwai3,R.mipmap.waiwai4,R.mipmap.waiwai5,
            R.mipmap.waiwai6,R.mipmap.waiwai7,R.mipmap.waiwai8,R.mipmap.waiwai9,R.mipmap.waiwai10,
            R.mipmap.waiwai11,R.mipmap.waiwai12,R.mipmap.waiwai13,R.mipmap.waiwai14,R.mipmap.waiwai15,
            R.mipmap.waiwai16,R.mipmap.waiwai17,R.mipmap.waiwai18,R.mipmap.waiwai19,R.mipmap.waiwai20,
            R.mipmap.waiwai21,R.mipmap.waiwai22,R.mipmap.waiwai23,R.mipmap.waiwai24,R.mipmap.waiwai25,
            R.mipmap.waiwai26,R.mipmap.waiwai27,R.mipmap.waiwai28,R.mipmap.waiwai29,R.mipmap.waiwai30,
            R.mipmap.waiwai31,R.mipmap.waiwai32,R.mipmap.waiwai33,R.mipmap.waiwai34,R.mipmap.waiwai35,
            R.mipmap.waiwai36,R.mipmap.waiwai37,R.mipmap.waiwai38,R.mipmap.waiwai39,R.mipmap.waiwai40,
            R.mipmap.waiwai41,R.mipmap.waiwai42,R.mipmap.waiwai43,R.mipmap.waiwai44,R.mipmap.waiwai45,
            R.mipmap.waiwai46,R.mipmap.waiwai47,R.mipmap.waiwai48,R.mipmap.waiwai49,R.mipmap.waiwai50,
            R.mipmap.waiwai51,R.mipmap.waiwai52,R.mipmap.waiwai53,R.mipmap.waiwai54,R.mipmap.waiwai55,
            R.mipmap.waiwai56,R.mipmap.waiwai1,R.mipmap.waiwai57
    };   // 定义并初始化保存头像id的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);

        mLoginUtils = AppManager.getLoginUtils();

        GridView gridView = (GridView) findViewById(R.id.id_gv_touXiang);
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return imageId.length;
            }

            @Override
            public Object getItem(int position) {
                return imageId[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if (convertView == null){
                    imageView = new ImageView(HeaderActivity.this);
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxWidth(250);
                    imageView.setMaxHeight(250);
                    imageView.setPadding(1, 1, 5, 5);
                }else {
                    imageView = (ImageView) convertView;
                }
                imageView.setImageResource(imageId[position]);
                return imageView;
            }
        };

        gridView.setAdapter(baseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("imageId", imageId[position]);
                intent.putExtras(bundle);

                if (mLoginUtils != null){
                    mLoginUtils.onTouXiangSelected(imageId[position]);
                }

                setResult(0x11, intent);
                finish();
            }
        });
    }
}