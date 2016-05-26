package com.jhs.inews.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jhs.inews.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"社会", "趣闻", "娱乐","苹果","美女","国际","体育"};
    private List<BaseFragment> fragments=new ArrayList<BaseFragment>() ;

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addViewToAdapter(BaseFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }


    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
