package com.jhs.inews.weather.presenter;

import android.content.Context;

import com.jhs.inews.utils.AppUtil;
import com.jhs.inews.weather.model.WeatherModel;
import com.jhs.inews.weather.model.WeatherModelImpl;
import com.jhs.inews.weather.view.WeatherView;
import com.jhs.inews.entity.WeatherBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Description :
 * Author : lauren
 * Email  : lauren.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 2015/12/22
 */
public class WeatherPresenterImpl implements WeatherPresenter, WeatherModelImpl.LoadWeatherListener {

    private WeatherView mWeatherView;
    private WeatherModel mWeatherModel;
    private Context mContext;

    public WeatherPresenterImpl(Context context, WeatherView weatherView) {
        this.mContext = context;
        this.mWeatherView = weatherView;
        mWeatherModel = new WeatherModelImpl();
    }

    @Override
    public void loadWeatherData() {
        mWeatherView.showProgress();
        if (!AppUtil.isNetworkAvailable(mContext)) {
            mWeatherView.hideProgress();
            mWeatherView.showErrorToast("无网络连接");
            return;
        }

        WeatherModelImpl.LoadLocationListener listener = new WeatherModelImpl.LoadLocationListener() {
            @Override
            public void onSuccess(String cityName) {
                //定位成功，获取定位城市天气预报
                mWeatherView.setCity(cityName);
                mWeatherModel.loadWeatherData(cityName, WeatherPresenterImpl.this);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                mWeatherView.showErrorToast("定位失败");
                mWeatherView.setCity("北京");
                mWeatherModel.loadWeatherData("北京", WeatherPresenterImpl.this);
            }
        };
        //获取定位信息
        mWeatherModel.loadLocation(mContext, listener);
    }

    @Override
    public void onSuccess(List<WeatherBean> list) {
        if (list != null && list.size() > 0) {
            WeatherBean todayWeather = list.remove(0);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
            String str = format.format(date);
            mWeatherView.setToday(str + todayWeather.getDate());
            mWeatherView.setTemperature(todayWeather.getTemperature());
            mWeatherView.setWeather(todayWeather.getWeather());
            mWeatherView.setWind(todayWeather.getWind());
            mWeatherView.setWeatherImage(todayWeather.getImageRes());
        }
        mWeatherView.setWeatherData(list);
        mWeatherView.hideProgress();
        mWeatherView.showWeatherLayout();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mWeatherView.hideProgress();
        mWeatherView.showErrorToast("获取天气数据失败");
    }

}
