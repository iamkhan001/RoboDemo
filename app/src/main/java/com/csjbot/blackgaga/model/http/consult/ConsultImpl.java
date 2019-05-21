package com.csjbot.blackgaga.model.http.consult;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.factory.RetrofitFactory;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultImpl extends BaseImpl implements IConsult {

    @Override
    public ConsultService getRetrofit() {
        return getRetrofit(ConsultService.class);
    }
}
