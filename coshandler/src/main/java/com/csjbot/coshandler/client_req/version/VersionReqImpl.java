package com.csjbot.coshandler.client_req.version;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by jingwc on 2017/11/8.
 */

public class VersionReqImpl extends BaseClientReq implements IVersionReq {
    @Override
    public void getVersion() {
        sendReq(getJson(REQConstants.GET_VERSION_REQ));
    }

    @Override
    public void softwareCheck() {
        sendReq(getJson(REQConstants.UPGRADE_CHECK_REQ));
    }

    @Override
    public void softwareUpgrade() {
        sendReq(getJson(REQConstants.UPGRADE_TOTAL_REQ));
    }
}
