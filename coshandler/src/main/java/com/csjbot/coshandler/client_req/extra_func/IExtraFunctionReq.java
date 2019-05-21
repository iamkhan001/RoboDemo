package com.csjbot.coshandler.client_req.extra_func;

import java.util.List;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/05/23 0023-15:22.
 * Email: puyz@csjbot.com
 */

public interface IExtraFunctionReq {
    void getHotWords();

    void setHotWords(List<String> hotwords);


    void startFaceFollow();

    void stopFaceFollow();
}
