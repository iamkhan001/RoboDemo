package com.csjbot.blackgaga.feature.settings.synchronous_data_setting;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public interface SynchronousDataContract {
    interface presenter extends BasePresenter<view> {
        /*更新导航菜单信息*/
        void synMenu();

        /*更新商品信息*/
        void synSp();

        /*停止更新*/
        void stopUpdate();
    }

    interface view extends BaseView {
        /*导航加载完成*/
        void menuSuccess(int base);

        /*更新进度条*/
        void updatePr(int num);

        /*产品加载完成*/
        void spSuccess(int base);

        /*同步导航信息失败*/
        void menuFailed();

        /*同步产品信息失败*/
        void spFailed();

        /*内存不足*/
        void cacheError();
    }
}
