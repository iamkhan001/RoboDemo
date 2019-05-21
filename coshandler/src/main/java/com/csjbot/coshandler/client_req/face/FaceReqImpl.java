package com.csjbot.coshandler.client_req.face;


import com.csjbot.coshandler.client_req.base.BaseClientReq;
import com.csjbot.coshandler.global.REQConstants;

/**
 * 人脸识别实现类
 * Created by jingwc on 2017/8/14.
 */

public class FaceReqImpl extends BaseClientReq implements IFaceReq {

    @Override
    public void openVideo() {
        sendReq(getJson(REQConstants.FACE_DETECT_OPEN_VIDEO_REQ));
    }

    @Override
    public void closeVideo() {
        sendReq(getJson(REQConstants.FACE_DETECT_CLOSE_VIDEO_REQ));
    }

    @Override
    public void startFaceService() {
        sendReq(getJson(REQConstants.FACE_DETECT_SERVICE_START_REQ));
    }

    @Override
    public void closeFaceService() {
        sendReq(getJson(REQConstants.FACE_DETECT_SERVICE_STOP_REQ));
    }

    @Override
    public void prepareReg() {
        sendReq(getJson(REQConstants.FACE_REG_START_REQ));
    }

    @Override
    public void faceRegEnd() {
        sendReq(getJson(REQConstants.FACE_REG_STOP_REQ));
    }

    @Override
    public void snapshot() {
        sendReq(getJson(REQConstants.FACE_SNAPSHOT_REQ));
    }

    @Override
    public void saveFace(String name) {
        sendReq(getJson(REQConstants.FACE_SAVE_REQ,"name",name));
    }

    @Override
    public void faceDel(int faceId) {
        sendReq(getJson(REQConstants.FACE_DATA_DEL_REQ,"person_id",faceId));
    }

    @Override
    public void faceDelList(String faceIdsJson) {
        sendReq(getJsonFromJsonContent(REQConstants.FACE_DATA_DELETE_LIST_REQ,"person_id",faceIdsJson));
    }

    @Override
    public void faceInfoChanged(String faceId) {
        sendReq(getJson(REQConstants.FACE_DATA_CHANGED_REQ,"person_id",faceId));
    }

    @Override
    public void getFaceDatabase() {
        sendReq(getJson(REQConstants.FACE_DATABASE_REQ));
    }

    @Override
    public void syncFaceData() {
        sendReq(getJson(REQConstants.FACE_SYNC_UNDO_REG_REQ));
    }
}
