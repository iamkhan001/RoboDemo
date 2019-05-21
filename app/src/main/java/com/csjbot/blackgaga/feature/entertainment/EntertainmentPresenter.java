package com.csjbot.blackgaga.feature.entertainment;

/**
 * Created by jingwc on 2017/10/16.
 */

public class EntertainmentPresenter implements EntertainmentContract.Presenter {

    EntertainmentContract.View view;

    @Override
    public EntertainmentContract.View getView() {
        return view;
    }

    @Override
    public void initView(EntertainmentContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
