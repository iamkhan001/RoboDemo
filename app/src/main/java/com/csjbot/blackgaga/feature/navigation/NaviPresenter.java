package com.csjbot.blackgaga.feature.navigation;

/**
 * Created by jingwc on 2017/9/21.
 */

public class NaviPresenter implements NaviContract.Presenter {

    NaviContract.View view;

    public NaviPresenter() {
    }

    @Override
    public NaviContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

}
