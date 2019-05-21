package com.csjbot.coshandler.client_req.custom_service;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.Locale;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/03/27 0027-10:57.
 * Email: puyz@csjbot.com
 */

public class CustomServiceImpl extends BaseClientReq implements ICustomServiceReq {
    @Override
    public void callHumanService(String sn) {
//        sendReq();
        String json = "{\"msg_id\":\"REQ_CALL_HUMAN_SERVICE\",\"sid\":%d,\"sn\":\"%s\"}";
        json = String.format(Locale.getDefault(), json, System.currentTimeMillis(), sn);
        CsjlogProxy.getInstance().debug("openCustomerService  === " + json);
        sendReq(json);
    }
}
