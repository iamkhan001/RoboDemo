package com.csjbot.blackgaga.guide.runnable;

import com.csjbot.blackgaga.guide.splash_thread_model.base.BaseRunnable;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/19.
 * 等待的时间
 */

public class DelayRunnable extends BaseRunnable {
    @Override
    public void getRun() {
            sendMsg(this.getClass().getSimpleName());
        }
}
