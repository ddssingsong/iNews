package com.jhs.inews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.entity.User;
import com.jhs.inews.ui.about.AboutActivity;
import com.jhs.inews.ui.collect.CollectFragment;
import com.jhs.inews.ui.image.ImageFragment;
import com.jhs.inews.ui.news.news.NewsFragment;
import com.jhs.inews.ui.user.LoginAndRegActivity;
import com.jhs.inews.ui.user.UserinfoActivity;
import com.jhs.inews.weather.widget.WeatherFragment;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CircleImageView user_icon;
    private TextView user_name;
    private NewsFragment newsFragment;
    private ImageFragment imageFragment;
    private WeatherFragment weatherFragment;
    private CollectFragment collectFragment;


    private boolean flag; //用户是否登录的标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
        initView();
        initListener();
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user_icon = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_icon);
        user_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        newsFragment = new NewsFragment();
        imageFragment = new ImageFragment();
        weatherFragment = new WeatherFragment();
        collectFragment = new CollectFragment();
        showFragment(R.id.frame_content, newsFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        User userInfo = User.getCurrentUser(this, User.class);
        if (userInfo != null) {
            // 显示头像和名称
            String username = (String) User.getObjectByKey(this, "username");
            user_name.setText(username);
            File file = new File(Environment.getExternalStorageDirectory(), "clip_photo.jpg");
            Uri uri = Uri.fromFile(file);
            user_icon.setImageURI(uri);
            flag = true;

        }


    }

    private void initListener() {
        user_icon.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //打开设置界面

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            showFragment(R.id.frame_content, newsFragment);
            setTitle("爱趣闻");
        } else if (id == R.id.nav_gallery) {
            showFragment(R.id.frame_content, imageFragment);
            setTitle("图片");
        } else if (id == R.id.nav_slideshow) {
            showFragment(R.id.frame_content, weatherFragment);
            setTitle("天气");
        } else if (id == R.id.nav_manage) {
            if (flag) {
                showFragment(R.id.frame_content, collectFragment);
                setTitle("新闻收藏");
            } else {
                startActivity(new Intent(this, LoginAndRegActivity.class));
            }


        } else if (id == R.id.nav_share) {
            //分享
            handleShare();

        } else if (id == R.id.nav_send) {
            //关于
            startActivity(new Intent(this, AboutActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 各种分享
     */
    private void handleShare() {
          ShareSDK.initSDK(this);
          OnekeyShare oks = new OnekeyShare();
          //关闭sso授权
          oks.disableSSOWhenAuthorize();

          // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
          oks.setTitleUrl("https://github.com/ddssingsong/iNews.git");
          // text是分享文本，所有平台都需要这个字段
          oks.setText("大帅出品，必属精品");
          // url仅在微信（包括好友和朋友圈）中使用
          oks.setUrl("https://github.com/ddssingsong/iNews.git");
         // 启动分享GUI
          oks.show(this);

    }

    /**
     * 用户信息
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (flag) {
            startActivity(new Intent(this, UserinfoActivity.class));
            return;
        }
        startActivity(new Intent(this, LoginAndRegActivity.class));
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}
