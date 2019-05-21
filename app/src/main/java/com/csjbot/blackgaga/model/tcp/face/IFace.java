package com.csjbot.blackgaga.model.tcp.face;

/**
 * Created by jingwc on 2017/9/5.
 */

public interface IFace {

    /**
     * 打开人脸识别服务
     */
    void openFace();

    /**
     * 关闭人脸识别服务
     */
    void closeFace();

    /**
     * 人脸注册准备
     */
    void faceRegPre();

    /**
     * 人脸注册拍照及保存
     */
    void faceRegSave(String name);


    /**
     * 人脸注册结束
     */
    void faceRegEnd();

    /**
     * 摄像头拍照
     */
    void snapshot();

    /**
     * 人脸信息删除json
     * @param faceIdsJson
     */
    void faceDelList(String faceIdsJson);

    /**
     * 人脸信息变更
     * @param faceId
     */
    void faceInfoChanged(String faceId);


    /**
     * 人脸信息同步
     */
    void syncFaceData();

}
