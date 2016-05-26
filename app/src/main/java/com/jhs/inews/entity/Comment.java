package com.jhs.inews.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by dds on 2016/3/24.
 */
public class Comment extends BmobObject {
    private String uid;
    private String nid;
    private String cauthor;
    private String cdate;
    private String ccontent;
    private String icon;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getCauthor() {
        return cauthor;
    }

    public void setCauthor(String cauthor) {
        this.cauthor = cauthor;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCcontent() {
        return ccontent;
    }

    public void setCcontent(String ccontent) {
        this.ccontent = ccontent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
