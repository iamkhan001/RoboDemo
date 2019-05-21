package com.csjbot.blackgaga.model.http.customerservice;

import com.csjbot.blackgaga.model.http.bean.CustomerServiceBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */
public interface ICustomerService {
    void getCustomerServiceInfo(String sn, Observer<CustomerServiceBean> observer);
}
