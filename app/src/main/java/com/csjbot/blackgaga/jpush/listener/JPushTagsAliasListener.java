package com.csjbot.blackgaga.jpush.listener;

import cn.jpush.android.api.JPushMessage;

/**
 * Created by Ben on 2018/2/8.
 */

public interface JPushTagsAliasListener {

    void onTagOperatorResult(JPushMessage message);

    void onCheckTagOperatorResult(JPushMessage message);

    void onAliasOperatorResult(JPushMessage message);

    void onMobileNumberOperatorResult(JPushMessage message);
}
