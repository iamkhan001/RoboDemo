package com.csjbot.blackgaga.cart.pactivity.introduce_list;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 */

public interface ProductContract {
    interface presenter extends BasePresenter<view>{
        int getCount();
        /**
         * load列表数据
         */
        void initListInfo();
    }

    interface view extends BaseView{
        /**
         * 网络加载错误
         */
        void error();

        void updateInfor(RobotMenuListBean robotMenuListBean);
    }
}
