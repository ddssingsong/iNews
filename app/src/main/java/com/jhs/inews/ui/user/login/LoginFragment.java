package com.jhs.inews.ui.user.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.User;
import com.jhs.inews.utils.ToastUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dds on 2016/3/26.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView jump_register;
    private TextView login_qq;
    private TextView login_wechat;


    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        login_qq = (TextView) view.findViewById(R.id.login_qq);
        login_wechat = (TextView) view.findViewById(R.id.login_wechat);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        jump_register = (TextView) view.findViewById(R.id.jump_register);
        jump_register.setOnClickListener(this);
        login_qq.setOnClickListener(this);
        login_wechat.setOnClickListener(this);


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);


    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            /**
             * 登录核心代码
             */
            User user = new User();
            user.setUsername(email);
            user.setPassword(password);
            user.login(getActivity(), new SaveListener() {
                @Override
                public void onSuccess() {

                    callbackToActivity(1, null);
                    showProgress(false);

                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.showToast(getActivity(), "登录失败--用户名或密码错误");
                    showProgress(false);
                }
            });


        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onInitloadData() {

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }


    /**
     * 第三方登录和跳到注册界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_qq:
                //qq登录
                handleLoginQQ();
                break;
            case R.id.login_wechat:
                //微信登录
                handleLoginWechat();
                break;
            case R.id.jump_register:
                callbackToActivity(2, null);//跳到注册
                break;

        }

    }

    /**
     * 微信登录
     */

    private void handleLoginWechat() {


    }

    /**
     * qq登录
     */


    private void handleLoginQQ() {



    }



}
