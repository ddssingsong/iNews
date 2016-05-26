package com.jhs.inews.config;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by Administrator on 2016/3/4.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader(this);
        Bmob.initialize(this, "e496f4b4f78d32e81854c64a1ef612c7");//初始化Bomb后台


        BmobConfig config = new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                        //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(512 * 1024)
                .build();
        Bmob.getInstance().initConfig(config);
    }

    /**
     * 初始化UIL
     *
     * @param context
     */
    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(4);// 线程池内加载的数量
        config.threadPriority(Thread.NORM_PRIORITY - 2);// 设置线程的优先级,为默认的线程级别-2,(5-2=3)
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
        config.diskCacheSize(10 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);// 设置缓存形式为后进先出
        // config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
