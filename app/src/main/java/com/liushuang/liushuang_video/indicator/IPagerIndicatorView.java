package com.liushuang.liushuang_video.indicator;

import java.util.List;


public interface IPagerIndicatorView extends IPagerChangeListener {

    void setPostionDataList(List<PositionData> list);
}
