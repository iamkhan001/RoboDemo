package com.csjbot.blackgaga.model.http.product;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.evaluate.AnswerResponse;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.feature.settings.SettingsAboutActivity;
import com.csjbot.blackgaga.model.http.bean.AdInfo;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.bean.PayResponse;
import com.csjbot.blackgaga.model.http.bean.PayResult;
import com.csjbot.blackgaga.model.http.bean.SceneBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by jingwc on 2017/9/18.
 */

public interface ProductService {
    /**
     * 获取广告信息
     */
    @GET("api/pdt/getADInfo")
    Observable<AdInfo> getAdInfo();

    /**
     * 支付订单生成请求接口
     *
     * @param body body里面是一个自己生成的Json字符串
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/pdt/addOrder")
    Observable<PayResponse> generateOrder(@Body RequestBody body);

    /**
     * 将评价传到后台
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/eva/save")
    Observable<AnswerResponse> sendEvalution(@Body RequestBody body);

    /**
     * 显示订单信息
     *
     * @param orderid
     * @return
     */
    @GET("api/pdt/showOrderInfo")
    Observable<PayResult> searchPayResult(@Query("orderid") String orderid);


    /**
     * @return
     */
    @GET("api/pdt/getRobotMenuList")
    Observable<RobotMenuListBean> getRobotMenuList(@Query("sn") String msg, @Query("language") String language);

    /**
     * @param msg 动态url
     * @param sn
     * @return
     */
    @GET("{msg}")
    Observable<RobotSpListBean> dynamicGetRobotSpList(@Path("msg") String msg, @Query("sn") String sn);

    /**
     * @param url 全动态url
     * @return
     */
    @GET
    Observable<RobotSpListBean> fullDynamicGetRobotSpList(@Url String url);

    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> getProductImage(@Url String url);

    /**
     * 利用post方法来获取到当前场景
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("csjbotservice/scene/style")
    Observable<SceneBean> getScene(@Body RequestBody body);

    /**
     * 下载皮肤包接口
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> downloadSceneSKINUrl(@Url String url);

    /**
     * 获取内容
     *
     * @param language
     * @param sn
     * @return
     */
    @GET("csjbotservice/api/getAllcontentTypes")
    Observable<ContentBean> getContent(@Query("language") String language, @Query("sn") String sn);

    /**
     * 获取子级菜单
     *
     * @param url
     * @return
     */
    @GET
    Observable<ContentTypeBean> getContentType(@Url String url);

    /**
     * 上传头像
     */
    @Multipart
    @POST("/member/uploadMemberIcon.do")
    Call<ResponseBody> upload(@Part MultipartBody.Part part, @Part("description") RequestBody destription);

    /**
     * 获取动态识别码
     */
    @POST("/appcsj/v1/robotUiUpdateDynamicCode")
    Observable<SettingsAboutActivity.Result> getCode(@Body SettingsAboutActivity.Bean bean);

    /**
     * 获取商品种类
     */
    @GET("/csjbotservice/api/getLevel")
    Observable<ClothTypeBean> getAllClothType(@Query("sn") String sn);

    /**
     * 获取衣服列表
     */
    @GET("/csjbotservice/api/getDesoninfo")
    Observable<ClothListBean> getClothingList(@Query("sn") String sn, @Query("secondLevel") String secondLevel, @Query("season") String season, @Query("minPrice") double minPrice, @Query("maxPrice") double maxPrice);
}
