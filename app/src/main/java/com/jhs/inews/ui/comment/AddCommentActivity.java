package com.jhs.inews.ui.comment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jhs.inews.R;
import com.jhs.inews.base.BaseActivity;
import com.jhs.inews.entity.Comment;
import com.jhs.inews.entity.User;
import com.jhs.inews.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;

public class AddCommentActivity extends BaseActivity {

    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("评论");
        nid = getIntent().getStringExtra("nid");

    }

    @OnClick(R.id.btn_submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String cdate = format.format(date);
                String ccontent = etContent.getText().toString().trim();
                if (etContent.getText() == null || ccontent.equals("")) {
                    etContent.setError("请输入内容");
                    return;
                }
                User userInfo = User.getCurrentUser(this, User.class);
                String username = "匿名";
                String userId = "匿名";
                String iconUrl = "http://file.bmob.cn/M03/1A/F9/oYYBAFcFJg2AAIpHAACdc7JtZJw878.png";
                if (userInfo != null) {
                    username = (String) User.getObjectByKey(this, "nickname");
                    userId = (String) User.getObjectByKey(this, "objectId");
                    iconUrl = (String) User.getObjectByKey(this, "iconUrl");
                }
                Comment comment = new Comment();
                comment.setUid(userId);
                comment.setNid(nid);
                comment.setCauthor(username);
                comment.setCdate(cdate);
                comment.setCcontent(ccontent);
                comment.setIcon(iconUrl);


                comment.save(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showToast(AddCommentActivity.this, "提交成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.showToast(AddCommentActivity.this, "网络错误");
                    }
                });
                break;
        }

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
