package com.csjbot.blackgaga.model.tcp.face;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class FaceImpl extends BaseImpl implements IFace {
    @Override
    public void openFace() {
        robotManager.robot.reqProxy.startFaceService();
    }

    @Override
    public void closeFace() {
        robotManager.robot.reqProxy.closeFaceService();
    }

    @Override
    public void faceRegPre() {
        robotManager.robot.reqProxy.prepareReg();
    }

    @Override
    public void faceRegSave(String name) {
        robotManager.robot.reqProxy.saveFace(name);
    }

    @Override
    public void faceRegEnd() {
        robotManager.robot.reqProxy.faceRegEnd();
    }

    @Override
    public void snapshot() {
        robotManager.robot.reqProxy.snapshot();
    }

    @Override
    public void faceDelList(String faceIdsJson) {
        robotManager.robot.reqProxy.faceDelList(faceIdsJson);
    }

    @Override
    public void faceInfoChanged(String faceId) {
        robotManager.robot.reqProxy.faceInfoChanged(faceId);
    }

    @Override
    public void syncFaceData() {
        robotManager.robot.reqProxy.syncFaceData();
    }
}
