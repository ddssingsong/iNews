package com.jhs.inews.base;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jhs.inews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by Administrator on 2016/3/4.
 */
public class BaseActivity extends AppCompatActivity implements BaseFragment.BackHandleInterface {
    // activity中初始化图片属性,这个一般写到baseActivity中
    public static DisplayImageOptions options;

    static {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.loaded_cry)
                .showImageOnFail(R.drawable.loaded_cry).resetViewBeforeLoading(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示,自动填充
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .displayer(new FadeInBitmapDisplayer(1000)).build();//渐渐显现

    }

    // --Fragment跳转-----------------------------------------------------------------
    public BaseFragment mBackHandedFragment;
    private static BaseFragment currentFragment;

    /**
     * 替换fragment,执行destroy方法---replace
     */
    public void replaceFragment(int contentID, BaseFragment toFragment) {
        replaceFragment(contentID, toFragment, false);
    }

    public void replaceFragment(int contentID, BaseFragment toFragment, boolean isAdd2Back) {
        String tag = toFragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(contentID, toFragment);
        if (isAdd2Back) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        currentFragment = toFragment;
    }

    /**
     * 显示fragment，隐藏其他fragment----hide-show
     */
    public void showFragment(int contentID, BaseFragment toFragment) {
        showFragment(contentID, toFragment, false);
    }

    public void showFragment(int contentID, BaseFragment toFragment, boolean isAdd2Back) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        String tag = toFragment.getClass().getSimpleName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            ft.add(contentID, toFragment, tag);
        } else {
            ft.show(toFragment);
        }
        if (isAdd2Back) {
            ft.addToBackStack(tag);
        }
        ft.commit();
        currentFragment = toFragment;
    }


    @Override
    public void setSelectedFragment(BaseFragment selectFragment) {
        this.mBackHandedFragment = selectFragment;

    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }

    }
}
