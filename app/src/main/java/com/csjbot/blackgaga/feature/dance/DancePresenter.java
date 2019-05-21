package com.csjbot.blackgaga.feature.dance;

import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;

/**
 * Created by jingwc on 2017/10/12.
 */

public class DancePresenter implements DanceContract.Presenter{

    DanceContract.View view;

    @Override
    public DanceContract.View getView() {
        return view;
    }

    @Override
    public void initView(DanceContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }

    @Override
    public void startDance(long time,String musicPath) {
//        ServerFactory.getDanceInstatnce().dance(time,musicPath);
    }

    @Override
    public void stopDance() {
        ServerFactory.getDanceInstatnce().stopDance();
    }
}
