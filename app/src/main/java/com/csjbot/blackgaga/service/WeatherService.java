package com.csjbot.blackgaga.service;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.WeatherBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.widget.WeatherView;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.csjbot.blackgaga.global.Constants.LocationInfo.city;


/**
 * 天气服务
 * Created by jingwc on 2018/2/28.
 */

public class WeatherService extends BaseService {

    WindowManager mWindowManager;

    WindowManager.LayoutParams wmBatteryParams;

    WeatherView mWeatherView;

    Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        createBatteryView();
        dateTimeShow();
        weatherShow();
    }

    private void weatherShow() {
        String city = "";
        try {
            if(TextUtils.isEmpty(Constants.LocationInfo.city)){
                new Handler().postDelayed(()->{
                    weatherShow();
                },5000);
                return;
            }else{
                city = URLEncoder.encode(Constants.LocationInfo.city,"utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServerFactory.createWeather().getWeather(ProductProxy.SN,city, new Observer<WeatherBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull WeatherBean weatherBean) {
                CsjlogProxy.getInstance().error("天气获取成功onNext:"+new Gson().toJson(weatherBean));
                if(weatherBean != null
                        && weatherBean.getResult() != null
                        && weatherBean.getResult().getData() != null
                        && weatherBean.getResult().getData().getData() != null
                        && weatherBean.getResult().getData().getData().getInfo() != null
                        && weatherBean.getResult().getData().getData().getInfo().size() > 0
                        ){
                    WeatherBean.ResultBean.DataBeanX.DataBean.InfoBean infoBean = weatherBean.getResult().getData().getData().getInfo().get(0);
                    if(mWeatherView != null){
                        mWeatherView.setCurrentTemp(infoBean.getCurrentTemp()+"°C");
                        mWeatherView.setNightDayTemp(infoBean.getNightTemp()+"/"+infoBean.getDayTemp());
                        String weather = infoBean.getWeather();
                        if(weather.contains("雨")){
                            mWeatherView.setWeather(R.drawable.weather_rain);
                        }else if(weather.contains("雪")){
                            mWeatherView.setWeather(R.drawable.weather_snow);
                        }else if(weather.contains("雨夹雪")){
                            mWeatherView.setWeather(R.drawable.weather_sleet);
                        }else if(weather.contains("多云")){
                            mWeatherView.setWeather(R.drawable.weather_coudy);
                        }else if(weather.contains("阴")){
                            mWeatherView.setWeather(R.drawable.weather_yin);
                        }else if(weather.contains("晴")){
                            mWeatherView.setWeather(R.drawable.weather_sunny);
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("天气获取失败"+e.toString());
            }

            @Override
            public void onComplete() {
                CsjlogProxy.getInstance().error("天气获取成功");
            }
        });
    }

    private void dateTimeShow() {
        new Thread(() -> {
            while(true) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);
                int week = c.get(Calendar.DAY_OF_WEEK);

                String str = "";
                switch (week) {
                    case 0:
                        str = "星期日";
                        break;
                    case 1:
                        str = "星期一";
                        break;
                    case 2:
                        str = "星期二";
                        break;
                    case 4:
                        str = "星期三";
                        break;
                    case 5:
                        str = "星期四";
                        break;
                    case 6:
                        str = "星期五";
                        break;
                    case 7:
                        str = "星期六";
                        break;
                }
                String weekStr = str;
                CsjlogProxy.getInstance().info("year:" + year);
                CsjlogProxy.getInstance().info("month:" + month);
                CsjlogProxy.getInstance().info("day:" + day);
                CsjlogProxy.getInstance().info("hour:" + hour);
                CsjlogProxy.getInstance().info("minute:" + minute);
                CsjlogProxy.getInstance().info("week:" + week);

                mHandler.post(() -> {
                    mWeatherView.setTime(hour + ":" + (minute<10?("0"+minute):minute));
                    mWeatherView.setDate(month + "月" + day +"日"+ weekStr);
                    mWeatherView.setCity(city);
                });

                if(minute == 0 && second == 0){
                    weatherShow();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void createBatteryView() {
        mWeatherView = new WeatherView(getApplicationContext());
        wmBatteryParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmBatteryParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmBatteryParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmBatteryParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmBatteryParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmBatteryParams.x = 480;
        wmBatteryParams.y = 30;

        // 设置悬浮窗口长宽数据
        wmBatteryParams.width = 360;
        wmBatteryParams.height = 100;
        mWindowManager.addView(mWeatherView, wmBatteryParams);
    }


    private void removeBatteryView() {
        if (mWeatherView != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mWeatherView);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeBatteryView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
