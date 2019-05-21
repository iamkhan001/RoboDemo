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

import com.csjbot.blackgaga.R;

/**
 * Created by jingwc on 2017/12/21.
 */

public class BatteryView extends FrameLayout {

    Context mContext;

    ImageView iv_battery;

    public BatteryView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BatteryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BatteryView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.layout_battery, this, true);

        iv_battery = findViewById(R.id.iv_battery);
    }

    public void setBatteryImage(@DrawableRes int resId){
        iv_battery.setImageResource(resId);
    }
}
