package com.csjbot.coshandler.client_req.face;


import com.csjbot.coshandler.client_req.base.IClientReq;

/**
 * 人脸识别接口
 * Created by jingwc on 2017/8/14.
 */

public interface IFaceReq extends IClientReq {

    /**
     * 打开摄像头
     */
    void openVideo();

    /**
     * 关闭摄像头
     */
    void closeVideo();

    /**
     * 启动人脸识别服务
     */
    void startFaceService();

    /**
     * 关闭人脸识别服务
     */
    void closeFaceService();

    /**
     * 人脸注册准备
     */
    void prepareReg();

    /**
     * 人脸注册结束
     */
    void faceRegEnd();

    /**
     * 摄像头拍照
     */
    void snapshot();

    /**
     * 人脸注册
     */
    void saveFace(String name);

    /**
     * 人脸信息删除
     * @param faceId
     */
    void faceDel(int faceId);

    /**
     * 人脸信息批量删除
     * @param faceIdsJson
     */
    void faceDelList(String faceIdsJson);

    /**
     * 人脸信息变更
     * @param faceId
     */
    void faceInfoChanged(String faceId);

    /**
     * 获取人脸数据库
     */
    void getFaceDatabase();

    /**
     * 人脸信息同步
     */
    void syncFaceData();
}
