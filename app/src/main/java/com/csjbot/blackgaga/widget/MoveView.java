package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;


/**
 * Created by jingwc on 2017/9/22.
 */

public class MoveView extends TextView {
    private int top = 200;
    private int left  = 200;

    public MoveView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.map_2maker);
    }

    public MoveView(Context context, int top, int left) {
        super(context);
        setBackgroundResource(R.drawable.map_2maker);
        this.top = top;
        this.left = left;
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            float x = (event.getRawX()-(getMeasuredWidth()>>1) - top);
            float y = (event.getRawY()-(getMeasuredHeight()>>1) - left);
            if (x>=900) {
                x=900;
            }
            if (y>=450) {
                y=450;
            }
            if (x<=0) {
                x=0;
            }
            if (y<=0) {
                y=0;
            }
            this.setTranslationX(x);
            this.setTranslationY(y);
        }
        return super.onTouchEvent(event);
    }

}
