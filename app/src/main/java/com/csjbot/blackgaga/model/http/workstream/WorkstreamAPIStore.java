package com.csjbot.blackgaga.model.http.workstream;

import com.csjbot.blackgaga.model.http.bean.LogoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xiasuhuei321 on 2017/10/31.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public interface WorkstreamAPIStore {

    /**
     * 请求 LOGO 图标
     *
     * @param SN sn参数
     */
    @GET("csjbotservice/api/getLogoTitle/")
    Observable<LogoBean> getLogo(@Query("sn") String SN);
}
