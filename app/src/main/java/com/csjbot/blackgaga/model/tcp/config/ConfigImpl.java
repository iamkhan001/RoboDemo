package com.csjbot.blackgaga.model.tcp.config;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/11/20.
 */

public class ConfigImpl extends BaseImpl implements IConfig {
    @Override
    public void setMicroVolume(int volume) {
        robotManager.robot.reqProxy.setMicroVolume(volume);
    }

    @Override
    public void getMicroVolume() {
        robotManager.robot.reqProxy.getMicroVolume();
    }
}
