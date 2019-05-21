package com.csjbot.coshandler.client_req.config;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by jingwc on 2017/11/20.
 */

public class ConfigReqImpl extends BaseClientReq implements IConfigReq {

    @Override
    public void setMicroVolume(int volume) {
        sendReq(getJson(REQConstants.SET_MICRO_VOLUME_REQ,"volume",volume));
    }

    @Override
    public void getMicroVolume() {
        sendReq(getJson(REQConstants.GET_MICRO_VOLUME_REQ));
    }
}
