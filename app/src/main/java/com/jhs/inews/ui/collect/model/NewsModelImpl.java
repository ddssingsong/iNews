package com.jhs.inews.ui.collect.model;

import android.content.Context;

import com.jhs.inews.entity.News;
import com.jhs.inews.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dds on 2016/5/7.
 */
public class NewsModelImpl implements NewsModel {
    @Override
    public void loadNews(Context context, final OnLoadNewsListListener listener) {
        String userId = (String) User.getObjectByKey(context, "objectId");
        BmobQuery<News> query = new BmobQuery<News>();
        query.addWhereEqualTo("uid", userId);
        query.setLimit(10);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        query.findObjects(context, new FindListener<News>() {
            @Override
            public void onSuccess(List<News> list) {
                listener.onSuccess(list);

            }

            @Override
            public void onError(int i, String s) {
                listener.onFailure(i, s);

            }
        });
    }

    public interface OnLoadNewsListListener {
        void onSuccess(List<News> list);

        void onFailure(int i, String s);
    }

}
