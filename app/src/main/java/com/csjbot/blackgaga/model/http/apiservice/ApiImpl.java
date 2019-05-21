package com.csjbot.blackgaga.model.http.apiservice;

import com.csjbot.blackgaga.model.http.base.BaseImpl;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/3/29.
 */

public class ApiImpl extends BaseImpl implements IApi{

    @Override
    public void getSales(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getSales(sn)).subscribe(observer);
    }

    @Override
    public void getChatAndSegmentInfo(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getChatAndSegmentInfo(sn)).subscribe(observer);
    }

    @Override
    public void updateChatAndSegment(String body, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().updateChatAndSegment(getBody(body))).subscribe(observer);
    }

    @Override
    public void getAnswer(String body, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getAnswer(getBody(body))).subscribe(observer);
    }

    @Override
    public void getInternationalStatus(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getInternationalStatus(sn)).subscribe(observer);
    }

    @Override
    public ApiService getRetrofit() {
        return getRetrofit(ApiService.class);
    }
}
