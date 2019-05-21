package com.csjbot.blackgaga.feature.consult;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultContract {

    interface Presenter extends BasePresenter<ConsultContract.View> {
        void getConsult();
    }

    interface View extends BaseView {
        void showConsult();
    }
}
