package com.jhs.inews.ui.user.userinfo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jhs.inews.R;
import com.jhs.inews.base.BaseFragment;
import com.jhs.inews.entity.User;
import com.jhs.inews.ui.user.ModifyActivity;
import com.jhs.inews.utils.ToastUtil;
import com.jhs.inews.view.UploadDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.SexPicker;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dds on 2016/3/26.
 */
public class UserinfoFragment extends BaseFragment {

    @Bind(R.id.iv_user_icon)
    CircleImageView ivUserIcon;
    @Bind(R.id.btn_user_icon)
    RelativeLayout btnUserIcon;

    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.btn_nickname)
    RelativeLayout btnNickname;

    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.btn_username)
    RelativeLayout btnUsername;

    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.btn_phone)
    RelativeLayout btnPhone;

    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.btn_email)
    RelativeLayout btnEmail;

    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.btn_sex)
    RelativeLayout btnSex;

    @Bind(R.id.tv_ip)
    TextView tvIp;
    @Bind(R.id.btn_ip)
    RelativeLayout btnIp;

    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.btn_sign)
    RelativeLayout btnSign;

    @Bind(R.id.tv_sign_out)
    TextView tvSignOut;
    @Bind(R.id.btn_sign_out)
    RelativeLayout btnSignOut;

    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String CLIP_PHOTO_FILE_NAME = "clip_photo.jpg";
    private Bitmap bitmap;
    private File tempFile;
    private UploadDialog uploadDialog;


    @Override
    public View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onInitloadData() {
        //初始化界面数据
        File file = new File(Environment.getExternalStorageDirectory(), CLIP_PHOTO_FILE_NAME);
        Uri uri = Uri.fromFile(file);
        ivUserIcon.setImageURI(uri);
        User user = User.getCurrentUser(getActivity(), User.class);
        tvUsername.setText(user.getUsername());
        if (user.getNickname() != null) {
            tvNickname.setText(user.getNickname());
        }
        if (user.getMobilePhoneNumber() != null) {
            tvPhone.setText(user.getMobilePhoneNumber());
        }
        if (user.getEmail() != null) {
            tvEmail.setText(user.getEmail());
        }

        if (user.getGender() != null) {
            tvSex.setText(user.getGender());
        }
        if (user.getSign() != null) {
            tvSign.setText(user.getSign());
        }
        if (getLocalIpAddress() != null) {
            tvIp.setText(getLocalIpAddress());
        }

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @OnClick({R.id.btn_sign_out, R.id.btn_user_icon, R.id.btn_nickname, R.id.btn_phone, R.id.btn_email, R.id.btn_sex, R.id.btn_sign})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_sign_out:
                String[] stringItems1 = {"确认退出"};
                ActionSheetDialog dialogfinish = new ActionSheetDialog(getActivity(), stringItems1, null);
                dialogfinish.isTitleShow(false).show();
                dialogfinish.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //退出登录
                        User.logOut(getActivity());   //清除缓存用户对象
                        callbackToActivity(1, null);
                    }
                });


                break;

            case R.id.btn_user_icon:
                //点击上传图片
                String[] stringItems = {"点击拍照", "从相册中取"};
                final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, null);
                dialog.isTitleShow(false).show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //打开相机
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSdcard()) {
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                                }
                                startActivityForResult(intent, 1);
                                break;
                            case 1:
                                //打开相册
                                // 激活系统图库，选择一张图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK);
                                intent1.setType("image/*");
                                startActivityForResult(intent1, 2);
                                break;


                        }
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.btn_nickname:
                //昵称
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                intent.putExtra("type", "nickname");
                startActivityForResult(intent, 4);

                break;
            case R.id.btn_phone:
                //手机
                break;
            case R.id.btn_email:
                //邮箱
                break;
            case R.id.btn_sex:
                //性别

                handleModifyGender();
                break;
            case R.id.btn_sign:
                //个性签名
                Intent intent1 = new Intent(getActivity(), ModifyActivity.class);
                intent1.putExtra("type", "sign");
                startActivityForResult(intent1, 5);
                break;


        }
    }

    private void handleModifyGender() {
        SexPicker picker = new SexPicker(getActivity());
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                final String gender = option;
                User newUser = new User();
                newUser.setGender(gender);
                User bmobUser = User.getCurrentUser(getActivity(), User.class);
                newUser.update(getActivity(), bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        tvSex.setText(gender);

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtil.showToast(getActivity(), "更新失败");


                    }
                });

            }
        });
        picker.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                ToastUtil.showToast(getActivity(), "未找到存储卡，无法存储照片！");
            }

        }
        if (requestCode == 2) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        }
        if (requestCode == 3) {
            try {
                bitmap = data.getParcelableExtra("data");
                ivUserIcon.setImageBitmap(bitmap);
                //这里将剪切好的图片保存到本地
                saveBitmap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 4) {
            if (data != null) {
                String nickname = data.getExtras().getString("result");
                tvNickname.setText(nickname);

            }

        }
        if (requestCode == 5) {
            if (data != null) {
                String sign = data.getExtras().getString("result");
                tvSign.setText(sign);

            }
        }

    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, 3);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 保存方法
     */
    public void saveBitmap() {
        uploadDialog = new UploadDialog(getActivity());
        uploadDialog.show();
        File f = new File(Environment.getExternalStorageDirectory(), CLIP_PHOTO_FILE_NAME);
        if (f.exists()) {
            f.delete();
        }

        try {

            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //存进本地之后在上传到网络

        final BmobFile bmobFile = new BmobFile(f);
        bmobFile.uploadblock(getActivity(), new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                User newUser = new User();
                newUser.setPic(bmobFile);
                String url = bmobFile.getFileUrl(getActivity());
                newUser.setIconUrl(url);
                User bmobUser = User.getCurrentUser(getActivity(), User.class);
                newUser.update(getActivity(), bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        uploadDialog.dismiss();
                        ToastUtil.showToast(getActivity(), "上传成功");


                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        uploadDialog.dismiss();

                    }
                });


            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                uploadDialog.dismiss();
                ToastUtil.showToast(getActivity(), "上传失败");
            }
        });
    }


    /**
     * 获取ip地址
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
