package com.csjbot.blackgaga.guide.splash_thread_model.base;

import android.os.Message;

import com.csjbot.blackgaga.guide.splash_thread_model.CsjSplashPlatform;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/18.
 * runnable 任务的基类
 */

public abstract class BaseRunnable implements Runnable {
    /*是否延迟*/
    private boolean isDelay = false;

    /*延迟多少秒也就是多少次*/
    private int times = 0;

    /*延迟当前次数*/
    private int allReadyTimes = 0;

    /*延迟时间*/
    private long delayTime;
    @Override
    public void run() {
        if (times == 0) {
            getRun();
            return;
        } else if (allReadyTimes == (times - 1)) {
            getRun();
            allReadyTimes = 0;
            return;
        }
        //这里没有任务
        allReadyTimes++;
        loop();
    }

    private void loop() {
        if (isDelay) {
            CsjSplashPlatform.handler.postDelayed(this, delayTime);
        }
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setDelay(boolean delay) {
        this.isDelay = delay;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    protected void sendMsg(String msg){
        Message message = new Message();
        message.obj = msg;
        CsjSplashPlatform.sendMessage(message);
    }

    /**
     * 开启一个任务
     */
    public abstract void getRun();
}
