package com.csjbot.blackgaga.feature.navigation.map;


import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviMapPresenter implements NaviMapContract.Presenter {

    NaviMapContract.View view;

    IChassis chassis;

    public NaviMapPresenter(){
        chassis = ServerFactory.getChassisInstance();
    }

    @Override
    public NaviMapContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviMapContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }

    @Override
    public void goNavi(String json) {
        chassis.navi(json);
    }
}
