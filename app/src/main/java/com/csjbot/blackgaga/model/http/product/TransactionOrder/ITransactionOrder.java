package com.csjbot.blackgaga.model.http.product.TransactionOrder;

import com.csjbot.blackgaga.model.http.bean.OrderBean;

import io.reactivex.Observer;

/**
 * Created by 孙秀艳 on 2017/10/23.
 */

public interface ITransactionOrder {

    /**
     * 获取产品明细
     * @param observer 订阅者
     */
    void getProductDetailInfo(Observer<OrderBean> observer);
}
