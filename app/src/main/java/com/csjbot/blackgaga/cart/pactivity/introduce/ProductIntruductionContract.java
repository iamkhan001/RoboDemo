package com.csjbot.blackgaga.cart.pactivity.introduce;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 */

public interface ProductIntruductionContract {
    interface presenter extends BasePresenter<view>{
        int getCount();

        boolean addCartCount(RobotSpListBean.ResultBean.ProductBean bean);

        void updateSp(String menuid);
    }

    interface view extends BaseView{
        void updateInfor(RobotSpListBean product);
    }
}
