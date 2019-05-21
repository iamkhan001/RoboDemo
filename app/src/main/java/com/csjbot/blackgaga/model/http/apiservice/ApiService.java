package com.csjbot.blackgaga.model.http.apiservice;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jingwc on 2018/3/29.
 */
public interface ApiService {

    @GET("csjbotservice/api/getActivity")
    Observable<ResponseBody> getSales(@Query("sn") String sn);

    @GET("csjbotservice/api/getChatAndSegmentInfo")
    Observable<ResponseBody> getChatAndSegmentInfo(@Query("sn") String sn);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/api/updateChatAndSegment")
    Observable<ResponseBody> updateChatAndSegment(@Body RequestBody body);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/api/ask")
    Observable<ResponseBody> getAnswer(@Body RequestBody body);

    @GET("csjbotservice/api/getInternationalStatus")
    Observable<ResponseBody> getInternationalStatus(@Query("sn") String sn);
}
