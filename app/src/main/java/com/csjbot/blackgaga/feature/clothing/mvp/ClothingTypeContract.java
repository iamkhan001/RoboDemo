package com.csjbot.blackgaga.feature.clothing.mvp;

import android.support.v7.widget.RecyclerView;

/**
 * @author ShenBen
 * @date 2018/11/13 9:50
 * @email 714081644@qq.com
 */

public interface ClothingTypeContract {
    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRvCloth();

        void speakMessage(String message);

        void isNoData(boolean isNodata);
    }

    interface Presenter {

        void initData();

        void previousPage();

        void nextPage();
    }
}
