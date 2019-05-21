package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jingwc on 2017/10/27.
 */

public class SlideProgressView extends View {

    public SlideProgressView(Context context) {
        super(context);
    }

    public SlideProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float lineStartX = 300;
    float lineStartY = 50;
    float lineEndX = 1300;
    float lineEndY = 50;
    float circleX = 300;
    float circleInitX = 300;
    float circleY = 50;
    float radus = 30;
    float downX = 0;
    int value;
    boolean isMove = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
        paint.setColor(Color.GRAY);
        canvas.drawLine(lineStartX,lineStartY,lineEndX,lineEndY,paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(lineStartX,lineStartY,circleX,lineEndY,paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(circleX,circleY,radus,paint);


        paint.reset();
        paint.setAntiAlias(true);
        paint.setTextSize(32);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(value),lineEndX+50,lineStartY+15,paint);

        paint.setTextSize(18);
        paint.setColor(Color.GRAY);
        canvas.drawText("0",lineStartX,lineStartY+50,paint);
        canvas.drawText("50",(lineStartX+((lineEndX-lineStartX)/2)),lineStartY+50,paint);
        canvas.drawText("100",lineEndX,lineStartY+50,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                if(event.getX() >(circleX-radus) && event.getX() < (circleX+radus*2) && event.getY() > circleY && event.getY() < (circleY+radus)){
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isMove){
                    break;
                }
                if(event.getX()> downX){
                    if((circleX += (event.getX() - circleX)) < lineEndX){

                    }else{
                        circleX = lineEndX;
                    }
                }else{
                    if((circleX += (event.getX()-circleX)) > circleInitX){

                    }else{
                        circleX = circleInitX;
                    }
                }
                invalidate();
                value = (int)(circleX - circleInitX)/10;
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                downX = 0;
                break;
        }
        return true;
    }

    public int getValue(){
        if(value >= 0 && value <= 100){
            return value;
        }
        return 0;
    }

    public void setValue(int value){
        this.value = value;
        circleX = value*10+circleInitX;
        invalidate();
    }
}
