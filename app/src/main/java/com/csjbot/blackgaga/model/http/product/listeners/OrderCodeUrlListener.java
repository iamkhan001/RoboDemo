package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.model.http.bean.PayResponse;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface OrderCodeUrlListener {
    void getOrderCodeUrl(PayResponse payResponse);

    void onOrderCodeError();

    void onOrderCodeComplete();
}
