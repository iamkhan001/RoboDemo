package com.csjbot.blackgaga.feature.navigation.config;

import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/9/21.
 */

public class NaviConfigPresenter implements NaviConfigContract.Presenter{

    private NaviConfigContract.View view;

    private IChassis chassis;

    private String name;

    private List<NaviBean> navis;

    public NaviConfigPresenter(){
        chassis = ServerFactory.getChassisInstance();
        navis = new ArrayList<>();
        /* 设置位置回调事件 */
        RobotManager.getInstance().addListener(new MyOnPositionListener(this));
    }

    @Override
    public NaviConfigContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviConfigContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }

    @Override
    public void getPosition(String name) {
        chassis.getPosition();
        this.name = name;
    }

    @Override
    public void savePosition(Position pos) {
        if(navis.size() == 0){
            String json = SharedPreUtil.getString(SharedKey.NAVI_NAME,SharedKey.NAVI_KEY);
            List<NaviBean> naviList = GsonUtils.jsonToObject(json,new TypeToken<List<NaviBean>>(){}.getType());
            if(naviList != null){
                navis.addAll(naviList);
            }
        }
        NaviBean naviBean = new NaviBean();
        naviBean.setName(name);
        naviBean.setPos(pos);
        navis.add(naviBean);
        SharedPreUtil.putString(SharedKey.NAVI_NAME,SharedKey.NAVI_KEY,GsonUtils.objectToJson(navis));
        BlackgagaLogger.debug("保存成功:"+naviBean.getName());
    }

    /**
     * 此事件为获取到当前位置而触发
     * 由于此事件需要设置到RobotManager,而RobotManager是静态对象,为避免释放不掉Presenter造成内存泄露
     * 所以将Presenter对象以弱引用的方式传入
     */
    private static final class MyOnPositionListener implements OnPositionListener{

        WeakReference<NaviConfigPresenter> reference;

        public MyOnPositionListener(NaviConfigPresenter value){
            this.reference = new WeakReference<>(value);
        }

        @Override
        public void positionInfo(String json) {
            reference.get().savePosition(GsonUtils.jsonToObject(json,Position.class));
        }

        @Override
        public void moveResult(String json) {

        }

        @Override
        public void moveToResult(String json) {

        }

        @Override
        public void cancelResult(String json) {

        }
    }
}
