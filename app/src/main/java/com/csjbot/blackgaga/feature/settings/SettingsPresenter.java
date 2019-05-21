package com.csjbot.blackgaga.feature.settings;

/**
 * Created by jingwc on 2017/10/20.
 */

public class SettingsPresenter implements SettingsContract.Presenter{

    SettingsContract.View view;

    @Override
    public SettingsContract.View getView() {
        return view;
    }

    @Override
    public void initView(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
