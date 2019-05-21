package com.csjbot.blackgaga.feature.navigation.config;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;
import com.csjbot.blackgaga.model.tcp.bean.Position;

/**
 * Created by jingwc on 2017/9/21.
 */

public class NaviConfigContract {
    interface Presenter extends BasePresenter<NaviConfigContract.View> {
        /* 获取当前位置 */
        void getPosition(String name);
        /* 保存当前位置 */
        void savePosition(Position pos);
    }

    interface View extends BaseView {

    }
}
