package com.csjbot.blackgaga.util;

import android.os.CountDownTimer;

/**
 * Created by 孙秀艳 on 2018/3/9.
 * 倒计时功能
 */

public class TimeCount extends CountDownTimer {

    MyTime mMyTime;

    /**
     * 监听内部
     */
    public interface MyTime {// 回调计时器

        public void onTick(long millisUntilFinished);

        public void onFinish();
    }

    public TimeCount(MyTime mMyTime) {
        super(60000, 1000);// 参数依次为总时长,和计时的时间间隔
        this.mMyTime = mMyTime;
    }

    public TimeCount(long millisInFuture, long countDownInterval, MyTime mMyTime) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.mMyTime = mMyTime;
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        mMyTime.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        mMyTime.onFinish();
    }

}

