package com.liushuang.liushuang_video.search;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.liushuang.liushuang_video.R;
import com.liushuang.liushuang_video.base.BaseActivity;

public class SearchActivity extends BaseActivity {


    private WebView mWebView;
    private ProgressBar mProgressBar;

    private static final int MAX_VALUE = 100;
//    private static final String SEARCH_URL =  "http://m.w3397.com/searchss.html";
//    private static final String SEARCH_URL =  "https://www.sofamv.com/";
    private static final String SEARCH_URL =  "https://z1.m1907.cn/";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    /*@Override
    protected void initView() {
        *//*mWebView = bindViewId(R.id.webview);
        mProgressBar = bindViewId(R.id.pb_progress);

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(mWebView.getSettings().getUserAgentString().concat("Android-APP"));

        mProgressBar.setMax(MAX_VALUE);

        mWebView.loadUrl(SEARCH_URL);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);//加载过程中更新进度
                if (newProgress == MAX_VALUE) {
                    mProgressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        //处理https
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //等待证书响应
                handler.proceed();
            }
        });

        // 如果页面中链接，如果希望点击链接继续在当前webView中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //监听到返回键被按下，并且当前网页可被返回
                if (KeyEvent.KEYCODE_BACK == keyCode && mWebView.canGoBack()) {
                    mWebView.goBack();
                    //返回true，交于系统处理
                    return true;
                }
                return false;
            }
        });*//*

    }*/

    @Override
    protected void initView() {
        mWebView = bindViewId(R.id.webview);
        mProgressBar = bindViewId(R.id.pb_progress);
        ImageButton mIbBack = bindViewId(R.id.id_ib_back);

        WebSettings settings = mWebView.getSettings();//用来设置webview的属性
//        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString().concat("Android-APP"));
        mProgressBar.setMax(MAX_VALUE);

        mIbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView.loadUrl(SEARCH_URL);
//        mWebView.setHttpAuthUsernamePassword(BLOG_URL,"Protected", "13320207235", "19761022");
        mWebView.setWebChromeClient(mWebChromeClient);
        //处理https
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //等待证书响应
                handler.proceed();
            }
        });

        // 如果页面中链接，如果希望点击链接继续在当前webView中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //监听到返回键被按下，并且当前网页可被返回
                if (KeyEvent.KEYCODE_BACK == keyCode && mWebView.canGoBack()) {
                    mWebView.goBack();
                    //返回true，交于系统处理
                    return true;
                }
                return false;
            }
        });

    }

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);//加载过程中更新进度
            if (newProgress == MAX_VALUE) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    protected void initData() {

    }

}