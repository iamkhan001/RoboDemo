package com.csjbot.blackgaga.model.http.advertisement;

import com.csjbot.blackgaga.model.http.bean.AdvBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Ben
 * @date 2018/5/12
 */

public interface AdvertisementService {
    /**
     * 获取媒体数据
     * http://dev.csjbot.com:8080/mealservice/api/getAdvinfo?sn=12345678&language=zh_CN
     *
     * @param sn  机器人sn
     * @param language 语言
     * @return
     */
    @GET("csjbotservice/api/getAdvinfo")
    Observable<AdvBean> getAdvertisement(@Query("sn") String sn, @Query("language") String language);
}
