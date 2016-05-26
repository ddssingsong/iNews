package com.jhs.inews.ui.image;

import android.app.Activity;
import android.os.Bundle;

import com.jhs.inews.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoActivity extends Activity {

    @Bind(R.id.iv_photo)
    PhotoView ivPhoto;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        ImageLoader.getInstance().displayImage(url, ivPhoto);
        mAttacher = new PhotoViewAttacher(ivPhoto);
    }
}
