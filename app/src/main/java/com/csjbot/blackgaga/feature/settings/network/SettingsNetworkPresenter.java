package com.csjbot.blackgaga.feature.settings.network;

/**
 * Created by jingwc on 2017/10/20.
 */

public class SettingsNetworkPresenter implements SettingsNetworkContract.Presenter{

    SettingsNetworkContract.View view;

    @Override
    public SettingsNetworkContract.View getView() {
        return view;
    }

    @Override
    public void initView(SettingsNetworkContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }
}
