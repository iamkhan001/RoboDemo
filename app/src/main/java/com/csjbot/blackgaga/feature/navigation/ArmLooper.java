package com.csjbot.blackgaga.feature.navigation;

import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.TimeoutUtil;
import com.csjbot.coshandler.listener.OnArmWaveListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiasuhuei321 on 2017/11/21.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class ArmLooper {

    private int retryCount = 0;
    private TimeoutUtil startTimeOutUtil;
    private TimeoutUtil stopTimeOutUtil;

    public ArmLooper() {
        RobotManager.getInstance().addListener(listener);
    }

    public void startLooper() {
        startTimeOutUtil = new TimeoutUtil();
        RobotManager.getInstance().robot.reqProxy.startWaveHands(1500);
    }

    public void stopLooper() {
        stopTimeOutUtil = new TimeoutUtil();
        RobotManager.getInstance().robot.reqProxy.stopWaveHands();
    }

    private OnArmWaveListener listener = new OnArmWaveListener() {
        @Override
        public void start(String json) {
            try {
                JSONObject jo = new JSONObject(json);
                int error_code = jo.optInt("error_code");
                if (error_code == 0 || error_code == -1) {
                    if (startTimeOutUtil != null) {
                        startTimeOutUtil.cancel();
                    }
                }
                BlackgagaLogger.debug("开始摆手成功");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            retryCount = 0;
        }

        @Override
        public void stop(String json) {
            try {
                JSONObject jo = new JSONObject(json);
                int error_code = jo.optInt("error_code");
                if (error_code == 0 || error_code ==-1) {
                    if (stopTimeOutUtil != null) {
                        stopTimeOutUtil.cancel();
                    }
                }
                BlackgagaLogger.debug("停止摆手成功");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            retryCount = 0;
        }
    };

//    private TimeoutUtil.TimeoutListener timeOutListener = () -> {
//        // 下发摆手超时
//        if (retryCount < 3) {
////            startLooper();
//            retryCount++;
//        } else {
//            BlackgagaLogger.error("开始摆手失败");
//            retryCount = 0;
//        }
//    };

//    private TimeoutUtil.TimeoutListener stopTimeOutListener = () -> {
//        if (retryCount < 3) {
////            stopLooper();
//            retryCount++;
//            BlackgagaLogger.debug("停止摆手");
//        } else {
//            BlackgagaLogger.error("停止摆手失败");
//            retryCount = 0;
//        }
//    };
}
