package com.csjbot.blackgaga.model.http.advertisement;

import com.csjbot.blackgaga.model.http.bean.AdvBean;

import io.reactivex.Observer;

/**
 * @author Ben
 * @date 2018/5/12
 */

public interface IAdvertisement {

    void getAdvertisement(String sn, String language, Observer<AdvBean> observer);
}
