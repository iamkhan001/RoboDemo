package com.csjbot.blackgaga.feature.face_register;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/8/15.
 */

public class FaceRegisterContract {
    interface Presenter extends BasePresenter<View>{
        void faceReg(String name,String phoneNum);
    }

    interface View extends BaseView{
        void init();
        void faceRegEnd();
    }

}
