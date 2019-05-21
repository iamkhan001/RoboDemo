package com.csjbot.blackgaga.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class RetrofitUtil {

    private static OkHttpClient okHttpClient;

    public static void init() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 设置日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 自定义OkhttpClient
        OkHttpClient.Builder ok = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS);
        // 添加拦截器
        ok.addInterceptor(httpLoggingInterceptor);
        okHttpClient = ok.build();
    }

    public static Retrofit.Builder get() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);
    }
}
