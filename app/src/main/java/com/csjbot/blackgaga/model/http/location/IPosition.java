package com.csjbot.blackgaga.model.http.location;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/1/30.
 */

public interface IPosition {
    void uploadPosition(String json, Observer<ResponseBody> observer);
}
