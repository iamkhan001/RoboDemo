package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017-10-25.
 */

public class Progress extends View {

    private int width  ;
    private int height;
    private Paint mPaint;
    private Paint mPaint_progress;
    private int progress;

    public Progress(Context context) {
        super(context);
        initView(context);
    }

    public Progress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Progress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //画笔
        mPaint = new Paint();
        mPaint_progress = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint_progress.setAntiAlias(true);
        //防抖动。
        mPaint.setDither(true);
        mPaint_progress.setDither(true);
        //设置画笔的颜色
        mPaint_progress.setColor(Color.RED);
        mPaint_progress.setStyle(Paint.Style.FILL);
        mPaint_progress.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.parseColor("#d7dce2"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
         width = getMeasuredWidth();
         height = getMeasuredHeight();
        canvas.drawRect(0, 0, width, height, mPaint);
        canvas.drawRect(0, 0, progress, height, mPaint_progress);
    }

    private void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
