package com.csjbot.blackgaga.feature.navigation.map;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviConfigMapPresenter implements NaviConfigMapContract.Presenter {

    NaviConfigMapContract.View view;

    @Override
    public NaviConfigMapContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviConfigMapContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
