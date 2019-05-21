package com.csjbot.blackgaga.customer_service;

import android.support.annotation.NonNull;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/03/28 0028-12:43.
 * Email: puyz@csjbot.com
 */

public interface IComplexActionWorker {
    boolean pushJob(@NonNull Runnable job);
}
