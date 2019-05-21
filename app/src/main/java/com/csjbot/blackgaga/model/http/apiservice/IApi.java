package com.csjbot.blackgaga.model.http.apiservice;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/3/29.
 */

public interface IApi {
    void getSales(String sn, Observer<ResponseBody> observer);

    void getChatAndSegmentInfo(String sn, Observer<ResponseBody> observer);

    void updateChatAndSegment(String body, Observer<ResponseBody> observer);

    void getAnswer(String body, Observer<ResponseBody> observer);

    void getInternationalStatus(String sn, Observer<ResponseBody> observer);
}
