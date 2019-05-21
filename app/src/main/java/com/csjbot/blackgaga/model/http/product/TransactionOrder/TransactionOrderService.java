package com.csjbot.blackgaga.model.http.product.TransactionOrder;

import com.csjbot.blackgaga.model.http.bean.OrderBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by 孙秀艳 on 2017/10/23.
 */

public interface TransactionOrderService {
    /**
     * 获取产品信息
     */
    @GET("api/pdt/getRobotProductInfo")
    Observable<OrderBean> getProductDetailInfo();
}
