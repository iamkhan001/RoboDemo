package com.csjbot.blackgaga.model.http.location;

import com.csjbot.blackgaga.model.http.base.BaseImpl;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/1/30.
 */

public class PositionImpl extends BaseImpl implements IPosition {
    @Override
    public void uploadPosition(String json, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().uploadPosition(getBody(json))).subscribe(observer);
    }

    @Override
    public PositionService getRetrofit() {
        return getRetrofit(PositionService.class);
    }
}
