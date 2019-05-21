package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.RSPConstants;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * Created by jingwc on 2017/10/25.
 */

public class RbRobotState extends RbBase {
    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {

    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {
        switch (msgId) {
            case RSPConstants.ROBOT_GET_BATTERY_RSP:
                // TODO: 2018/05/19 0019 是否有更加好的办法
                int errorCode = getIntSingleField(dataSource, "error_code");
                if (errorCode == 0) {
                    int battery = getIntSingleField(dataSource, "battery");
                    Robot.getInstance().pushRobotState(battery);
                } else {
                    CsjlogProxy.getInstance().error(RSPConstants.ROBOT_GET_BATTERY_RSP + " error , code is " + errorCode);
                }
                break;
        }
    }
}
