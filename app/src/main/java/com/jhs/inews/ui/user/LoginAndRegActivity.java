package com.jhs.inews.ui.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.ui.user.login.LoginFragment;
import com.jhs.inews.ui.user.login.RegisterFragment;
import com.jhs.inews.utils.ToastUtil;


public class LoginAndRegActivity extends BaseActivity implements BaseFragment.BaseFragmentCallback {
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("登陆");
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();


    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceFragment(R.id.login_content, loginFragment);
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
        if (tag.equals(loginFragment.getClass().getSimpleName())) {
            if (eventID == 1) {
                //登录成功
                ToastUtil.showToast(this, "登录成功");
                this.finish();
            }
            if (eventID == 2) {
                replaceFragment(R.id.login_content, registerFragment, true);
                setTitle("注册");
            }
        }
        if (tag.equals(registerFragment.getClass().getSimpleName())) {
            if (eventID == 1) {
                setTitle("登录");

            }
            if (eventID == 2) {
                //注册成功
                finish();

            }

        }
    }
}

