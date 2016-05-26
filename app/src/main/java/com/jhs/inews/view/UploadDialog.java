package com.jhs.inews.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by dds on 2016/4/24.
 */
public class UploadDialog extends ProgressDialog {

    public UploadDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage("小编在努力奔跑中，请稍候…");
    }

}
