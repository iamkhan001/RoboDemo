package com.csjbot.blackgaga.model.tcp.base;

import com.csjbot.blackgaga.core.RobotManager;

/**
 * Created by jingwc on 2017/9/21.
 */

public class BaseImpl {

    protected RobotManager robotManager;

    public BaseImpl(){
        robotManager = RobotManager.getInstance();
    }
}
