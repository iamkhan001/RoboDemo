package com.csjbot.blackgaga.model.http.advertisement;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.AdvBean;

import io.reactivex.Observer;

/**
 *
 * @author Ben
 * @date 2018/5/12
 */

public class AdvertisementImpl extends BaseImpl implements IAdvertisement {
    @Override
    public void getAdvertisement(String sn, String language, Observer<AdvBean> observer) {
        scheduler(getRetrofit().getAdvertisement(sn, language)).subscribe(observer);
    }

    @Override
    public AdvertisementService getRetrofit() {
        return getRetrofit(AdvertisementService.class);
    }
}
