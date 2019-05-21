package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by  Wql , 2018/1/29 13:33
 */
public class AnomalyView extends AppCompatImageView {

    private int width = -1;
    private int height = -1;
    private Bitmap bitmap;

    public AnomalyView(Context context) {
        super(context);
    }

    public AnomalyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnomalyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (width == -1 || height == -1) {
            Drawable drawable = getBackground().getCurrent();
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            width = getWidth();
            height = getHeight();
        }
        if (null == bitmap || x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }
        int pixel = -1;
        try{
            pixel = bitmap.getPixel(x, y);
        }catch (Exception e){}
        return Color.TRANSPARENT != pixel && super.onTouchEvent(event);
    }
}
