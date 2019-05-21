package com.csjbot.blackgaga.model.http.notice;

import com.csjbot.blackgaga.model.http.bean.NoticeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jingwc on 2018/3/1.
 */

public interface NoticeService {

    @GET("csjbotservice/api/getGlobalAnnouncement")
    Observable<NoticeBean> getGlobalAnnouncement(@Query("sn") String sn);
}