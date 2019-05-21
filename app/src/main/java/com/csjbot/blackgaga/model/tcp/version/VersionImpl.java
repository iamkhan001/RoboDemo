package com.csjbot.blackgaga.model.tcp.version;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/11/8.
 */

public class VersionImpl extends BaseImpl implements IVersion {
    @Override
    public void getVersion() {
        robotManager.robot.reqProxy.getVersion();
    }

    @Override
    public void softwareCheck() {
        robotManager.robot.reqProxy.softwareCheck();
    }

    @Override
    public void softwareUpgrade() {
        robotManager.robot.reqProxy.softwareUpgrade();
    }
}
