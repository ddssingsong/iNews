package com.jhs.inews.ui.image;

import android.os.Bundle;
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
import com.jhs.inews.adapter.ImageAdapter;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.ImageInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dds on 2016/4/9.
 */
public class ImageFragment extends BaseFragment implements ImageAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh;
    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    private ImageAdapter adapter;

    private List<ImageInfo> newsInfoList;
    private LinearLayoutManager layoutManager;
    private int page = 0;
    private int pageSize = 15;

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onInitloadData() {
        initVar();
        initListener();
        onRefresh();
    }


    private void initListener() {
        adapter.setOnItemClickListener(this);
        swipe_refresh.setOnRefreshListener(this);
    }

    private void initVar() {
        adapter = new ImageAdapter();
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
                handlegetImageList();

            }
        }
    };

    private void handlegetImageList() {

        BmobQuery<ImageInfo> query = new BmobQuery<ImageInfo>();
        query.setLimit(pageSize);
        if (page != 0) {
            query.setSkip(pageSize * page + 1);
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        query.findObjects(getActivity(), new FindListener<ImageInfo>() {
            @Override
            public void onSuccess(List<ImageInfo> list) {
                if (list != null && list.size() != 0) {
                    if (page == 0) {
                        adapter.addDataToAdapter(list, true);
                        newsInfoList = list;
                    } else {
                        adapter.addDataToAdapter(list, false);
                        newsInfoList.addAll(list);
                    }
                } else {
                    View view = getActivity() == null ? recyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
                    Snackbar.make(view, getString(R.string.load_none), Snackbar.LENGTH_SHORT).show();
                    adapter.isShowFooter(false);
                }
                swipe_refresh.setRefreshing(false);

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
     * 点击列表项
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 0;
        handlegetImageList();


    }
}
