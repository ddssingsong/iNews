package com.jhs.inews.config;

/**
 * Created by dds on 2016/3/1.
 */
public class WebInterface {

    public static final String APKEY = "09ef3db646e37b03921c31224834ce72";

    /**
     * 新闻接口----------------------------------------------------------
     */
    public static final String HOST = "http://apis.baidu.com/txapi/";


    /**
     * 社会新闻
     */
    public static final String NEWS_SOCIAL = HOST + "social/social";
    /**
     * 娱乐新闻
     */
    public static final String NEWS_HUABIAN = HOST + "huabian/newtop";
    /**
     * 趣闻
     */
    public static final String NEWS_QUWEN = HOST + "qiwen/qiwen";
    /**
     * 美女图片
     */
    public static final String NEWS_MEINV = HOST + "mvtp/meinv";
    /**
     * 国际
     */
    public static final String NEWS_WORLD = HOST + "world/world";
    /**
     * 体育
     */
    public static final String NEWS_TIYU = HOST + "tiyu/tiyu";
    /**
     * 苹果
     */
    public static final String NEWS_APPLE = HOST + "apple/apple";
    /**
     * 健康
     */
    public static final String NEWSHEALTH = HOST + "health/health";
    /**
     * 科技
     */
    public static final String NEWSKEJI = HOST + "keji/keji";


    /**
     * 笑话图片接口
     */
    public static final String JOK_PIC = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_pic";

    // 天气预报url
    public static final String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

    //百度定位
    public static final String INTERFACE_LOCATION = "http://api.map.baidu.com/geocoder";

}
