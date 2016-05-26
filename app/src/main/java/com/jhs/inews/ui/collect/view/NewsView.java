package com.jhs.inews.ui.collect.view;

import com.jhs.inews.entity.News;

import java.util.List;

/**
 * Created by dds on 2016/5/7.
 */
public interface NewsView {
    void showProgress();

    void addNews(List<News> list);

    void hideProgress();

    void showLoadFailMsg();

}
