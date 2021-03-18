package com.liushuang.liushuang_video.home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liushuang.liushuang_video.AppManager;
import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.model.book.BookResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NovelFragment extends Fragment {

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
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                BookResult bookResult = AppManager.getGson().fromJson(result, BookResult.class);
                mBookList = bookResult.getData();
                mListView.setAdapter(new BookListAdapter());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    class BookListAdapter extends BaseAdapter{

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
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_list_book, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mNameTextView.setText(book.getBookname());
            return convertView;
        }

        class ViewHolder{
            public TextView mNameTextView;
            public Button mButton;

            public ViewHolder(View view){
                mNameTextView = view.findViewById(R.id.id_tv_bookName);
                mButton = view.findViewById(R.id.id_btn_read);
            }
        }

    }
}