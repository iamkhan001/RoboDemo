package com.csjbot.coshandler.client_req.chassis;

import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * Created by jingwc on 2017/9/20.
 */

public class ChassisReqImpl extends BaseClientReq implements IChassisReq, IStatusSearch {
    @Override
    public void getPosition() {
        sendReq(getJson(REQConstants.NAVI_GET_CURPOS_REQ));
    }

    @Override
    public void move(int direction) {
        sendReq(getJson(REQConstants.NAVI_ROBOT_MOVE_REQ, "direction", direction));
    }

    @Override
    public void navi(String json) {
        sendReq(getChassisJson(REQConstants.NAVI_ROBOT_MOVE_TO_REQ, "pos", json));
    }

    @Override
    public void cancelNavi() {
        sendReq(getJson(REQConstants.NAVI_ROBOT_CANCEL_REQ));
    }

    @Override
    public void goAngle(int rotation) {
        sendReq(getJson(REQConstants.NAVI_GO_ROTATION_TO_REQ, "rotation", rotation));
    }


    @Override
    public void moveAngle(int rotation) {
        sendReq(getJson(REQConstants.NAVI_GO_ROTATION_REQ, "rotation", rotation));
    }

    @Override
    public void goHome() {
        sendReq(getJson(REQConstants.NAVI_GO_HOME_REQ));
    }

    @Override
    public void saveMap() {
        sendReq(getJson(REQConstants.NAVI_GET_MAP_REQ));
    }

    @Override
    public void loadMap() {
        sendReq(getJson(REQConstants.NAVI_SET_MAP_REQ));
    }

    @Override
    public void setSpeed(float speed) {
        sendReq(getJson(REQConstants.NAVI_ROBOT_SET_SPEED_REQ, "speed", speed));
    }

    @Override
    public void search() {
        sendReq(getJson(REQConstants.NAVI_GET_STATUS_REQ));
    }
}
