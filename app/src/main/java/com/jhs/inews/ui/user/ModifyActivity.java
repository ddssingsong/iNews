package com.jhs.inews.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.ui.user.modify.NickNameFragment;
import com.jhs.inews.ui.user.modify.SignFragment;
import com.jhs.inews.utils.ToastUtil;


/**
 * Created by dds on 2016/4/24.
 */
public class ModifyActivity extends BaseActivity implements BaseFragment.BaseFragmentCallback {
    private String type;//修改的类型
    private NickNameFragment nickNameFragment;
    private SignFragment signFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("修改信息");
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        if (type.equals("nickname")) {
            nickNameFragment = new NickNameFragment();
            replaceFragment(R.id.modify_content, nickNameFragment);
            setTitle("修改昵称");
        }
        if (type.equals("sign")) {
            signFragment = new SignFragment();
            replaceFragment(R.id.modify_content, signFragment);
            setTitle("修改签名");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if(tag.equals(NickNameFragment.class.getSimpleName())){
            if(eventID==1){
                //修改成功
                ToastUtil.showToast(this, "修改成功");
                String nickname= (String) data;
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", nickname);
                //设置返回数据
                this.setResult(4, intent);

                finish();
            }
            if(eventID==2){
                //修改失败
                ToastUtil.showToast(this,"网络错误，请稍后再试");
                finish();
            }
        }
        if(tag.equals(SignFragment.class.getSimpleName())){
            if(eventID==1){
                //修改成功
                ToastUtil.showToast(this, "修改成功");
                String sign=(String) data;
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", sign);
                //设置返回数据
                this.setResult(5, intent);
                finish();
            }
            if(eventID==2){
                //修改失败
                ToastUtil.showToast(this,"网络错误，请稍后再试");
                finish();
            }
        }

    }
}
