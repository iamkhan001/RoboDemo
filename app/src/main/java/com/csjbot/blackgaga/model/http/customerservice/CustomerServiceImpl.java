package com.csjbot.blackgaga.model.http.customerservice;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.CustomerServiceBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */

public class CustomerServiceImpl extends BaseImpl implements ICustomerService {

    @Override
    public CustomerSService getRetrofit() {
        return getRetrofit(CustomerSService.class);
    }

    @Override
    public void getCustomerServiceInfo(String sn, Observer<CustomerServiceBean> observer) {
        scheduler(getRetrofit().getCustomerServiceInfo(sn)).subscribe(observer);
    }
}
