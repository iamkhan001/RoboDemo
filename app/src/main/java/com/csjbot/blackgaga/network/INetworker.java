package com.csjbot.blackgaga.network;

/**
 * Created by xiasuhuei321 on 2017/10/30.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public interface INetworker {
    /**
     * 在网络可用时调用
     */
    void runAvailable();

    /**
     * 在网络不可用时调用
     */
    void runUnavailable();

}
