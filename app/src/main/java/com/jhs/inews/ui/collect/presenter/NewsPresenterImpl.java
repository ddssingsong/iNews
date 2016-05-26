package com.jhs.inews.ui.collect.presenter;

import android.content.Context;

import com.jhs.inews.entity.News;
import com.jhs.inews.ui.collect.model.NewsModel;
import com.jhs.inews.ui.collect.model.NewsModelImpl;
import com.jhs.inews.ui.collect.view.NewsView;

import java.util.List;

/**
 * Created by dds on 2016/5/7.
 */
public class NewsPresenterImpl implements NewsPresenter, NewsModelImpl.OnLoadNewsListListener {
    private NewsModel newsModel;
    private NewsView newsView;


    public NewsPresenterImpl(NewsView newsView) {
        newsModel = new NewsModelImpl();
        this.newsView = newsView;

    }

    @Override
    public void loadNews(Context context,int pageIndex) {
        if(pageIndex==0){
            newsView.showProgress();
        }
        newsModel.loadNews(context,this);
    }


    @Override
    public void onSuccess(List<News> list) {
        newsView.addNews(list);
        newsView.hideProgress();
    }

    @Override
    public void onFailure(int i, String s) {
        newsView.hideProgress();
        newsView.showLoadFailMsg();

    }
}
