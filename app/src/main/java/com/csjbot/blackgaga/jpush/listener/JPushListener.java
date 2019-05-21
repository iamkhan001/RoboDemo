package com.csjbot.blackgaga.jpush.listener;

import java.util.Set;

/**
 * @author Ben
 * @date 2018/2/6
 */

public interface JPushListener {

    void init();

    void stopPush();

    void resumePush();

    boolean isPushStopped();

    String getRegistrationId();

    void setTags(int sequence, Set<String> set);

    void getAllTags(int sequence);

    void setAlias(int sequence, String alias);

    void getAlias(int sequence);

}
