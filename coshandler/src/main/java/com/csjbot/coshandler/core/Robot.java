package com.csjbot.coshandler.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.csjbot.cosclient.CosClientAgent;
import com.csjbot.cosclient.entity.CommonPacket;
import com.csjbot.cosclient.entity.MessagePacket;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.client_req.custom_service.ICustomServiceReq;
import com.csjbot.coshandler.core.interfaces.IAction;
import com.csjbot.coshandler.core.interfaces.IActionV2;
import com.csjbot.coshandler.core.interfaces.IChassis;
import com.csjbot.coshandler.core.interfaces.IExpression;
import com.csjbot.coshandler.core.interfaces.IExtraFunction;
import com.csjbot.coshandler.core.interfaces.IFace;
import com.csjbot.coshandler.core.interfaces.ISpeech;
import com.csjbot.coshandler.global.ConnectConstants;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.global.RobotContants;
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
import com.csjbot.coshandler.listener.OnNaviSearchListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.listener.OnRobotStateListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnSetSNListener;
import com.csjbot.coshandler.listener.OnShutdownListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechErrorListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.listener.OnSpeechListener;
import com.csjbot.coshandler.listener.OnSpeedSetListener;
import com.csjbot.coshandler.listener.OnSyncFaceListener;
import com.csjbot.coshandler.listener.OnUpgradeListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.listener.OnWarningCheckSelfListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.csjbot.coshandler.tts.ISpeechSpeak;
import com.csjbot.coshandler.tts.SpeechFactory;
import com.csjbot.coshandler.util.ConfInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对外提供的机器人功能类
 * Created by jingwc on 2017/9/5.
 */

public class Robot implements IFace, IAction, ISpeech, IExpression, IChassis, ICustomServiceReq, IActionV2, IExtraFunction {

    private volatile static Robot robot;

    /* 所以基础请求的代理类 */
    public ClientReqProxy reqProxy;

    /* 语音合成类 */
    private ISpeechSpeak speechSpeak;

    private OnSpeechListener speechListener;
    private CopyOnWriteArrayList<OnFaceListener> faceListeners = new CopyOnWriteArrayList<>();
    private OnExpressionListener expressionListener;
    private OnInitListener initListener;
    private OnCameraListener cameraListener;
    private OnChassisListener chassisListener;
    private OnPositionListener positionListener;
    private OnSNListener snListener;
    private OnDeviceInfoListener deviceInfoListener;
    private OnRobotStateListener robotStateListener;
    private OnSnapshotoListener snapshotoListener;
    private OnFaceSaveListener faceSaveListener;
    private OnSetSNListener setSNListener;
    private OnSpeechErrorListener speechErrorListener;
    private OnMapListener mapListener;
    private OnGetVersionListener getVersionListener;
    private CopyOnWriteArrayList<OnWakeupListener> wakeupListeners = new CopyOnWriteArrayList<>();
    private OnSpeedSetListener speedSetListener;
    private OnMicroVolumeListener microVolumeListener;
    private OnArmWaveListener armWaveListener;
    private OnHeadTouchListener headTouchListener;
    private OnNaviSearchListener onNaviSearchListener;
    private OnChargetStateListener onChargetStateListener;
    private OnDetectPersonListener detectPersonListener;
    private OnUpgradeListener onUpgradeListener;
    private OnSpeechGetResultListener onSpeechGetResultListener;
    private OnCustomServiceMsgListener customServiceMsgListener;
    private OnWarningCheckSelfListener warningCheckSelfListener;
    private OnShutdownListener shutdownListener;
    private OnSyncFaceListener syncFaceListener;

    /**
     * 热词监听
     */
    private OnHotWordsListener hotWordsListener;

    private OnLinuxRobotTypeListener linuxRobotTypeListener;

    private List<OnPositionListener> positionListeners = new ArrayList<>();
    private List<OnNaviSearchListener> naviSearchListeners = new ArrayList<>();


    public static final int SPEECH_ASR_ONLY = 0;
    public static final int SPEECH_LAST_RESULT = 1;
    public static final int SPEECH_CUSTOMER_SERVICE = 2;


    private int hardWareVersion = RobotContants.ROBOT_VERSION.ROBOT_VERSION_1_0;


    ////////////////////// SN (开始)
    public static String SN = ConfInfoUtil.getSN();

    public static void initSN() {
        ConfInfoUtil.init();
        getSN();
    }

    public static void setSN(String sn) {
        ConfInfoUtil.putData(sn);
        getSN();
    }

    public static void getSN() {
        SN = ConfInfoUtil.getSN();
    }
    ////////////////////// SN (结束)


    ////////////////////// 连接状态 (开始)
    public static boolean isLinuxConnect;

    public static boolean getConnectState() {
        return isLinuxConnect;
    }

    private void setConnectState(boolean isConnect) {
        isLinuxConnect = isConnect;
    }
    ////////////////////// 连接状态 (结束)

    public static Robot getInstance() {
        if (robot == null) {
            synchronized (Robot.class) {
                if (robot == null) {
                    robot = new Robot();
                }
            }
        }
        return robot;
    }

    private Robot() {
        reqProxy = new ClientReqProxy();
    }

    /**
     * 初始化语音合成
     *
     * @param context
     */
    @Override
    public void initSpeak(Context context, int speechType) {
        speechSpeak = SpeechFactory.createSpeech(context, speechType);
    }


    public void setSpeechListener(OnSpeechListener listener) {
        speechListener = listener;
    }

    public void setFaceListener(OnFaceListener listener) {
        if (listener != null && !faceListeners.contains(listener)) {
            faceListeners.add(listener);
        }
    }

    public void removeFaceListener(OnFaceListener listener) {
        if (listener != null && faceListeners.contains(listener)) {
            faceListeners.remove(listener);
        }
    }

    public void clearFaceListener() {
        faceListeners.clear();
    }

    public void setExpressionListener(OnExpressionListener listener) {
        expressionListener = listener;
    }

    public void setInitListener(OnInitListener listener) {
        initListener = listener;
    }

    public void setCameraListener(OnCameraListener listener) {
        cameraListener = listener;
    }

    public void setChassisListener(OnChassisListener listener) {
        chassisListener = listener;
    }

    public void setPositionListener(OnPositionListener listener) {
        positionListener = listener;
    }

    public synchronized void registerPositionListener(OnPositionListener positionListener) {
        if (positionListener != null && !positionListeners.contains(positionListener)) {
            positionListeners.add(positionListener);
        }
    }

    public synchronized void unregisterPositionListener(OnPositionListener listener) {
        if (listener != null && positionListeners.contains(listener)) {
            positionListeners.remove(listener);
        }
    }

    public void setSnListener(OnSNListener listener) {
        snListener = listener;
    }

    public void setDeviceInfoListener(OnDeviceInfoListener listener) {
        deviceInfoListener = listener;
    }

    public void setRobotStateListener(OnRobotStateListener listener) {
        robotStateListener = listener;
    }

    public void setSnapshotoListener(OnSnapshotoListener listener) {
        snapshotoListener = listener;
    }

    public void setFaceSaveListener(OnFaceSaveListener listener) {
        faceSaveListener = listener;
    }

    public void setSpeechErrorListener(OnSpeechErrorListener listener) {
        speechErrorListener = listener;
    }

    public void setMapListener(OnMapListener listener) {
        mapListener = listener;
    }

    public void setGetVersionListener(OnGetVersionListener listener) {
        getVersionListener = listener;
    }

    public void addWakeupListener(OnWakeupListener listener) {
        if (listener != null && !wakeupListeners.contains(listener)) {
            wakeupListeners.add(listener);
        }
    }

    public void removeWakeupListener(OnWakeupListener listener) {
        if (listener != null && wakeupListeners.contains(listener)) {
            wakeupListeners.remove(listener);
        }
    }

    public void clearWakeupListener() {
        wakeupListeners.clear();
    }

    public void setSpeedSetListener(OnSpeedSetListener speedSetListener) {
        this.speedSetListener = speedSetListener;
    }

    public void setMicroVolumeListener(OnMicroVolumeListener listener) {
        this.microVolumeListener = listener;
    }

    public void setHeadTouchListener(OnHeadTouchListener listener) {
        this.headTouchListener = listener;
    }

    public void setOnNaviSearchListener(OnNaviSearchListener onNaviSearchListener) {
        this.onNaviSearchListener = onNaviSearchListener;
    }

    public void setWarningCheckSelfListener(OnWarningCheckSelfListener listener) {
        this.warningCheckSelfListener = listener;
    }

    public void registerNaviSearchListener(OnNaviSearchListener onNaviSearchListener) {
        naviSearchListeners.add(onNaviSearchListener);
    }

    public void unregisterNaviSearchListener(OnNaviSearchListener onNaviSearchListener) {
        naviSearchListeners.remove(onNaviSearchListener);
    }

    public void setOnChargetStateListener(OnChargetStateListener onChargetStateListener) {
        this.onChargetStateListener = onChargetStateListener;
    }

    public void setDetectPersonListener(OnDetectPersonListener detectPersonListener) {
        this.detectPersonListener = detectPersonListener;
    }

    public void setOnUpgradeListener(OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;
    }

    public void setOnSpeechGetResultListener(OnSpeechGetResultListener onSpeechGetResultListener) {
        this.onSpeechGetResultListener = onSpeechGetResultListener;
    }


    public void setHotWordsListener(OnHotWordsListener listener) {
        hotWordsListener = listener;
    }

    public void setLinuxRobotTypeListener(OnLinuxRobotTypeListener listener) {
        linuxRobotTypeListener = listener;
    }

    public void setShutdownListener(OnShutdownListener listener) {
        shutdownListener = listener;
    }

    public void setSyncFaceListener(OnSyncFaceListener listener) {
        syncFaceListener = listener;
    }

    public void pushSyncFaceResponse(int errorCode) {
        if (syncFaceListener != null) {
            syncFaceListener.response(errorCode);
        }
    }

    public void pushSyncFaceCompelte(int errorCode) {
        if (syncFaceListener != null) {
            syncFaceListener.complete(errorCode);
        }
    }

    public void pushShutdown() {
        if (shutdownListener != null) {
            shutdownListener.response();
        }
    }

    public void pushCheckSelf(String json) {
        if (warningCheckSelfListener != null) {
            warningCheckSelfListener.response(json);
        }
    }

    public void pushSpeechGetResult(String json) {
        if (onSpeechGetResultListener != null) {
            onSpeechGetResultListener.response(json);
        }
    }

    public void pushUpgradeCheck(int errorCode) {
        if (onUpgradeListener != null) {
            onUpgradeListener.checkRsp(errorCode);
        }
    }

    public void pushUpgrade(int errorCode) {
        if (onUpgradeListener != null) {
            onUpgradeListener.upgradeRsp(errorCode);
        }
    }

    public void pushUpgradeProgress(int progress) {
        if (onUpgradeListener != null) {
            onUpgradeListener.upgradeProgress(progress);
        }
    }

    public void pushDetectPerson(int state) {
        if (detectPersonListener != null) {
            detectPersonListener.response(state);
        }
    }

    public void pushChargeState(int state) {
        if (onChargetStateListener != null) {
            onChargetStateListener.response(state);
        }
    }

    public void pushHeadTouch() {
        if (headTouchListener != null) {
            headTouchListener.response();
        }
    }

    public void pushMicrolVolume(int volume) {
        if (microVolumeListener != null) {
            microVolumeListener.response(volume);
        }
    }

    public void setArmWaveListener(OnArmWaveListener armWaveListener) {
        this.armWaveListener = armWaveListener;
    }

    public void setCustomServiceMsgListener(OnCustomServiceMsgListener listener) {
        this.customServiceMsgListener = listener;
    }

    public void pushWakeup(int angle) {
        for (OnWakeupListener listener : wakeupListeners) {
            if (listener != null) {
                listener.response(angle);
            }
        }
    }

    public void pushVersion(String version) {
        if (getVersionListener != null) {
            getVersionListener.response(version);
        }
    }

    public void pushLoadMap(boolean state) {
        if (mapListener != null) {
            mapListener.loadMap(state);
        }

    }

    public void pushSaveMap(boolean state) {
        if (mapListener != null) {
            mapListener.saveMap(state);
        }
    }

    public void pushSetSpeed(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            int error_code = jo.optInt("error_code");
            if (error_code == 0 && speedSetListener != null) {
                speedSetListener.setSpeedResult(true);
            } else if (error_code != 0 && speedSetListener != null) {
                speedSetListener.setSpeedResult(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void pushNaviSearchResult(String json) {
        if (onNaviSearchListener != null) {
            onNaviSearchListener.searchResult(json);

            for (OnNaviSearchListener listener : naviSearchListeners) {
                listener.searchResult(json);
            }
        }
    }

    public void pushSpeechError() {
        if (speechErrorListener != null) {
            speechErrorListener.response();
        }
    }

    public void pushFaceSave(String json) {
        if (faceSaveListener != null) {
            faceSaveListener.response(json);
        }
    }

    public void pushSnapshoto(String json) {
        if (snapshotoListener != null) {
            snapshotoListener.response(json);
        }
    }

    public void setSnListener(OnSetSNListener listener) {
        if (setSNListener != null) {
            setSNListener = listener;
        }
    }

    public void pushArmWaveResult(String json) {
        if (armWaveListener != null) {
            armWaveListener.start(json);
        }
    }

    public void pushArmStopResult(String json) {
        if (armWaveListener != null) {
            armWaveListener.stop(json);
        }
    }


    public void pushCustomServiceMsg(String json) {
        if (customServiceMsgListener != null) {
            customServiceMsgListener.onMsg(json);
        }
    }

    /**
     * 热词
     *
     * @param hotwords
     */
    public void pushHotWords(List<String> hotwords) {
        if (hotWordsListener != null) {
            hotWordsListener.hotWords(hotwords);
        }
    }

    /**
     * linux 机器人类型
     *
     * @param type
     */
    public void pushLinuxRobotType(String type) {
        if (linuxRobotTypeListener != null) {
            linuxRobotTypeListener.linuxRobotType(type);
        }
    }

//    public void pushArmStartWaveResult

    /**
     * 推送发送
     *
     * @param json
     */
    public void pushMoveToResult(String json) {
        if (positionListener != null) {
            positionListener.moveToResult(json);
        }

        for (OnPositionListener listener : positionListeners) {
            listener.moveToResult(json);
        }
    }

    public void pushPosition(String json) {
        if (positionListener != null) {
            positionListener.positionInfo(json);
        }

        for (OnPositionListener listener : positionListeners) {
            listener.positionInfo(json);
        }
    }

    public void pushMoveResult(String json) {
        if (positionListener != null) {
            positionListener.moveResult(json);
        }

        for (OnPositionListener listener : positionListeners) {
            listener.moveResult(json);
        }
    }

    public void pushCancelTask(String json) {
        if (positionListener != null) {
            positionListener.cancelResult(json);
        } else {
            Csjlogger.debug("positionListener为空");
        }

        for (OnPositionListener listener : positionListeners) {
            listener.cancelResult(json);
        }
    }

    /**
     * 向上推送语音识别结果
     *
     * @param json
     */
    public void pushSpeech(String json, int type) {
        if (speechListener != null) {
            speechListener.speechInfo(json, type);
        }
    }

    /**
     * 向上推送人脸识别信息
     *
     * @param json
     */
    public void pushFace(String json) {
        for (OnFaceListener listener : faceListeners) {
            if (listener != null) {
                listener.personInfo(json);
            }
        }

    }

    /**
     * 向上推送人脸检测靠近信息
     *
     * @param person
     */
    public void pushFace(boolean person) {
        for (OnFaceListener listener : faceListeners) {
            if (listener != null) {
                listener.personNear(person);
            }
        }
    }

    /**
     * 向上推送人脸数据库信息
     *
     * @param json
     */
    public void pushFaceList(String json) {
        for (OnFaceListener listener : faceListeners) {
            if (listener != null) {
                listener.personList(json);
            }
        }
    }

    /**
     * 向上推送当前表情
     *
     * @param expression
     */
    public void pushExpression(int expression) {
        if (expressionListener != null) {
            expressionListener.response(expression);
        }
    }

    /**
     * 向上推送视频流
     *
     * @param bitmap
     */
    public void pushCamera(Bitmap bitmap) {
        if (cameraListener != null) {
            cameraListener.response(bitmap);
        }
    }

    /**
     * 向上推送init状态信息
     *
     * @param type
     */
    public void pushInit(int type) {
        if (initListener != null) {
            switch (type) {
                case ConnectConstants.ConnectStatus.SUCCESS:
                    setConnectState(true);
                    initListener.success();
                    break;
                case ConnectConstants.ConnectStatus.FAILD:
                    setConnectState(false);
                    initListener.faild();
                    break;
                case ConnectConstants.ConnectStatus.TIMEOUT:
                    setConnectState(false);
                    initListener.timeout();
                    break;
                case ConnectConstants.ConnectStatus.DISCONNECT:
                    setConnectState(false);
                    initListener.disconnect();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 向上推送 sn 信息
     *
     * @param sn 机器人 SN
     */
    public void pushSN(String sn) {
        if (snListener != null) {
            snListener.response(sn);
        }
    }

    /**
     * 向上推送机器人上身板等设备信息
     *
     * @param info 设备信息
     */
    public void pushDeviceInfo(String info) {
        if (deviceInfoListener != null) {
            deviceInfoListener.response(info);
        }
    }

    /**
     * 向上推送设置 SN 信息
     */
    public void pushSetSnResp(String info) {
        if (setSNListener != null) {
            setSNListener.response(info);
        }
    }

    /**
     * 向上推送机器人电量信息
     *
     * @param battery
     */
    public void pushRobotState(int battery) {
        if (robotStateListener != null) {
            robotStateListener.getBattery(battery);
        }
    }

    @Override
    public void leftLargeArmUp() {
        reqProxy.action(REQConstants.BodyPart.LEFT_ARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void leftLargeArmDown() {
        reqProxy.action(REQConstants.BodyPart.LEFT_ARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void leftSmallArmUp() {
        reqProxy.action(REQConstants.BodyPart.LEFT_FOREARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void leftSmallArmDown() {
        reqProxy.action(REQConstants.BodyPart.LEFT_FOREARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void righLargeArmUp() {
        reqProxy.action(REQConstants.BodyPart.RIGHT_ARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void rightLargeArmDown() {
        reqProxy.action(REQConstants.BodyPart.RIGHT_ARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void rightSmallArmUp() {
        reqProxy.action(REQConstants.BodyPart.RIGHT_FOREARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void rightSmallArmDown() {
        reqProxy.action(REQConstants.BodyPart.RIGHT_FOREARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void doubleLargeArmUp() {
        reqProxy.action(REQConstants.BodyPart.DOUBLE_ARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void doubleLargeArmDown() {
        reqProxy.action(REQConstants.BodyPart.DOUBLE_ARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void doubleSmallArmUp() {
        reqProxy.action(REQConstants.BodyPart.DOUBLE_FOREARM, REQConstants.BodyAction.LEFT_UP);
    }

    @Override
    public void doubleSmallArmDown() {
        reqProxy.action(REQConstants.BodyPart.DOUBLE_FOREARM, REQConstants.BodyAction.RIGHT_DOWN);
    }

    @Override
    public void startWave(int intervalTime) {
        reqProxy.startWaveHands(intervalTime);
    }

    @Override
    public void stopWave() {
        reqProxy.stopWaveHands();
    }

    @Override
    public void snowRightArm() {
        reqProxy.action(1, 1);
    }

    @Override
    public void snowLeftArm() {
        reqProxy.action(2, 1);
    }

    @Override
    public void snowDoubleArm() {
        reqProxy.action(3, 1);
    }

    /**
     * 摇头动作
     */
    @Override
    public void denyAction() {
        reqProxy.action(REQConstants.BodyPart.HEAD, REQConstants.BodyAction.LEFT_THEN_RIGHT);
    }

    /**
     * 点头动作
     */
    @Override
    public void nodAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                reqProxy.action(REQConstants.BodyPart.HEAD, REQConstants.BodyAction.DOWN);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reqProxy.action(REQConstants.BodyPart.HEAD, REQConstants.BodyAction.UP);
            }
        }).start();

    }

    /**
     * 重置动作
     */
    @Override
    public void resetAction() {
        reqProxy.action(REQConstants.BodyPart.RESET, REQConstants.BodyAction.NONE);
    }

    public void expression(int expression) {
        reqProxy.setExpression(expression
                , REQConstants.Expression.NO
                , REQConstants.Expression.NO);
    }

    public void expression(int expression, int once, int time) {
        reqProxy.setExpression(expression, once, time);
    }

    /**
     * 开心表情
     */
    @Override
    public void happy() {
        expression(REQConstants.Expression.HAPPY);
    }

    /**
     * 悲伤表情
     */
    @Override
    public void sadness() {
        expression(REQConstants.Expression.SADNESS);
    }

    /**
     * 惊讶表情
     */
    @Override
    public void surprised() {
        expression(REQConstants.Expression.SURPRISED);
    }

    /**
     * 微笑表情
     */
    @Override
    public void smile() {
        expression(REQConstants.Expression.SMILE);
    }

    /**
     * 普通表情
     */
    @Override
    public void normal() {
        expression(REQConstants.Expression.NORMAL);
    }

    /**
     * 生气表情
     */
    @Override
    public void angry() {
        expression(REQConstants.Expression.ANGRY);
    }

    @Override
    public void lightning() {
        expression(REQConstants.Expression.LIGHTNING);
    }

    @Override
    public void sleepiness() {
        expression(REQConstants.Expression.SLEEPINESS);
    }


    //************************************** TTS **************************************/
    //************************************** TTS **************************************/
    //************************************** TTS **************************************/

    /**
     * 返回tts的发声人列表
     *
     * @param language 语言
     * @param country  国家或者地区
     * @return tts的发声人列表，只会返回空，永不为null
     */
    public ArrayList<String> getSpeakerNames(String language, String country) {
        ArrayList<String> list = speechSpeak.getSpeakerNames(language, country);
        return list == null ? new ArrayList<String>() : list;
    }

    @Override
    public void setVolume(float volume) {
        speechSpeak.setVolume(volume);
    }


    /**
     * 开始说话
     *
     * @param text
     * @param listener
     */
    @Override
    public void startSpeaking(String text, OnSpeakListener listener) {
        if (speechSpeak != null) {
            speechSpeak.startSpeaking(text, listener);
        }
    }


    @Override
    public boolean setSpeakerName(String name) {
        if (speechSpeak != null) {
            speechSpeak.setSpeakerName(name);
        }
        return false;
    }

    @Override
    public boolean setLanguage(Locale language) {
        return speechSpeak.setLanguage(language);
    }

    /**
     * 停止说话
     */
    @Override
    public void stopSpeaking() {
        if (speechSpeak != null) {
            speechSpeak.stopSpeaking();
        }
    }

    /**
     * 暂停说话
     */
    @Override
    public void pauseSpeaking() {
        if (speechSpeak != null) {
            speechSpeak.pauseSpeaking();
        }
    }

    /**
     * 重新说话
     */
    @Override
    public void resumeSpeaking() {
        if (speechSpeak != null) {
            speechSpeak.resumeSpeaking();
        }
    }

    /************************************** TTS  END **************************************/

    @Override
    public boolean isSpeaking() {
        return speechSpeak != null && speechSpeak.isSpeaking();
    }

    //************************************* TTS  END **************************************/
    //************************************* TTS  END **************************************/

    @Override
    public void turnLeft() {
        reqProxy.moveAngle(90);
    }

    @Override
    public void turnRight() {
        reqProxy.moveAngle(-90);
    }

    @Override
    public void moveLeft() {
        reqProxy.move(REQConstants.MoveDirection.LEFT);
    }

    @Override
    public void moveRight() {
        reqProxy.move(REQConstants.MoveDirection.RIGHT);
    }

    @Override
    public void moveForward() {
        reqProxy.move(REQConstants.MoveDirection.FORWARD);
    }

    @Override
    public void moveBack() {
        reqProxy.move(REQConstants.MoveDirection.BACK);
    }


    /**
     * 透传方法
     *
     * @param json
     */
    public void transparentTransmission(String json) {
        if (TextUtils.isEmpty(json)) return;
        CsjlogProxy.getInstance().info(json);
        try {
            MessagePacket packet = new CommonPacket(json.getBytes());
            CosClientAgent.getRosClientAgent().sendMessage(packet);
        } catch (Exception e) {
            CosLogger.error("BaseClientReq:sendReq:e:" + e.toString());
        }
    }

    /**
     * 呼叫人工客服
     */
    @Override
    public void callHumanService(String sn) {
        reqProxy.callHumanService(sn);
    }

    /**
     * 爱丽丝头向上运动
     */
    @Override
    public void AliceHeadUp() {
        reqProxy.action(REQConstants.BodyPartV2.HEAD1, REQConstants.BodyActionV2.HEAD_UP);
    }

    /**
     * 爱丽丝头向下运动
     */
    @Override
    public void AliceHeadDown() {
        reqProxy.action(REQConstants.BodyPartV2.HEAD1, REQConstants.BodyActionV2.HEAD_DOWN);
    }

    /**
     * 爱丽丝头向左运动
     */
    @Override
    public void AliceHeadLeft() {
        reqProxy.action(REQConstants.BodyPartV2.HEAD2, REQConstants.BodyActionV2.HEAD_LEFT);
    }

    /**
     * 爱丽丝头向右运动
     */
    @Override
    public void AliceHeadRight() {
        reqProxy.action(REQConstants.BodyPartV2.HEAD2, REQConstants.BodyActionV2.HEAD_RIGHT);
    }

    /**
     * 爱丽丝头水平重置
     */
    @Override
    public void AliceHeadHReset() {
        reqProxy.action(REQConstants.BodyPartV2.HEAD2, REQConstants.BodyActionV2.HORIZONTAL_RESET);
    }

    /**
     * 爱丽丝左臂抬起
     */
    @Override
    public void AliceLeftArmUp() {
        reqProxy.action(REQConstants.BodyPartV2.LEFT_ARM, REQConstants.BodyActionV2.ARM_UP);
    }

    /**
     * 爱丽丝左臂放下
     */
    @Override
    public void AliceLeftArmDown() {
        reqProxy.action(REQConstants.BodyPartV2.LEFT_ARM, REQConstants.BodyActionV2.ARM_DOWN);
    }

    /**
     * 爱丽丝右臂抬起
     */
    @Override
    public void AliceRightArmUp() {
        reqProxy.action(REQConstants.BodyPartV2.RIGHT_ARM, REQConstants.BodyActionV2.ARM_UP);
    }

    /**
     * 爱丽丝右臂放下
     */
    @Override
    public void AliceRightArmDown() {
        reqProxy.action(REQConstants.BodyPartV2.RIGHT_ARM, REQConstants.BodyActionV2.ARM_DOWN);
    }

    /**
     * 小雪左臂挥动
     *
     * @param count 次数
     */
    @Override
    public void SnowLeftArmSwing(int count) {
        reqProxy.action(REQConstants.SnowBodyActionV2.SNOW_LEFT_ARM_SWING, count);
    }

    /**
     * 小雪右臂挥动
     *
     * @param count 次数
     */
    @Override
    public void SnowRightArmSwing(int count) {
        reqProxy.action(REQConstants.SnowBodyActionV2.SNOW_RIGHT_ARM_SWING, count);
    }

    /**
     * 小雪双臂挥动
     *
     * @param count 次数
     */
    @Override
    public void SnowDoubleArmSwing(int count) {
        reqProxy.action(REQConstants.SnowBodyActionV2.SNOW_DOUBLE_ARM_SWING, count);
    }

    public int getHardWareVersion() {
        return hardWareVersion;
    }

    public void setHardWareVersion(String hw) {
        if (hw.contains("V1")) {
            hardWareVersion = RobotContants.ROBOT_VERSION.ROBOT_VERSION_1_0;
        } else {
            hardWareVersion = RobotContants.ROBOT_VERSION.ROBOT_VERSION_2_0;
        }
    }

    public void setHardWareVersion(int hardWareVersion) {
        this.hardWareVersion = hardWareVersion;
    }

    @Override
    public void getHotWords() {
        reqProxy.getHotWords();
    }

    @Override
    public void setHotWords(List<String> hotwords) {
        reqProxy.setHotWords(hotwords);
    }

    /**
     * 开始人脸跟随
     */
    @Override
    public void startFaceFollow() {
        reqProxy.startFaceFollow();
    }

    /**
     * 关闭人脸跟随
     */
    @Override
    public void stopFaceFollow() {
        reqProxy.stopFaceFollow();
    }
}
