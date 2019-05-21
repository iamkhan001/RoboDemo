package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.global.RSPConstants;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * Created by jingwc on 2017/11/21.
 */

public class RbOther extends RbBase {
    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {
        switch (msgId) {
            case NTFConstants.ROBOT_BODY_HEAD_TOUCH_NTF:
                Robot.getInstance().pushHeadTouch();
                break;
            case NTFConstants.ROBOT_CHARGE_STATE_NTF:
                Robot.getInstance().pushChargeState(getIntSingleField(dataSource, "charge_state"));
                break;
            case NTFConstants.DEVICE_DETECT_PERSON_NEAR_NTF:
                Robot.getInstance().pushDetectPerson(getIntSingleField(dataSource, "state"));
                break;
            case NTFConstants.ROBOT_COMPLEX_ACTION_NTF:
            case NTFConstants.ROBOT_SET_VOLUME_NTF:
                Robot.getInstance().pushCustomServiceMsg(dataSource);
                break;
            case NTFConstants.ROBOT_SHUTDOWN_NTF:
                Robot.getInstance().pushShutdown();
                break;
            default:
                CsjlogProxy.getInstance().debug("RbOther default [" + msgId + "]");
                break;
        }
    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {
        switch (msgId) {
            case RSPConstants.GET_MICRO_VOLUME_RSP:
                Robot.getInstance().pushMicrolVolume(getIntSingleField(dataSource, "volume"));
                break;
            case RSPConstants.CUSTSERVICE_GET_RESULT_RSP:
                Robot.getInstance().pushSpeechGetResult(dataSource);
                break;
            case RSPConstants.WARNING_CHECK_SELF_RSP:
                Robot.getInstance().pushCheckSelf(dataSource);
                break;
            case RSPConstants.GET_HARDWARE_INFO_RSP:
                Robot.getInstance().setHardWareVersion(getSingleField(dataSource, "version"));
                break;
            case RSPConstants.GET_ROBOT_TYPE_RSP:
                Robot.getInstance().pushLinuxRobotType(getSingleField(dataSource, "type"));
            default:
                break;
        }
    }
}
