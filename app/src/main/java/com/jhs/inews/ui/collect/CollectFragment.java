package com.jhs.inews.ui.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhs.inews.R;
import com.jhs.inews.adapter.CollectNewsListAdapter;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.News;
import com.jhs.inews.ui.collect.presenter.NewsPresenter;
import com.jhs.inews.ui.collect.presenter.NewsPresenterImpl;
import com.jhs.inews.ui.collect.view.NewsView;
import com.jhs.inews.ui.news.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dds on 2016/5/7.
 */
public class CollectFragment extends BaseFragment implements NewsView, CollectNewsListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;
    CollectNewsListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private NewsPresenter mNewsPresenter;
    private List<News> datalist;

    private int pageindex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter = new NewsPresenterImpl(this);
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onInitloadData() {
        adapter = new CollectNewsListAdapter();
        swipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        //RecylerView
        recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.addOnScrollListener(mOnScrollListener);
        adapter.setOnItemClickListener(this);
        swipeRefreshWidget.setOnRefreshListener(this);
        onRefresh();


    }

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            adapter.isShowFooter(true);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isShowFooter()) {
                //加载更多

                mNewsPresenter.loadNews(getActivity(), pageindex + 10);
            }
        }
    };

    @Override
    public void showProgress() {
        swipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addNews(List<News> list) {
        adapter.isShowFooter(true);
        if (datalist == null) {
            datalist = new ArrayList<News>();
        }
        datalist.addAll(list);
        if (pageindex == 0) {
            adapter.addDataToAdapter(list, true);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if (list == null || list.size() == 0) {
                adapter.isShowFooter(false);
            }
            adapter.notifyDataSetChanged();
        }
        pageindex += 10;

    }

    @Override
    public void hideProgress() {
        swipeRefreshWidget.setRefreshing(false);
        adapter.isShowFooter(false);
    }

    @Override
    public void showLoadFailMsg() {
        if (pageindex == 0) {
            adapter.isShowFooter(false);
            adapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? recycleView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        News newsInfo = datalist.get(position);
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("news", newsInfo);
        startActivity(intent);


    }

    @Override
    public void onRefresh() {
        pageindex = 0;
        if (datalist != null) {
            datalist.clear();
        }
        mNewsPresenter.loadNews(getActivity(), pageindex);
    }
}
