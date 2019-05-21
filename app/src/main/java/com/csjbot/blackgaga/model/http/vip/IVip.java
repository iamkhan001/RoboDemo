package com.csjbot.blackgaga.model.http.vip;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/1/30.
 */

public interface IVip {
    void uploadVipInfo(String json, Observer<ResponseBody> observer);
}
