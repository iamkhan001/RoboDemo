package com.csjbot.blackgaga.cart.widget;

import android.content.Context;
import android.graphics.PointF;
import android.widget.ImageView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/17.
 */

public class MoveImageView extends ImageView {
    public MoveImageView(Context context) {
        super(context);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }
}
