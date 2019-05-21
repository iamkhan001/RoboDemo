package com.csjbot.blackgaga.model.http.vip;

import com.csjbot.blackgaga.model.http.base.BaseImpl;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/1/30.
 */

public class VipImpl extends BaseImpl implements IVip {
    @Override
    public void uploadVipInfo(String json, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().uploadVipInfo(getBody(json))).subscribe(observer);
    }

    @Override
    public VipService getRetrofit() {
        return getRetrofit(VipService.class);
    }
}
