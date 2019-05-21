package com.csjbot.blackgaga.cart.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.util.BlackgagaLogger;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/19.
 */

public class CartImageAnmotionTool<T extends ViewGroup> implements Animator.AnimatorListener {
    /**
     * 添加购物车添加完成的监听
     */
    private AddListener listener;

    private Context context;

    private T start;

    private T destination;

    public CartImageAnmotionTool(Context ctx) {
        context = ctx;
    }

    /**
     * @param addV        被点击view
     * @param start       父布局
     * @param destination 购物车布局
     */
    public CartImageAnmotionTool addAnmotion(View addV, T start, T destination, Drawable drawable) {
        int[] childCoordinate = new int[2];
        int[] parentCoordinate = new int[2];
        int[] shopCoordinate = new int[2];
        this.start = start;
        this.destination = destination;
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        addV.getLocationInWindow(childCoordinate);
        start.getLocationInWindow(parentCoordinate);
        destination.getLocationInWindow(shopCoordinate);
        //2.自己定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(context);
        //drawable.setBounds(0, 0, 326, 322);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        img.setImageDrawable(drawable);

        //3.设置img在父布局中的坐标位置
        img.setX(childCoordinate[0] - parentCoordinate[0]);
        img.setY(childCoordinate[1] - parentCoordinate[1]);
        //4.父布局加入该Img
        start.addView(img);
        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        //開始的数据点坐标就是 addV的坐标
        startP.x = childCoordinate[0] - parentCoordinate[0];
        startP.y = childCoordinate[1] - parentCoordinate[1];
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = (float) (shopCoordinate[0] - (destination.getWidth() * 0.7));
        endP.y = -(shopCoordinate[1] + (destination.getHeight() / 2));
        BlackgagaLogger.debug("chenqi y = " + destination.getWidth() + "y" + endP.y);
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = childCoordinate[0];
        controlP.y = childCoordinate[1];
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF", new PointFTypeEvaluator(controlP), startP, endP);//线程
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(img, "scaleX", 1f, 0.05f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(img, "scaleY", 1f, 0.05f);
        ObjectAnimator icon_anim = ObjectAnimator.ofFloat(img, "rotation", 0.0F, 359.0F);//设置Y轴的立体旋转动
        animator.setDuration(1000);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        icon_anim.setDuration(1000);
        animatorSet.play(animator).with(icon_anim).with(scaleX).with(scaleY);//几个动画按照顺序同时开始
        animator.addListener(this);
        animatorSet.start();
        return this;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //动画结束后 父布局移除 img
        Object target = ((ObjectAnimator) animation).getTarget();
        start.removeView((View) target);
        //shopImg 開始一个放大动画
        Animation scaleAnim = AnimationUtils.loadAnimation(context, R.anim.shop_scale);
        destination.startAnimation(scaleAnim);
        if (listener != null) {
            listener.addCart();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    /**
     * 自己定义估值器
     */
    public class PointFTypeEvaluator implements TypeEvaluator<PointF> {
        /**
         * 每一个估值器相应一个属性动画。每一个属性动画仅相应唯一一个控制点
         */
        PointF control;
        /**
         * 估值器返回值
         */
        PointF mPointF = new PointF();

        public PointFTypeEvaluator(PointF control) {
            this.control = control;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return getBezierPoint(startValue, endValue, control, fraction);
        }

        /**
         * 二次贝塞尔曲线公式
         *
         * @param start   開始的数据点
         * @param end     结束的数据点
         * @param control 控制点
         * @param t       float 0-1
         * @return 不同t相应的PointF
         */
        private PointF getBezierPoint(PointF start, PointF end, PointF control, float t) {
            mPointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x;
            mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y;
            return mPointF;
        }
    }

    public void setListener(AddListener listener) {
        this.listener = listener;
    }

    public interface AddListener {
        void addCart();
    }
}
