package com.jhs.inews.ui.comment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jhs.inews.R;
import com.jhs.inews.adapter.CommListAdapter;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.entity.Comment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CommListActivity extends BaseActivity implements CommListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private CommListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int page = 0;
    private int pageSize = 15;
    private String nid;
    private List<Comment> commentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("评论");
        nid = getIntent().getStringExtra("nid");
        initView();
    }

    private void initView() {

        adapter = new CommListAdapter();
        swipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        //RecylerView
        recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(adapter);


        recycleView.addOnScrollListener(mOnScrollListener);
        adapter.setOnItemClickListener(this);
        swipeRefreshWidget.setOnRefreshListener(this);
        onRefresh();
    }

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            adapter.isShowFooter(true);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isShowFooter()) {
                //加载更多
                handlepost();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }
    };

    private void handlepost() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("nid", nid);
        if (page != 0) {
            query.setSkip(pageSize * page + 1);
        }
        query.setLimit(pageSize);
        query.findObjects(CommListActivity.this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list != null && list.size() != 0) {
                    if (page == 0) {
                        adapter.addDataToAdapter(list, true);
                        commentlist = list;
                    } else {
                        adapter.addDataToAdapter(list, false);
                        commentlist.addAll(list);
                    }
                    swipeRefreshWidget.setRefreshing(false);
                    adapter.isShowFooter(false);
                } else {
                    View view = recycleView.getRootView();
                    Snackbar.make(view, getString(R.string.load_none), Snackbar.LENGTH_SHORT).show();
                    swipeRefreshWidget.setRefreshing(false);
                    adapter.isShowFooter(false);
                }
            }

            @Override
            public void onError(int i, String s) {
                View view = recycleView.getRootView();
                Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
                swipeRefreshWidget.setRefreshing(false);
                adapter.isShowFooter(false);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRefresh() {
        //请求数据
        page = 0;
        handlepost();
    }
}
