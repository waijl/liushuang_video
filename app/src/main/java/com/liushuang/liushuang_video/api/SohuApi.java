package com.liushuang.liushuang_video.api;

import android.util.Log;

import com.liushuang.liushuang_video.AppManager;
import com.liushuang.liushuang_video.model.Album;
import com.liushuang.liushuang_video.model.AlbumList;
import com.liushuang.liushuang_video.model.Channel;
import com.liushuang.liushuang_video.model.ErrorInfo;
import com.liushuang.liushuang_video.model.Site;
import com.liushuang.liushuang_video.model.sohu.DetailResult;
import com.liushuang.liushuang_video.model.sohu.Result;
import com.liushuang.liushuang_video.model.sohu.ResultAlbum;
import com.liushuang.liushuang_video.model.sohu.Video;
import com.liushuang.liushuang_video.model.sohu.VideoList;
import com.liushuang.liushuang_video.model.sohu.VideoResult;
import com.liushuang.liushuang_video.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SohuApi extends BaseSiteApi{

    private static final String TAG = SohuApi.class.getSimpleName();
    private static final int SOHU_CHANNELID_MOVIE = 1; //搜狐电影频道ID
    private static final int SOHU_CHANNELID_SERIES = 2; //搜狐电视剧频道ID
    private static final int SOHU_CHANNELID_VARIETY = 7; //搜狐综艺频道ID
    private static final int SOHU_CHANNELID_DOCUMENTRY = 8; //搜狐纪录片频道ID
    private static final int SOHU_CHANNELID_COMIC = 16; //搜狐动漫频道ID
    private static final int SOHU_CHANNELID_MUSIC = 24; //搜狐音乐频道ID

    //某一专辑详情
    //http://api.tv.sohu.com/v4/album/info/9112373.json?plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_KEY = "plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    private final static String API_ALBUM_INFO = "http://api.tv.sohu.com/v4/album/info/";
    //http://api.tv.sohu.com/v4/search/channel.json?cid=2&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47&page=1&page_size=1
    private final static String API_CHANNEL_ALBUM_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=6.2.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";
    //http://api.tv.sohu.com/v4/album/videos/9112373.json?page=1&page_size=50&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_ALBUM_VIDOES_FORMAT = "http://api.tv.sohu.com/v4/album/videos/%s.json?page=%s&page_size=%s&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    // 播放url
    //http://api.tv.sohu.com/v4/video/info/3669315.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=9112373
    private final static String API_VIDEO_PLAY_URL_FORMAT = "http://api.tv.sohu.com/v4/video/info/%s.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=%s";
    //真实url格式 m3u8
    //http://hot.vrs.sohu.com/ipad3669271_4603585256668_6870592.m3u8?plat=6uid=f5dbc7b40dad477c8516885f6c681c01&pt=5&prod=app&pg=1
    @Override
    public void onGetChannelAlbums(Channel channel, int pageNum, int pageSize, OnGetChannelAlbumListener listener) {
        String url = getChannelAlbumUrl(channel, pageNum, pageSize);
        doGetChannelAlbumsByUrl(url, listener);
    }



    public void doGetChannelAlbumsByUrl(final String url, final OnGetChannelAlbumListener listener){
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null){
                    ErrorInfo errorInfo = buildErrorInfo(url, "doGetChannelAlbumsByUrl", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetChannelAlbumFailed(errorInfo);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    ErrorInfo errorInfo = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetChannelAlbumFailed(errorInfo);
                    return;
                }

                // 1、取到数据映射Result
                // 2、转换ResultAlbum变成Album
                // 3、Album存到AlbumLis中
                Result result = AppManager.getGson().fromJson(response.body().string(), Result.class);
                AlbumList albumList = toConvertAlbumList(result);

                if (albumList != null){
                    if (albumList.size() > 0 && listener != null){
                        listener.onGetChannelAlbumSuccess(albumList);
                    }else {
                        ErrorInfo errorInfo = buildErrorInfo(url, "doGetChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                        listener.onGetChannelAlbumFailed(errorInfo);
                    }
                }
            }
        });
    }

    private AlbumList toConvertAlbumList(Result result){

        if (result.getData().getResultAlbumList().size() > 0){
            AlbumList albumList = new AlbumList();
            for (ResultAlbum resultAlbum : result.getData().getResultAlbumList()){
                Album album = new Album(Site.SOHU);
                album.setAlbumDesc(resultAlbum.getTvDesc());
                album.setAlbumId(resultAlbum.getAlbumId());
                album.setHorImgUrl(resultAlbum.getHorHighPic());
                album.setMainActor(resultAlbum.getMainActor());
                album.setTip(resultAlbum.getTip());
                album.setTitle(resultAlbum.getAlbumName());
                album.setVerImgUrl(resultAlbum.getVerHighPic());
                album.setDirector(resultAlbum.getDirector());
                albumList.add(album);
            }
            return albumList;
        }
        return null;
    }

    /**
     * 构建ErrorInfo信息（即出错的信息）
     * @param url
     * @param functionName
     * @param e
     * @param type
     * @return
     */
    private ErrorInfo buildErrorInfo(String url, String functionName, Exception e, int type){
        ErrorInfo errorInfo = new ErrorInfo(Site.SOHU, type);
        errorInfo.setExceptionString(e.getMessage());
        errorInfo.setFunctionName(functionName);
        errorInfo.setUrl(url);
        errorInfo.setTag(TAG);
        errorInfo.setClassName(TAG);
        return errorInfo;
    }
    public String getChannelAlbumUrl(Channel channel, int pageNum, int pageSize){
        return String.format(API_CHANNEL_ALBUM_FORMAT, toConvertChannelId(channel), pageNum, pageSize);
    }

    public int toConvertChannelId(Channel channel){
        int channelId = -1;
        switch (channel.getChannelId()){
            case Channel.SHOW:
                channelId = SOHU_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = SOHU_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = SOHU_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = SOHU_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTRY:
                channelId = SOHU_CHANNELID_DOCUMENTRY;
                break;
            case Channel.VARIETY:
                channelId = SOHU_CHANNELID_VARIETY;
                break;
        }
        return channelId;
    }

    public void onGetAlbumDetail(final Album album, final OnGetAlbumDetailListener listener) {
        final String url = API_ALBUM_INFO + album.getAlbumId() + ".json?" + API_KEY;
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetAlbumDetail", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetAlbumDetailFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetAlbumDetail", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetAlbumDetailFailed(info);
                    return;
                }
                //Data
                DetailResult result = AppManager.getGson().fromJson(response.body().string(),  DetailResult.class);
                if (result.getResultAlbum() != null) {
                    if (result.getResultAlbum().getLastVideoCount() > 0) {
                        album.setVideoTotal(result.getResultAlbum().getLastVideoCount());
                    } else {
                        album.setVideoTotal(result.getResultAlbum().getTotalVideoCount());
                    }
                }
                //set完数据后,进行通知
                if (listener != null) {
                    listener.onGetAlbumDetailSuccess(album);
                }
            }
        });
    }

    @Override
    public void onGetVideo(Album album, int pageSize, int pageNo, OnGetVideoListener listener) {
        String url = String.format(API_ALBUM_VIDOES_FORMAT, album.getAlbumId(), pageNo, pageSize);
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideo", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetVideoFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info  = buildErrorInfo(url, "onGetVideo", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetVideoFailed(info);
                    return;
                }

//                Log.d(TAG, ">> onGetVideo response" + response.body().string());

                VideoResult result = AppManager.getGson().fromJson(response.body().string(), VideoResult.class);
                if (result != null){
                    VideoList videoList = new VideoList();
                    if (result.getData() != null){
                        for (Video video : result.getData().getVideoList()) {
                            Video video1 = new Video();
                            video1.setSite(album.getSite().getSiteId());
                            video1.setHorHighPic(video.getHorHighPic());
                            video1.setVerHighPic(video.getVerHighPic());
                            video1.setVid(video.getVid());
                            video1.setAid(video.getAid());
                            video1.setVideoName(video.getVideoName());
                            videoList.add(video1);
                        }

                        if (listener != null){
                            listener.onGetVideoSuccess(videoList);
                        }
                    }

                }
            }
        });
    }

}
