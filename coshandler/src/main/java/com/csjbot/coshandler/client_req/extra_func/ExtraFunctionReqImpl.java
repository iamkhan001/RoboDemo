package com.csjbot.coshandler.client_req.extra_func;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

import java.util.List;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/05/23 0023-15:25.
 * Email: puyz@csjbot.com
 */

public class ExtraFunctionReqImpl extends BaseClientReq implements IExtraFunctionReq {
    @Override
    public void getHotWords() {
        sendReq(getJson(REQConstants.SPEECH_GET_USERWORDS_REQ));
    }

    @Override
    public void setHotWords(List<String> hotwords) {
        sendReq(getHotWordJson(REQConstants.SPEECH_SET_USERWORDS_REQ, hotwords));
    }

    @Override
    public void startFaceFollow() {
        sendReq(getJson(REQConstants.FACE_FOLLOW_START_REQ));
    }

    @Override
    public void stopFaceFollow() {
        sendReq(getJson(REQConstants.FACE_FOLLOW_CLOSE_REQ));
    }
}
