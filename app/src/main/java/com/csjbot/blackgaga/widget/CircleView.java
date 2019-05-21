package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形图片
 * Created by jingwc on 2017/12/15.
 */

public class CircleView extends ImageView {

    Paint paint;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取当前设置的drawable
        Drawable drawable = getDrawable();

        if(drawable == null){
            return;
        }
        // drawable 转 bitmap
        Bitmap bitmap = drawableToBitmap(getDrawable());

        if(bitmap == null){
            return;
        }
        // 创建画笔,设置抗锯齿
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 创建bitmap着色器
        Shader shader = new BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);

        paint.setShader(shader);

        int cx = getWidth()/2;
        int cy = getHeight()/2;
        int radius = Math.min(cx,cy);

        canvas.drawCircle(cx,cy,radius,paint);

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
}
