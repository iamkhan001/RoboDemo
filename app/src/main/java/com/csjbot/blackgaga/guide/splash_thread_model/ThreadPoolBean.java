package com.csjbot.blackgaga.guide.splash_thread_model;

import com.csjbot.blackgaga.guide.splash_thread_model.base.BaseRunnable;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/17.
 */

public class ThreadPoolBean<T extends BaseRunnable> {
    private T runnable;
    private String tag;
    private boolean isOpenRun;

    public ThreadPoolBean(T runnable, String tag, boolean isOpenRun) {
        this.runnable = runnable;
        this.tag = tag;
        this.isOpenRun = isOpenRun;
    }

    public T getRunnable() {
        return runnable;
    }

    public void setRunnable(T runnable) {
        this.runnable = runnable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isOpenRun() {
        return isOpenRun;
    }

    public void setOpenRun(boolean openRun) {
        isOpenRun = openRun;
    }
}
