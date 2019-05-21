package com.csjbot.coshandler.client_req.version;

/**
 * Created by jingwc on 2017/11/8.
 */

public interface IVersionReq {
    void getVersion();

    void softwareCheck();

    void softwareUpgrade();
}
