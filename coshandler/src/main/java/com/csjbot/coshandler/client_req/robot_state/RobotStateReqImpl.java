package com.csjbot.coshandler.client_req.robot_state;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.CmdConstants;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by jingwc on 2017/10/25.
 */

public class RobotStateReqImpl extends BaseClientReq implements IRobotStateReq {
    @Override
    public void shutdown() {
        sendReq(getJson(REQConstants.ROBOT_SHUTDOWN_REQ));
    }

    @Override
    public void reboot() {
        sendReq(getJson(REQConstants.ROBOT_REBOOT_REQ));
    }

    @Override
    public void getBattery() {
        sendReq(getJson(REQConstants.ROBOT_GET_BATTERY_REQ));
    }

    @Override
    public void checkSelf() {
        sendReq(getJson(REQConstants.WARNING_CHECK_SELF_REQ));
    }

    /**
     * 获取硬件版本
     */
    @Override
    public void getRobotHWVersion() {
        sendReq(getJson(REQConstants.GET_HARDWARE_INFO_REQ));
    }

    /**
     * 获取Linux储存的机器人硬件类型，默认是迎宾
     */
    @Override
    public void getLinuxRobotType() {
        sendReq(getJson(REQConstants.GET_ROBOT_TYPE_REQ));
    }

    /**
     * 设置Linux储存的机器人硬件类型，默认是迎宾
     *
     * @param type
     */
    @Override
    public void setLinuxRobotType(String type) {
        sendReq(getJson(CmdConstants.SET_ROBOT_TYPE_CMD, "type", type));
    }
}
