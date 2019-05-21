package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/8/30.
 * @Package_name: BlackGaGa
 */
public class VideoViewFullScreen extends VideoView {
    public VideoViewFullScreen(Context context) {
        super(context);
    }

    public VideoViewFullScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewFullScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }
}
