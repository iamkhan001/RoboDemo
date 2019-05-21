package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.csjbot.blackgaga.R;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/30.
 */
public class BubbleProgressBar extends ProgressBar {
    /**
     * 气泡
     */
    private Bitmap bm;


    /**
     * 图片的宽度
     */
    private float mImgWidth;

    /**
     * 图片的高度
     */
    private float mImgHei;

    /**
     * 获取图片的资源
     */
    private Resources res;

    /**
     *
     */
    private Paint mPaint;

    /**
     * 气泡的左边距
     */
    private int imagepaddingleft;

    /**
     * 气泡的右边距
     */
    private int imagepaddingtop;

    /**
     * 更新前的顶部边距
     */
    private int oldPaddingTop;

    /**
     * 更新前的左边距
     */
    private int oldPaddingLeft;

    /**
     * 更新前的右边距
     */
    private int oldPaddingRight;

    /**
     * 更新前的底部边距
     */
    private int oldPaddingBottom;

    /**
     * 是否更新图片的padding
     */
    private boolean isMysetPadding = true;

    /**
     * 进度条提示文本
     */
    private String mText;

    /**
     * 文本宽度
     */
    private float mTextWidth;

    /**
     * 初始的字号
     */
    private int textsize = 20;

    /**
     * 文本左边距
     */
    private int textpaddingleft;

    /**
     * 文本上边距
     */
    private int textpaddingtop;


    public BubbleProgressBar(Context context) {
        super(context);
        init();
    }

    public BubbleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * (非 Javadoc)
     *
     * @param event
     * @return
     * @方法名: onTouchEvent
     * @描述: 不屏蔽屏蔽滑动
     * @日期: 2014-8-11 下午2:03:15
     * @see android.widget.AbsSeekBar#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void initBitmap() {
        bm = BitmapFactory.decodeResource(res, R.drawable.bubble);
        if (bm != null) {
            mImgWidth = bm.getWidth();
            mImgHei = bm.getHeight();
        } else {
            mImgWidth = 0;
            mImgHei = 0;
        }
    }

    /**
     * 修改setpadding 使其在外部调用的时候无效
     */
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (isMysetPadding) {
            super.setPadding(left, top, right, bottom);
        }
    }

    /**
     * 替代setpadding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setMyPadding(int left, int top, int right, int bottom) {
        oldPaddingTop = top;
        oldPaddingLeft = left;
        oldPaddingRight = right;
        oldPaddingBottom = bottom;
        isMysetPadding = true;
        setPadding(left + getBitmapWidth() / 2, top + getBitmapHeigh(), right
                + getBitmapWidth() / 2, bottom);
        isMysetPadding = false;
    }

    // 初始化
    private void init() {
        res = getResources();
        initBitmap();
        initDraw();
        setPadding();
    }


    private void initDraw() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(textsize);
        mPaint.setColor(Color.BLACK);
    }

    // 初始化padding 使其左右上 留下位置用于展示进度图片
    private void setPadding() {
        int top = getBitmapHeigh() + oldPaddingTop;
        int left = getBitmapWidth() / 2 + oldPaddingLeft;
        int right = getBitmapWidth() / 2 + oldPaddingRight;
        int bottom = oldPaddingBottom;
        isMysetPadding = true;
        setPadding(left, top, right, bottom);
        isMysetPadding = false;
    }

    /**
     * 调整进图背景图的位置 初始位置为进度条正上方、偏左一半
     *
     * @param top
     * @param left
     */
    public void setImagePadding(int top, int left) {
        this.imagepaddingleft = left;
        this.imagepaddingtop = top;
    }

    /**
     * 调整进度字体的位置 初始位置为图片的正中央
     *
     * @param top
     * @param left
     */
    public void setTextPadding(int top, int left) {
        this.textpaddingleft = left;
        this.textpaddingtop = top;
    }

    /**
     * 将气泡的宽度设置为int
     *
     * @return
     */
    private int getBitmapWidth() {
        return (int) Math.ceil(mImgWidth);
    }

    /**
     * 将气泡的高度设置为int
     *
     * @return
     */
    private int getBitmapHeigh() {
        return (int) Math.ceil(mImgHei);
    }

    private float getTextHei() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
            mText = (getProgress() * 100 / getMax()) + "%";
            if (getProgress() * 100 / getMax() != 0) {
                mTextWidth = mPaint.measureText(mText);
                Rect bounds = this.getProgressDrawable().getBounds();
                float xImg =
                        bounds.width() * getProgress() / getMax() + imagepaddingleft
                                + oldPaddingLeft;
                float yImg = imagepaddingtop + oldPaddingTop;
                float xText =
                        bounds.width() * getProgress() / getMax() + mImgWidth / 2
                                - mTextWidth / 2 + textpaddingleft + oldPaddingLeft;
                float yText =
                        yImg + textpaddingtop + mImgHei / 2 + getTextHei() / 4;
                canvas.drawBitmap(bm, xImg, yImg, mPaint);
                canvas.drawText(mText, xText, yText, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
