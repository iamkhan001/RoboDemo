package com.csjbot.blackgaga.model.http.weather;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.WeatherBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */

public class WeatherImpl extends BaseImpl implements IWeather {

    @Override
    public WeatherService getRetrofit() {
        return getRetrofit(WeatherService.class);
    }

    @Override
    public void getWeather(String sn, String city, Observer<WeatherBean> observer) {
        scheduler(getRetrofit().getWeather(sn,city)).subscribe(observer);
    }
}
