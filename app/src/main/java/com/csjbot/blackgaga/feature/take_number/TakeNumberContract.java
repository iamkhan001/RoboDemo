package com.csjbot.blackgaga.feature.take_number;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/9/26.
 */

public class TakeNumberContract {
    interface Presenter extends BasePresenter<View> {
        void takeNumber();
    }

    interface View extends BaseView {
    }
}
