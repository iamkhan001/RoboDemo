package com.csjbot.blackgaga.model.http.weather;

import com.csjbot.blackgaga.model.http.bean.WeatherBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jingwc on 2018/3/1.
 */

public interface WeatherService {

    @GET("csjbotservice/api/getWeather")
    Observable<WeatherBean> getWeather(@Query("sn") String sn,@Query("city") String city);
}