package com.csjbot.blackgaga.feature.settings.settings_list;

import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.coshandler.listener.OnMapListener;

/**
 * Created by jingwc on 2017/10/20.
 */

public class SettingsListPresenter implements SettingsListContract.Presenter{

    SettingsListContract.View view;
    private IChassis chassis;
    private OnMapListener listener;

    @Override
    public SettingsListContract.View getView() {
        return view;
    }

    @Override
    public void initView(SettingsListContract.View view) {
        this.view = view;
        chassis = ServerFactory.getChassisInstance();
        listener = new OnMapListener() {
            @Override
            public void saveMap(boolean state) {
                view.saveMapResult(state);
            }

            @Override
            public void loadMap(boolean state) {
                view.restoreMapResult(state);
            }
        };
        RobotManager.getInstance().addListener(listener);
    }

    @Override
    public void releaseView() {
        listener = null;
        if(view != null){
            view = null;
        }
    }

    @Override
    public void saveMap() {
        chassis.saveMap();
    }

    @Override
    public void restoreMap() {
        chassis.loadMap();
    }
}
