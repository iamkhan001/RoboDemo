package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.global.RSPConstants;

/**
 * Created by jingwc on 2017/9/20.
 */

public class RbChassis extends RbBase {

    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {
        switch (msgId) {
            case NTFConstants.NAVI_ROBOT_MOVE_TO_NTF:
                CosLogger.debug("RbChassis--->NAVI_ROBOT_MOVE_TO_NTF");
                Robot.getInstance().pushMoveResult(dataSource);
                CosLogger.debug("msgid=" + msgId + "\ndataSource=" + dataSource);
                break;
        }

    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {
        int errorCode = getIntSingleField(dataSource, "error_code");
        switch (msgId) {
            case RSPConstants.NAVI_GET_MAP_RSP:
                if (errorCode == 0) {
                    Robot.getInstance().pushSaveMap(true);
                } else {
                    Robot.getInstance().pushSaveMap(false);
                }
                break;
            case RSPConstants.NAVI_SET_MAP_RSP:
                if (errorCode == 0) {
                    Robot.getInstance().pushLoadMap(true);
                } else {
                    Robot.getInstance().pushLoadMap(false);
                }
                break;
            case RSPConstants.NAVI_GET_CURPOS_RSP:
                Robot.getInstance().pushPosition(dataSource);
                CosLogger.debug("RbChassis--->NAVI_GET_CURPOS_RSP" + dataSource);
                break;
            case RSPConstants.NAVI_GO_HOME_RSP:
                CosLogger.debug("RbChassis--->NAVI_GO_HOME_RSP");
                break;
            case RSPConstants.NAVI_ROBOT_MOVE_RSP:
                CosLogger.debug("RbChassis--->NAVI_ROBOT_MOVE_RSP");
                break;
            case RSPConstants.NAVI_ROBOT_MOVE_TO_RSP:
                Robot.getInstance().pushMoveToResult(dataSource);
                CosLogger.debug("RbChassis--->NAVI_ROBOT_MOVE_TO_RSP");
                break;
            case RSPConstants.NAVI_ROBOT_CANCEL_RSP:
                CosLogger.debug("RbChassis--->NAVI_ROBOT_CANCEL_RSP");
                Robot.getInstance().pushCancelTask(dataSource);
                break;
            case RSPConstants.NAVI_GO_ROTATION_TO_RSP:
                CosLogger.debug("RbChassis--->NAVI_GO_ROTATION_TO_RSP");
                break;
            case RSPConstants.NAVI_GO_ROTATION_RSP:
                CosLogger.debug("RbChassis--->NAVI_GO_ROTATION_RSP");
                break;
            case RSPConstants.NAVI_ROBOT_SET_SPEED_RSP:
                CosLogger.debug("RbChassis--->NAVI_ROBOT_SET_SPEED_RSP");
                Robot.getInstance().pushSetSpeed(dataSource);
                break;
            case RSPConstants.NAVI_GET_STATUS_RSP:
                CosLogger.debug("RbChassis--->NAVI_GET_STATUS_RSP\n" + dataSource);
                Robot.getInstance().pushNaviSearchResult(dataSource);
                break;
            default:
                break;
        }
    }
}
