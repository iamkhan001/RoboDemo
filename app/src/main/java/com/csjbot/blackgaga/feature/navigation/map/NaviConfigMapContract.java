package com.csjbot.blackgaga.feature.navigation.map;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviConfigMapContract {
    interface Presenter extends BasePresenter<View> {
    }

    interface View extends BaseView {
        void showNaviMap();
    }
}
