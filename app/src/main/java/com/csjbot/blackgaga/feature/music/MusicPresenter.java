package com.csjbot.blackgaga.feature.music;

/**
 * Created by jingwc on 2017/10/16.
 */

public class MusicPresenter implements MusicContract.Presenter {

    MusicContract.View view;

    @Override
    public MusicContract.View getView() {
        return view;
    }

    @Override
    public void initView(MusicContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
