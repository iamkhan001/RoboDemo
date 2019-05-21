package com.csjbot.blackgaga.feature.settings.settings_list;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by jingwc on 2017/10/20.
 */

public interface SettingsListContract {

    interface Presenter extends BasePresenter<SettingsListContract.View> {
        public void saveMap();
        public void restoreMap();
    }

    interface View extends BaseView {
        public void saveMapResult(boolean isSuccess);
        public void restoreMapResult(boolean isSuccess);
    }
}
