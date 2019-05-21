package com.csjbot.blackgaga.model.http.qrcode;

import com.csjbot.blackgaga.model.http.bean.PayQrCodeBean;
import com.csjbot.blackgaga.model.http.bean.QrCodeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by  Wql , 2018/3/1 18:10
 */
public interface QrCodeService {
    @GET("csjbotservice/api/getQrCode")
    Observable<QrCodeBean> getQrCode(@Query("sn") String sn);

    @GET("csjbotservice/api/getPayQrCode")
    Observable<PayQrCodeBean> getPayQrCode(@Query("sn") String sn);
}
