package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by xiasuhuei321 on 2017/11/4.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class KeyPointView extends FrameLayout {

    private TextView tv_point_name;
    private ImageView iv_maker;
    private float lastX, lastY;
    private boolean touch = false;
    private long current = System.currentTimeMillis();
    boolean isTouch = false;
    private int boundWidth;
    private int boundHeight;
    private boolean isBound = false;

    public KeyPointView(@NonNull Context context) {
        this(context, null);
    }

    public KeyPointView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyPointView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_point, this, true);
        tv_point_name = (TextView) findViewById(R.id.tv_point_name);
        iv_maker = (ImageView) findViewById(R.id.iv_maker);
    }

    public void setName(String name) {
        tv_point_name.setText(name);
    }

    public void setMaker(int resId) {
        iv_maker.setBackgroundResource(resId);
    }

    public void setIsTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public void setBound(int width, int height) {
        boundWidth = width;
        boundHeight = height;
    }

    public void setIsBound(boolean isBound) {
        this.isBound = isBound;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                current = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isTouch) {
                    float dx = event.getRawX() - lastX;
                    float dy = event.getRawY() - lastY;
                    if (Math.abs(dx) >= 15 || Math.abs(dy) >= 15) {
                        setX(getX() + dx);
                        setY(getY() + dy);

                        lastX = event.getRawX();
                        lastY = event.getRawY();
                    }
                    if (isBound) {
                        if (getX() >= boundWidth) {
                            setX(boundWidth);
                        }
                        if (getX() <= 0) {
                            setX(0);
                        }
                        if (getY() >= boundHeight) {
                            setY(boundHeight);
                        }
                        if (getY() <= 0) {
                            setY(0);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                long lastTime = System.currentTimeMillis() - current;
                if (lastTime <= 700) {
                    performClick();
                }
                break;

            default:
                break;
        }

        return true;
    }
}
