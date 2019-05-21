package com.csjbot.blackgaga.feature.dance;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/10/12.
 */

public class DanceContract {

    interface Presenter extends BasePresenter<DanceContract.View> {
        void startDance(long time,String musicPath);
        void stopDance();
    }

    interface View extends BaseView {

    }
}
