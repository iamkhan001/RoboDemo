package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by jingwc on 2018/2/28.
 */

public class WeatherView extends FrameLayout {

    Context mContext;

    TextView tv_time;

    TextView tv_city;

    TextView tv_date;

    TextView tv_current_temp;

    TextView tv_night_day_temp;

    ImageView iv_weather;

    public WeatherView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.layout_weather, this, true);

        tv_time = findViewById(R.id.tv_time);
        tv_city = findViewById(R.id.tv_city);
        tv_date = findViewById(R.id.tv_date);
        tv_current_temp = findViewById(R.id.tv_current_temp);
        tv_night_day_temp = findViewById(R.id.tv_night_day_temp);
        iv_weather = findViewById(R.id.iv_weather);

    }

    public void setWeather(@DrawableRes int resId){
        if(iv_weather != null){
            iv_weather.setImageResource(resId);
        }
    }

    public void setNightDayTemp(String nightDayTemp){
        if(tv_night_day_temp != null){
            tv_night_day_temp.setText(nightDayTemp);
        }
    }

    public void setCurrentTemp(String currentTemp){
        if(tv_current_temp != null){
            tv_current_temp.setText(currentTemp);
        }
    }

    public void setTime(String time){
        if(tv_time != null){
            tv_time.setText(time);
        }
    }

    public void setCity(String city){
        if(tv_city != null){
            tv_city.setText(city);
        }
    }

    public void setDate(String date){
        if(tv_date != null){
            tv_date.setText(date);
        }
    }

}
