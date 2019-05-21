package com.csjbot.blackgaga.util;

import android.os.CountDownTimer;

/**
 * Created by xiasuhuei321 on 2017/11/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class TimeoutUtil {
    private CountDownTimer timer;

    /**
     * 不要 start 多次，这样无法控制，使用这个类请务必保证一次 start 一次 cancel，
     * 或者一次 start 一次 timeout
     */
    public void start(long lastTime, TimeoutListener listener) {
        timer = new CountDownTimer(lastTime, lastTime) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.timeOut();
                }
            }
        };

        timer.start();
    }

    public void cancel() {
        if (timer != null)
            timer.cancel();
    }

    public interface TimeoutListener {
        void timeOut();
    }

}
