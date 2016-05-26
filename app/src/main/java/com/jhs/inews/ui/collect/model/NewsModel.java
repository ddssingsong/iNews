package com.jhs.inews.ui.collect.model;

import android.content.Context;

/**
 * dds
 * Created by dds on 2016/5/7.
 */
public interface NewsModel {
    void loadNews(Context context, NewsModelImpl.OnLoadNewsListListener listener);

}
