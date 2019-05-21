package com.csjbot.blackgaga.guide;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/13.
 */

public interface GuideContract {
    interface presenter extends BasePresenter<view> {
        /*跳过splash页面的线程*/
        void startTh();

        /*某个监听需要延时*/
        void delayTh(GuidePresenter.Type type);

        /*清除线程*/
        void releaseHandler();

        /*停止跳过splash页面*/
        void stopDown();

        /*开始数据加载*/
        void actionSpLoad();

        /*停止延时*/
        void stopDelay();
    }

    interface view extends BaseView {
        /*超时提醒*/
        void timeOut(GuidePresenter.Type type);

        /*跳转页面*/
        void showButton();

        /*所有项都加载完毕退出splash页面*/
        void shutDown();
    }
}
