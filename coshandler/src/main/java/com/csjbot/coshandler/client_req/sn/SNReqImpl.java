package com.csjbot.coshandler.client_req.sn;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by xiasuhuei321 on 2017/10/23.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SNReqImpl extends BaseClientReq implements ISNReq {
    @Override
    public void getSN() {
        sendReq(getJson(REQConstants.GET_SN_REQ));
    }

    @Override
    public void getDeviceList() {
        sendReq(getJson(REQConstants.GET_LOCAL_DEVICE_REQ));
    }

    @Override
    public void setSN(String sn) {
        sendReq(getJson(REQConstants.SET_SN_REQ, "sn", sn));
    }
}
