package com.csjbot.blackgaga.feature.face_register;

import com.csjbot.blackgaga.core.RobotManager;

/**
 * Created by jingwc on 2017/8/15.
 */

public class FaceRegisterPresenter implements FaceRegisterContract.Presenter{

    FaceRegisterContract.View mView;
    RobotManager robotManager;

    public FaceRegisterPresenter(){
        robotManager = RobotManager.getInstance();
    }

    @Override
    public FaceRegisterContract.View getView() {
        return mView;
    }

    @Override
    public void initView(FaceRegisterContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        if(mView != null){
            mView = null;
        }
    }

    @Override
    public void faceReg(String name,String phoneNum) {
        // 人脸注册准备
//        robotManager.faceRegPre();
        // 人脸注册
//        robotManager.faceRegSave();
        // 人脸注册结束
//        robotManager.faceRegEnd();

        mView.faceRegEnd();
    }
}
