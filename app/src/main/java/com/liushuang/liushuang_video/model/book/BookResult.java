package com.liushuang.liushuang_video.model.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookResult implements Serializable {
    /**
     * status : 1
     * data : [{"bookname":"幻兽少年","bookfile":"http://www.imooc.com/data/teacher/down/幻兽少年.txt"},{"bookname":"魔界的女婿","bookfile":"http://www.imooc.com/data/teacher/down/魔界的女婿.txt"},{"bookname":"盘龙","bookfile":"http://www.imooc.com/data/teacher/down/盘龙.txt"},{"bookname":"庆余年","bookfile":"http://www.imooc.com/data/teacher/down/庆余年.txt"},{"bookname":"武神空间","bookfile":"http://www.imooc.com/data/teacher/down/武神空间.txt"}]
     * msg : 成功
     */

    @SerializedName("status")
    private Integer mStatus;
    @SerializedName("msg")
    private String mMessage;

    @SerializedName("data")

    private List<Book> mData;

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public List<Book> getData() {
        return mData;
    }

    public void setData(List<Book> mData) {
        this.mData = mData;
    }



    public static class Book implements Serializable {
        /**
         * bookname : 幻兽少年
         * bookfile : http://www.imooc.com/data/teacher/down/幻兽少年.txt
         */

        @SerializedName("bookname")
        private String mBookname;

        @SerializedName("bookfile")
        private String mBookfile;

        public String getBookname() {
            return mBookname;
        }

        public void setBookname(String mBookname) {
            this.mBookname = mBookname;
        }

        public String getBookfile() {
            return mBookfile;
        }

        public void setBookfile(String mBookfile) {
            this.mBookfile = mBookfile;
        }

    }
}
