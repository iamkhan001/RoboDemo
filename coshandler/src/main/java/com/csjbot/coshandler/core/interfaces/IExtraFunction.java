package com.csjbot.coshandler.core.interfaces;

import java.util.List;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/05/23 0023-15:05.
 * Email: puyz@csjbot.com
 */

public interface IExtraFunction {
    void getHotWords();

    void setHotWords(List<String> strings);


    /**
     * 开始人脸跟随
     */
    void startFaceFollow();

    /**
     * 关闭人脸跟随
     */
    void stopFaceFollow();
}
