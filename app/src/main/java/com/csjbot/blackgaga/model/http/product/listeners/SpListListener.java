package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.cart.entity.RobotSpListBean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface SpListListener extends BaseBackstageListener{
    void getSpList(RobotSpListBean bean);

    void onSpError(Throwable e);

    void onLocaleSpList(RobotSpListBean bean);

    void ImageSize(int num);
}
