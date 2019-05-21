package com.csjbot.blackgaga.core;

import android.content.Context;
import android.content.Intent;

import com.csjbot.blackgaga.manual_control.service.ServerService;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnArmWaveListener;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnChargetStateListener;
import com.csjbot.coshandler.listener.OnChassisListener;
import com.csjbot.coshandler.listener.OnCustomServiceMsgListener;
import com.csjbot.coshandler.listener.OnDetectPersonListener;
import com.csjbot.coshandler.listener.OnDeviceInfoListener;
import com.csjbot.coshandler.listener.OnExpressionListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnFaceSaveListener;
import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.coshandler.listener.OnHeadTouchListener;
import com.csjbot.coshandler.listener.OnHotWordsListener;
import com.csjbot.coshandler.listener.OnInitListener;
import com.csjbot.coshandler.listener.OnLinuxRobotTypeListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnMicroVolumeListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.listener.OnRobotStateListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnSetSNListener;
import com.csjbot.coshandler.listener.OnShutdownListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;
import com.csjbot.coshandler.listener.OnSpeechErrorListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.listener.OnSpeechListener;
import com.csjbot.coshandler.listener.OnSpeedSetListener;
import com.csjbot.coshandler.listener.OnSyncFaceListener;
import com.csjbot.coshandler.listener.OnUpgradeListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.listener.OnWarningCheckSelfListener;
import com.csjbot.coshandler.service.CameraService;
import com.csjbot.coshandler.service.HandlerMsgService;

/**
 * 提供所有底层通信的基础api操作
 * Created by jingwc on 2017/9/5.
 */

public class RobotManager {

    private volatile static RobotManager robotManager;

    public Robot robot;

    public static RobotManager getInstance() {
        if (robotManager == null) {
            synchronized (RobotManager.class) {
                if (robotManager == null) {
                    robotManager = new RobotManager();
                }
            }
        }
        return robotManager;
    }

    private RobotManager() {
        if (robot == null) {
            robot = Robot.getInstance();
        }
    }

    public void addListener(OnInitListener initListener) {
        robot.setInitListener(initListener);
    }

    public void addListener(OnExpressionListener expressionListener) {
        robot.setExpressionListener(expressionListener);
    }

    public void addListener(OnSpeechListener speechListener) {
        robot.setSpeechListener(speechListener);
    }

    public void addListener(OnFaceListener faceListener) {
        robot.setFaceListener(faceListener);
    }

    public void addListener(OnCameraListener cameraListener) {
        robot.setCameraListener(cameraListener);
    }

    public void addListener(OnChassisListener chassisListener) {
        robot.setChassisListener(chassisListener);
    }

    public void addListener(OnPositionListener positionListener) {
        robot.setPositionListener(positionListener);
    }

    public void addPositionListener(OnPositionListener positionListener) {
        robot.registerPositionListener(positionListener);
    }

    public void removePositionListener(OnPositionListener positionListener) {
        robot.unregisterPositionListener(positionListener);
    }

    public void addListener(OnSNListener listener) {
        robot.setSnListener(listener);
    }

    public void addListener(OnDeviceInfoListener listener) {
        robot.setDeviceInfoListener(listener);
    }

    public void addListener(OnRobotStateListener listener) {
        robot.setRobotStateListener(listener);
    }

    public void addListener(OnSnapshotoListener listener) {
        robot.setSnapshotoListener(listener);
    }

    public void addListener(OnFaceSaveListener listener) {
        robot.setFaceSaveListener(listener);
    }

    public void addListener(OnSetSNListener listener) {
        robot.setSnListener(listener);
    }

    public void addListener(OnSpeechErrorListener listener) {
        robot.setSpeechErrorListener(listener);
    }

    public void addListener(OnMapListener listener) {
        robot.setMapListener(listener);
    }

    public void addListener(OnGetVersionListener listener) {
        robot.setGetVersionListener(listener);
    }

    public void addListener(OnLinuxRobotTypeListener listener) {
        robot.setLinuxRobotTypeListener(listener);
    }

    /**
     * 热词接收回调
     *
     * @param listener
     */
    public void addListener(OnHotWordsListener listener) {
        robot.setHotWordsListener(listener);
    }

    public void addListener(OnWakeupListener listener) {
        robot.addWakeupListener(listener);
    }

    public void addListener(OnSpeedSetListener listener) {
        robot.setSpeedSetListener(listener);
    }

    public void addListener(OnArmWaveListener listener) {
        robot.setArmWaveListener(listener);
    }

    public void addListener(OnMicroVolumeListener listener) {
        robot.setMicroVolumeListener(listener);
    }

    public void addListener(OnHeadTouchListener listener) {
        robot.setHeadTouchListener(listener);
    }

    public void addListener(OnChargetStateListener listener) {
        robot.setOnChargetStateListener(listener);
    }

    public void addListener(OnDetectPersonListener listener) {
        robot.setDetectPersonListener(listener);
    }

    public void addListener(OnUpgradeListener listener) {
        robot.setOnUpgradeListener(listener);
    }

    public void addListener(OnSpeechGetResultListener listener) {
        robot.setOnSpeechGetResultListener(listener);
    }

    public void addListener(OnCustomServiceMsgListener listener) {
        robot.setCustomServiceMsgListener(listener);
    }

    public void addListener(OnWarningCheckSelfListener listener) {
        robot.setWarningCheckSelfListener(listener);
    }

    public void addListener(OnShutdownListener listener) {
        robot.setShutdownListener(listener);
    }

    public void addListener(OnSyncFaceListener listener) {
        robot.setSyncFaceListener(listener);
    }

    public void removeListener(OnFaceListener listener) {
        robot.removeFaceListener(listener);
    }

    public void clearFaceListener() {
        robot.clearFaceListener();
    }

    /**
     * 连接底层通信
     *
     * @param context
     */
    public void connect(Context context) {
        context.startService(new Intent(context, HandlerMsgService.class));
//        context.startService(new Intent(context, ServerService.class));
    }

    /**
     * 断开底层通信
     *
     * @param context
     */
    public void disconnect(Context context) {
        context.stopService(new Intent(context, HandlerMsgService.class));
//        context.stopService(new Intent(context, ServerService.class));
    }

    /**
     * 连接视频流通信
     *
     * @param context
     */
    public void cameraConnect(Context context) {
        context.startService(new Intent(context, CameraService.class));
    }

    /**
     * 断开视频流通信
     *
     * @param context
     */
    public void cameraDisconnect(Context context) {
        context.stopService(new Intent(context, CameraService.class));
    }

    public boolean getConnectState() {
        return Robot.isLinuxConnect;
    }

    /**
     * 透传方法
     *
     * @param json
     */
    public void transparentTransmission(String json) {
        robot.transparentTransmission(json);
    }


    public int getHardWareVersion() {
        return robot.getHardWareVersion();
    }

    public void setHardWareVersion(int hardWareVersion) {
        robot.setHardWareVersion(hardWareVersion);
    }

}
