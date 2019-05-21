package com.csjbot.coshandler.core;

import com.csjbot.coshandler.client_req.ReqFactory;
import com.csjbot.coshandler.client_req.body_action.IBodyActionReq;
import com.csjbot.coshandler.client_req.chassis.IChassisReq;
import com.csjbot.coshandler.client_req.chassis.IStatusSearch;
import com.csjbot.coshandler.client_req.config.IConfigReq;
import com.csjbot.coshandler.client_req.custom_service.ICustomServiceReq;
import com.csjbot.coshandler.client_req.expression.IExpressionReq;
import com.csjbot.coshandler.client_req.extra_func.IExtraFunctionReq;
import com.csjbot.coshandler.client_req.face.IFaceReq;
import com.csjbot.coshandler.client_req.print.IPrintReq;
import com.csjbot.coshandler.client_req.robot_state.IRobotStateReq;
import com.csjbot.coshandler.client_req.sn.ISNReq;
import com.csjbot.coshandler.client_req.speech.ISpeechReq;
import com.csjbot.coshandler.client_req.version.IVersionReq;

import java.util.List;

/**
 * 所有基础请求的代理类
 * Created by jingwc on 2017/9/5.
 */

public class ClientReqProxy implements
        IBodyActionReq
        , IFaceReq
        , ISpeechReq
        , IExpressionReq
        , IChassisReq
        , IPrintReq
        , IRobotStateReq
        , IVersionReq
        , IConfigReq
        , IStatusSearch, ICustomServiceReq, IExtraFunctionReq {

    /* 上肢体动作接口 */
    private IBodyActionReq bodyActionReq;
    /* 人脸识别接口 */
    private IFaceReq faceReq;
    /* 语音识别接口 */
    private ISpeechReq speechReq;
    /* 表情接口 */
    private IExpressionReq expressionReq;
    /* 底盘接口 */
    private IChassisReq chassisReq;
    /* 打印接口 */
    private IPrintReq printReq;

    private IRobotStateReq robotStateReq;

    private final ISNReq snReq;

    private IVersionReq versionReq;

    private IConfigReq configReq;

    private ICustomServiceReq customServiceReq;

    /* 额外功能接口 */
    private IExtraFunctionReq extraFunctionReq;

    public ClientReqProxy() {
        bodyActionReq = ReqFactory.getBodyActionReqInstance();
        faceReq = ReqFactory.getFaceReqInstance();
        speechReq = ReqFactory.getSpeechReqInstance();
        expressionReq = ReqFactory.getExpressionReqInstance();
        chassisReq = ReqFactory.getChassisReqInstance();
        printReq = ReqFactory.getPrintReqInstantce();
        snReq = ReqFactory.getSnReqInstance();
        robotStateReq = ReqFactory.getRobotStateInstantce();
        versionReq = ReqFactory.getVersionInstance();
        configReq = ReqFactory.getConfigReqInstance();
        customServiceReq = ReqFactory.getCustomServiceInstantce();
        extraFunctionReq = ReqFactory.getExtraFunctionInstance();
    }

    /**
     * 重置动作
     */
    @Override
    public void reset() {
        bodyActionReq.reset();
    }

    /**
     * 上肢体动作
     */
    @Override
    public void action(int bodyPart, int action) {
        bodyActionReq.action(bodyPart, action);
    }

    /**
     * 开始摆手
     */
    @Override
    public void startWaveHands(int intervalTime) {
        bodyActionReq.startWaveHands(intervalTime);
    }

    /**
     * 停止摆手
     */
    @Override
    public void stopWaveHands() {
        bodyActionReq.stopWaveHands();
    }

    @Override
    public void startDance() {
        bodyActionReq.startDance();
    }

    @Override
    public void stopDance() {
        bodyActionReq.stopDance();
    }

    /**
     * 打开摄像头
     */
    @Override
    public void openVideo() {
        faceReq.openVideo();
    }

    /**
     * 关闭摄像头
     */
    @Override
    public void closeVideo() {
        faceReq.closeVideo();
    }

    /**
     * 启动人脸识别服务
     */
    @Override
    public void startFaceService() {
        faceReq.startFaceService();
    }

    /**
     * 关闭人脸识别服务
     */
    @Override
    public void closeFaceService() {
        faceReq.closeFaceService();
    }

    /**
     * 人脸注册准备
     */
    @Override
    public void prepareReg() {
        faceReq.prepareReg();
    }

    /**
     * 人脸注册结束
     */
    @Override
    public void faceRegEnd() {
        faceReq.faceRegEnd();
    }

    /**
     * 摄像头拍照
     */
    @Override
    public void snapshot() {
        faceReq.snapshot();
    }

    /**
     * 人脸注册
     */
    @Override
    public void saveFace(String name) {
        faceReq.saveFace(name);
    }

    @Override
    public void faceDel(int faceId) {
        faceReq.faceDel(faceId);
    }

    @Override
    public void faceDelList(String faceIdsJson) {
        faceReq.faceDelList(faceIdsJson);
    }

    @Override
    public void faceInfoChanged(String faceId) {
        faceReq.faceInfoChanged(faceId);
    }

    @Override
    public void getFaceDatabase() {
        faceReq.getFaceDatabase();
    }

    @Override
    public void syncFaceData() {
        faceReq.syncFaceData();
    }

    /**
     * 开启讯飞语音服务
     */
    @Override
    public void startSpeechService() {
        speechReq.startSpeechService();
    }

    /**
     * 关闭讯飞语音服务
     */
    @Override
    public void closeSpeechService() {
        speechReq.closeSpeechService();
    }

    /**
     * 开启多次识别
     */
    @Override
    public void startIsr() {
        speechReq.startIsr();
    }

    /**
     * 停止多次识别
     */
    @Override
    public void stopIsr() {
        speechReq.stopIsr();
    }

    /**
     * 开始单次识别
     */
    @Override
    public void startOnceIsr() {
        speechReq.startOnceIsr();
    }

    /**
     * 停止单次识别
     */
    @Override
    public void stopOnceIsr() {
        speechReq.stopOnceIsr();
    }

    /**
     * 唤醒机器人麦克风
     */
    @Override
    public void openMicro() {
        speechReq.openMicro();
    }

    /**
     * 发送tts说话请求
     *
     * @param content
     */
    @Override
    public void speak(String content) {
        speechReq.speak(content);
    }

    /**
     * 停止说话请求
     */
    @Override
    public void stopSpeak() {
        speechReq.stopSpeak();
    }

    @Override
    public void setLanguage(String language) {
        speechReq.setLanguage(language);
    }

    @Override
    public void setLanguageAndAccent(String language, String accent) {
        speechReq.setLanguageAndAccent(language,accent);
    }

    @Override
    public void getResult(String text) {
        speechReq.getResult(text);
    }

    /**
     * 设置表情
     *
     * @param expression
     * @param once
     * @param time
     */
    @Override
    public void setExpression(int expression, int once, int time) {
        expressionReq.setExpression(expression, once, time);
    }

    /**
     * 获取 SN
     */
    public void getSN() {
        snReq.getSN();
    }

    /**
     * 获取设备列表信息
     */
    public void getDeviceInfo() {
        snReq.getDeviceList();
    }

    /**
     * 设置SN
     */
    public void setSN(String sn) {
        snReq.setSN(sn);
    }

    /**
     * 获取当前表情
     */
    @Override
    public void getExpression() {
        expressionReq.getExpression();
    }

    @Override
    public void updateExpression() {
        expressionReq.updateExpression();
    }

    @Override
    public void getPosition() {
        chassisReq.getPosition();
    }

    @Override
    public void move(int direction) {
        chassisReq.move(direction);
    }

    @Override
    public void navi(String json) {
        chassisReq.navi(json);
    }

    @Override
    public void cancelNavi() {
        chassisReq.cancelNavi();
    }

    @Override
    public void goAngle(int rotation) {
        chassisReq.goAngle(rotation);
    }

    @Override
    public void moveAngle(int rotation) {
        chassisReq.moveAngle(rotation);
    }

    @Override
    public void goHome() {
        chassisReq.goHome();
    }

    @Override
    public void saveMap() {
        chassisReq.saveMap();
    }

    @Override
    public void loadMap() {
        chassisReq.loadMap();
    }

    @Override
    public void setSpeed(float speed) {
        chassisReq.setSpeed(speed);
    }

    @Override
    public void shutdown() {
        robotStateReq.shutdown();
    }

    @Override
    public void reboot() {
        robotStateReq.reboot();
    }

    @Override
    public void getBattery() {
        robotStateReq.getBattery();
    }

    @Override
    public void checkSelf() {
        robotStateReq.checkSelf();
    }

    /**
     * 获取硬件版本
     */
    @Override
    public void getRobotHWVersion() {
        robotStateReq.getRobotHWVersion();
    }

    /**
     * 获取Linux储存的机器人硬件类型，默认是迎宾
     */
    @Override
    public void getLinuxRobotType() {
        robotStateReq.getLinuxRobotType();
    }

    /**
     * 设置Linux储存的机器人硬件类型，默认是迎宾
     *
     * @param type
     */
    @Override
    public void setLinuxRobotType(String type) {
        robotStateReq.setLinuxRobotType(type);
    }

    @Override
    public void openPrinter() {
        printReq.openPrinter();
    }

    @Override
    public void printerCut() {
        printReq.printerCut();
    }

    @Override
    public void printText(String text) {
        printReq.printText(text);
    }

    @Override
    public void printQRCode(String qrcode) {
        printReq.printQRCode(qrcode);
    }

    @Override
    public void printImage(String image) {
        printReq.printImage(image);
    }

    @Override
    public void getVersion() {
        versionReq.getVersion();
    }

    @Override
    public void softwareCheck() {
        versionReq.softwareCheck();
    }

    @Override
    public void softwareUpgrade() {
        versionReq.softwareUpgrade();
    }

    @Override
    public void setMicroVolume(int volume) {
        configReq.setMicroVolume(volume);
    }

    @Override
    public void getMicroVolume() {
        configReq.getMicroVolume();
    }

    @Override
    public void search() {
        chassisReq.search();
    }

    @Override
    public void callHumanService(String sn) {
        customServiceReq.callHumanService(sn);
    }

    @Override
    public void getHotWords() {
        extraFunctionReq.getHotWords();
    }

    @Override
    public void setHotWords(List<String> hotwords) {
        extraFunctionReq.setHotWords(hotwords);
    }

    @Override
    public void startFaceFollow() {
        extraFunctionReq.startFaceFollow();
    }

    @Override
    public void stopFaceFollow() {
        extraFunctionReq.stopFaceFollow();
    }
}
