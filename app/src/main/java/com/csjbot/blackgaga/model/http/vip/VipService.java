package com.csjbot.blackgaga.model.http.vip;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by jingwc on 2018/1/30.
 */

public interface VipService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/api/updateVipInfo")
    Observable<ResponseBody> uploadVipInfo(@Body RequestBody body);
}
