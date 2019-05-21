package com.csjbot.blackgaga.util;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * Created by 孙秀艳 on 2017/10/19.
 */

/**
 * 增加点击范围
 */
public class TouchUtil {
    public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {
        ((View)view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                if (View.class.isInstance(view.getParent())) {
                    ((View)view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
