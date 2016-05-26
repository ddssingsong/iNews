package com.jhs.inews.ui.user.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.User;
import com.jhs.inews.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dds on 2016/3/26.
 */
public class RegisterFragment extends BaseFragment {

    @Bind(R.id.regit_user_name)
    EditText regitUserName;
    @Bind(R.id.regit_password)
    EditText regitPassword;
    @Bind(R.id.regit_password2)
    EditText regitPassword2;
    @Bind(R.id.regit_email)
    EditText regitEmail;
    @Bind(R.id.regit_email_ver)
    EditText regitEmailVer;
    @Bind(R.id.regit_getemailver)
    Button regitGetemailver;
    @Bind(R.id.regit_register)
    Button regitRegister;


    @Override
    protected boolean onBackPressed() {
        callbackToActivity(1, null);
        return false;
    }

    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onInitloadData() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.regit_getemailver, R.id.regit_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regit_getemailver:
                //获取验证码


                break;
            case R.id.regit_register:
                //注册

                handleRegister();
                break;
        }
    }

    /**
     * 注册逻辑
     */
    private void handleRegister() {
        regitUserName.setError(null);
        regitPassword.setError(null);
        regitPassword2.setError(null);
        regitEmail.setError(null);
        regitEmailVer.setError(null);


        String username = regitUserName.getText().toString();
        String password = regitPassword.getText().toString();
        String password2 = regitPassword2.getText().toString();
        String email = regitEmail.getText().toString();
        String emailVerified = regitEmailVer.getText().toString();
        boolean cancel = false;
        View focusView = null;



        if (TextUtils.isEmpty(email)) {
            regitEmail.setError(getString(R.string.error_field_required));
            focusView = regitEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            regitEmail.setError(getString(R.string.error_invalid_email));
            focusView = regitEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(emailVerified)) {
            regitEmailVer.setError(getString(R.string.error_emailver_required));
            focusView = regitEmailVer;
            cancel = true;
        }

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            regitPassword.setError(getString(R.string.error_invalid_password));
            focusView = regitPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(password2) || !password2.equals(password)) {
            regitPassword2.setError(getString(R.string.error_incorrect_password));
            focusView = regitPassword2;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            regitUserName.setError(getString(R.string.error_username_required));
            focusView = regitUserName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUp(getContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtil.showToast(getContext(), "注册成功");
                    callbackToActivity(2, null);

                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.showToast(getContext(), s);
                }
            });

        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


}
