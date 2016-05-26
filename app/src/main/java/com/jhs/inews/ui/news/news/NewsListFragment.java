package com.jhs.inews.ui.news.news;

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
import com.jhs.inews.adapter.NewsListAdapter;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.News;
import com.jhs.inews.ui.news.NewsDetailActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by dds on 2016/2/25.
 */
public class NewsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsListAdapter.OnItemClickListener {
    private NewsListAdapter adapter;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView;
    private List<News> newsInfoList;
    private LinearLayoutManager layoutManager;

    private String newsUrl;
    private int pageSize = 10;
    private int page = 0;


    public static NewsListFragment newInstance(String newsUrl) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putString("type", newsUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.newsUrl = getArguments().getString("type");
    }

    @Override
    protected boolean onBackPressed() {
        return true;
    }

    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        initView(view);
        initVar();
        initListener();
        return view;
    }

    @Override
    public void onInitloadData() {
        onRefresh();
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

    }

    private void initVar() {
        adapter = new NewsListAdapter();
        //SwipeRefreshLayout
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipe_refresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //RecylerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(mOnScrollListener);
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
                page++;
                handlerPost();
            }
        }
    };

    private void initListener() {
        adapter.setOnItemClickListener(this);
        swipe_refresh.setOnRefreshListener(this);
    }


    /**
     * 下拉刷新事件
     */
    @Override
    public void onRefresh() {
        //请求数据
        page = 0;
        handlerPost();
    }

    /**
     * 处理请求类
     */
    private void handlerPost() {
        BmobQuery<News> query = new BmobQuery<News>();
        query.addWhereEqualTo("nType", newsUrl);
        if (page != 0) {
            query.setSkip(pageSize * page + 1);
        }
        query.setLimit(pageSize);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE

        query.findObjects(getActivity(), new FindListener<News>() {
            @Override
            public void onSuccess(List<News> list) {
                if (list != null && list.size() != 0) {
                    if (page == 0) {
                        adapter.addDataToAdapter(list, true);
                        newsInfoList = list;
                    } else {
                        adapter.addDataToAdapter(list, false);
                        newsInfoList.addAll(list);
                    }
                    swipe_refresh.setRefreshing(false);

                } else {
                    View view = getActivity() == null ? recyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
                    Snackbar.make(view, getString(R.string.load_none), Snackbar.LENGTH_SHORT).show();
                    swipe_refresh.setRefreshing(false);
                    adapter.isShowFooter(false);
                }


            }

            @Override
            public void onError(int i, String s) {
                View view = getActivity() == null ? recyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
                Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
                swipe_refresh.setRefreshing(false);
                adapter.isShowFooter(false);

            }
        });


    }


    /**
     * RecylerView项的点击事件
     */

    @Override
    public void onItemClick(View view, int position) {
        News newsInfo = newsInfoList.get(position);
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("news", newsInfo);
        startActivity(intent);
    }


}