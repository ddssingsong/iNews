package com.jhs.inews.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by dds on 2016/5/23.
 */
public class ImageInfo extends BmobObject {
    private String ct;
    private String img;
    private String title;
    private Integer type;

    public void setCt(String ct) {
        this.ct = ct;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCt() {
        return ct;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }
}
