package com.jhs.inews.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.entity.Comment;
import com.jhs.inews.entity.News;
import com.jhs.inews.entity.User;
import com.jhs.inews.ui.comment.AddCommentActivity;
import com.jhs.inews.ui.comment.CommListActivity;
import com.jhs.inews.ui.user.LoginAndRegActivity;
import com.jhs.inews.view.VideoEnabledWebChromeClient;
import com.jhs.inews.view.VideoEnabledWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;


public class NewsDetailActivity extends BaseActivity {


    @Bind(R.id.videoLayout)
    RelativeLayout videoLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.loading_bar)
    ProgressBar loadingBar;
    @Bind(R.id.webview)
    VideoEnabledWebView webview;
    @Bind(R.id.toolbar_divider)
    View toolbarDivider;
    @Bind(R.id.action_favor)
    ImageView actionFavor;
    @Bind(R.id.action_write_comment)
    ImageView actionWriteComment;
    @Bind(R.id.action_view_comment)
    ImageView actionViewComment;
    @Bind(R.id.action_comment_count)
    TextView actionCommentCount;
    @Bind(R.id.action_repost)
    ImageView actionRepost;
    @Bind(R.id.action_report)
    ImageView actionReport;

    private News news;

    private VideoEnabledWebChromeClient webChromeClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("详情");
        initVar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("nid", news.getObjectId());
        query.count(this, Comment.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                actionCommentCount.setText(count+"");
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });

    }


    private void initVar() {
        news = (News) getIntent().getSerializableExtra("news");
        WebSettings setting = webview.getSettings();
        setting.setJavaScriptEnabled(true); // 启用JS脚本
        setting.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);


        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webview) // See all available constructors...
        {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webview.setWebChromeClient(webChromeClient);
        webview.setWebViewClient(new InsideWebViewClient());

        webview.loadUrl(news.getUrl());
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @OnClick({R.id.action_favor, R.id.action_write_comment, R.id.action_view_comment, R.id.action_comment_count, R.id.action_repost, R.id.action_report})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.action_favor:
                //收藏
                User userInfo = User.getCurrentUser(this, User.class);
                if (userInfo != null) {
                    String userId = (String) User.getObjectByKey(this, "objectId");
                    news.setUid(userId);
                    news.update(this, news.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            actionFavor.setSelected(true);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                } else {
                    startActivity(new Intent(this, LoginAndRegActivity.class));

                }
                break;
            case R.id.action_write_comment:
                //写评论
                Intent intent = new Intent(this, AddCommentActivity.class);
                intent.putExtra("nid", news.getObjectId());
                startActivity(intent);
                break;
            case R.id.action_view_comment:
                //看评论
                Intent intent1 = new Intent(this, CommListActivity.class);
                intent1.putExtra("nid", news.getObjectId());
                startActivity(intent1);
                break;
            case R.id.action_repost:
                //分享
                break;
            case R.id.action_report:
                //举报
                break;

        }

    }


    @Override
    protected void onPause() {
        webview.reload();
        super.onPause();
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
}
