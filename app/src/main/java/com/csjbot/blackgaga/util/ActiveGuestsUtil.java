package com.csjbot.blackgaga.util;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/2/6.
 * @Package_name: BlackGaGa\
 * 主动迎宾工具类
 */

public class ActiveGuestsUtil {
    private static boolean OPENID = true;

    public static ActiveGuestsUtil getInstance() {
        return util.activeGuestsUtil;
    }

    private static class util {
        public static ActiveGuestsUtil activeGuestsUtil = new ActiveGuestsUtil();
    }

    /**
     * 重新打开监听
     */
    public void reOpenThread() {
        OPENID = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMoveForward();
    }

    /**
     * 打开监听
     */
    public void sendMoveForward() {
        if (BaseApplication.getAppContext().getClass().getName().equals(NaviActivity.class.getName()) ||
                BaseApplication.getAppContext().getClass().getName().equals(NaviSettingActivity.class.getName()) ||
                BaseApplication.getAppContext().getClass().getName().equals(EntertainmentActivity.class.getName())) {
            return;
        }
        OPENID = true;
        new Thread(() -> {
            int i = 0;
            while (OPENID) {
                try {
                    Thread.sleep(1000);
                    if (i++ >= 10) {
                        OPENID = true;
                        //获取迎宾点
                        String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
                        List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
                        }.getType());
                        if (positionList == null || positionList.size() <= 0) {
                            ServerFactory.getChassisInstance().moveBack();
                            return;
                        }
                        RobotManager.getInstance().robot.reqProxy.navi(positionList.get(0).toJson());
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
