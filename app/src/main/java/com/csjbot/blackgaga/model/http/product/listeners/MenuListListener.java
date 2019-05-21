package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface MenuListListener extends BaseBackstageListener{
    void getMenuList(RobotMenuListBean bean);

    void onMenuError(Throwable e);

    void onLocaleMenuList(RobotMenuListBean bean);

    void ImageSize(int num);
}
