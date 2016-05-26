package com.jhs.inews.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.ui.MainActivity;
import com.jhs.inews.ui.user.userinfo.UserinfoFragment;

public class UserinfoActivity extends BaseActivity implements BaseFragment.BaseFragmentCallback {
    private UserinfoFragment userinfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("用户信息");
        userinfoFragment=new UserinfoFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        showFragment(R.id.user_content,userinfoFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onCallback(String tag, int eventID, Object data) {
        if (tag.equals(userinfoFragment.getClass().getSimpleName())) {
            if (eventID == 1) {
                //退出登录成功
                Intent intent =new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
    }
}
