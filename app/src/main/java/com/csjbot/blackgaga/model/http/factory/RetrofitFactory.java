package com.csjbot.blackgaga.model.http.factory;

import com.csjbot.blackgaga.model.http.ApiUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    /* 使用自定义的OKHttpClient */
    private static OkHttpClient httpClient;

    /**
     * 初始化OKHttpClient
     */
    public static void initClient() {
        // 系统日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 设置日志级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        // 自定义OkhttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS);
        // 添加拦截器
        client.addInterceptor(loggingInterceptor);
        httpClient = client.build();
    }

    /**
     * 构建一个retrofit对象
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .baseUrl(ApiUrl.DEFAULT_ADRESS)
                .build();
        return retrofit.create(service);
    }
}
