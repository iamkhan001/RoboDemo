package com.csjbot.blackgaga.feature.navigation.contract;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * Created by xiasuhuei321 on 2017/12/12.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public interface NaviCommentContract {
    interface View extends BaseView{
        int GOOD = 1;
        int VERY_GOOD = 2;
        int BAD = 3;

        void well();
        void veryWell();
        void bad();

        void success();
        void failed();
    }

    interface Presenter extends BasePresenter<View>{
        void submitResult(int code);
    }
}
