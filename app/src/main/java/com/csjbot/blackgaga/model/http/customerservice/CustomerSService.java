package com.csjbot.blackgaga.model.http.customerservice;

import com.csjbot.blackgaga.model.http.bean.CustomerServiceBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jingwc on 2018/3/1.
 */

public interface CustomerSService {

    @GET("csjbotservice/api/getCustomerServiceInfo")
    Observable<CustomerServiceBean> getCustomerServiceInfo(@Query("sn") String sn);
}