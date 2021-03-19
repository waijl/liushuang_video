package com.liushuang.liushuang_video.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liushuang.liushuang_video.AppManager;
import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.book.BookResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.entity.mime.Header;


public class NovelFragment extends Fragment {

    private static final String TAG = "NovelFragment";
    private ListView mListView;
    private List<BookResult.Book> mBookList;
    private AsyncHttpClient mClient;
    private static final String URL_BOOKLIST = "http://www.imooc.com/api/teacher?type=10";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novel, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                initPermission();
            }
        }
    }

    private void initView() {
        mListView = getView().findViewById(R.id.id_lv_bookList);
    }


    private void initData() {

        mClient = new AsyncHttpClient();
        mClient.get(URL_BOOKLIST, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                BookResult bookResult = AppManager.getGson().fromJson(result, BookResult.class);
                mBookList = bookResult.getData();
                mListView.setAdapter(new BookListAdapter());
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void initPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission1 != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        /*if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission_group.STORAGE}, 1);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // TODO: 2021/3/18
        }
    }

    class BookListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBookList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBookList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BookResult.Book book = (BookResult.Book) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_book, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mNameTextView.setText(book.getBookname());

//            Android10以上已经过时
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/imooc/" + book.getBookname() + ".txt";
//            String path = getActivity().getFilesDir().getAbsolutePath() + "/book/" + book.getBookname() + ".txt";
//            File file = new File(getActivity().getExternalFilesDir(null), "/book/" + book.getBookname() + ".txt");

//            /storage/emulated/0/Android/data/yourPackageName/files/novel/bookname.txt
            String path = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/novel/" + book.getBookname() + ".txt";
//            Log.d(TAG, "getView: path = " + path);
            File file = new File(path);
            holder.mButton.setText(file.exists() ? "打开" : "下载");
            ViewHolder finalHolder = holder;
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        // TODO: 2021/3/18
                    } else {
                        mClient.addHeader("Accept-Encoding", "identity");
                        mClient.get(book.getBookfile(), new FileAsyncHttpResponseHandler(file) {
                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                                finalHolder.mButton.setText("下载");
                                Toast.makeText(getActivity(), "下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                                finalHolder.mButton.setText("打开");

                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                finalHolder.mButton.setText(String.valueOf(bytesWritten * 100 / totalSize) + "%");
                            }

                        });
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            public TextView mNameTextView;
            public Button mButton;

            public ViewHolder(View view) {
                mNameTextView = view.findViewById(R.id.id_tv_bookName);
                mButton = view.findViewById(R.id.id_btn_read);
            }
        }

    }
}