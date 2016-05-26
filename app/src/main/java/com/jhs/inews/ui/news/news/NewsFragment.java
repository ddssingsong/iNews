package com.jhs.inews.ui.news.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhs.inews.R;
import com.jhs.inews.adapter.NewsPagerAdapter;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.config.WebInterface;

/**
 * Created by dds on 2016/3/6.
 */
public class NewsFragment extends BaseFragment {
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private NewsPagerAdapter adapter;


    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initView(view);
        initVar();
        return view;
    }


    private void initView(View view) {
        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

    }

    private void initVar() {
        adapter = new NewsPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onInitloadData() {
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_SOCIAL));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_QUWEN));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_HUABIAN));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_APPLE));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_MEINV));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_WORLD));
        adapter.addViewToAdapter(NewsListFragment.newInstance(WebInterface.NEWS_TIYU));
    }

    @Override
    public void ResetData() {
        super.ResetData();

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }
}
