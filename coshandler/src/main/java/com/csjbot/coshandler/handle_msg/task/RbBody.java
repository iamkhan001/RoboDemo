package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.RSPConstants;

/**
 * Created by jingwc on 2017/9/20.
 */

public class RbBody extends RbBase {
    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {
        CosLogger.debug("handleNTFMessage-->" + msgId);
    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {
        CosLogger.debug("handleRSPMessage-->" + msgId);

        if(msgId.equals(RSPConstants.ROBOT_ARM_LOOP_START_RSP)){
            Robot.getInstance().pushArmWaveResult(dataSource);
        }

        if(msgId.equals(RSPConstants.ROBOT_ARM_LOOP_STOP_RSP)){
            Robot.getInstance().pushArmStopResult(dataSource);
        }
    }
}
