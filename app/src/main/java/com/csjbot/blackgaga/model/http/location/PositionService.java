package com.csjbot.blackgaga.model.http.location;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by jingwc on 2018/1/30.
 */

public interface PositionService {


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/api/position")
    Observable<ResponseBody> uploadPosition(@Body RequestBody body);
}
