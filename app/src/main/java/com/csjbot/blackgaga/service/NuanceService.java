package com.csjbot.blackgaga.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.nuance.CsjbotAudioGenerator;
import com.csjbot.blackgaga.nuance.NuanceLanguages;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.ConnectConstants;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.voiceclient.VoiceClientAgent;
import com.csjbot.voiceclient.constant.VoiceClientConstant;
import com.csjbot.voiceclient.listener.VoiceEventListener;
import com.csjbot.voiceclient.utils.VoiceLogger;
import com.roobo.vui.RError;
import com.roobo.vui.api.AutoTypeController;
import com.roobo.vui.api.IASRController;
import com.roobo.vui.api.InitListener;
import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.asr.RASRListener;
import com.roobo.vui.business.auth.entity.UserInfo;
import com.roobo.vui.common.recognizer.ASRResult;
import com.roobo.vui.common.recognizer.EventType;
import com.roobo.vui.recognizer.OnAIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/11/12.
 */

public class NuanceService extends Service {

    public static String currentLanguage = NuanceLanguages.Mandarin_China;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    VUIApi mVUI;

    private IASRController mASRController;

    CsjbotAudioGenerator csjbotAudioGenerator;

    VoiceClientAgent voiceClientAgent;

    private static final int FRAME_SIZE = 2560;
    private byte[] mDataBuf = new byte[FRAME_SIZE*512];
    private byte[] mTempDataBuf = new byte[FRAME_SIZE];

    int len = 0;

    int count = 1;

    private volatile int d;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init(){
        CsjlogProxy.getInstance().info("SN:"+Robot.SN);
        if(TextUtils.isEmpty(Robot.SN)){
            stopSelf();
            return;
        }
        mVUI = VUIApi.getInstance();
        csjbotAudioGenerator = new CsjbotAudioGenerator();
        VUIApi.SourceType micModel = VUIApi.SourceType.ANDROID_STANDARD;
        String wakeupGrammarFile = "test_offline";
        VUIApi.InitParam.InitParamBuilder builder = new VUIApi.InitParam.InitParamBuilder();

        UserInfo userInfo = new UserInfo();
        CsjlogProxy.getInstance().info("SN:"+Robot.SN);
        userInfo.setDeviceID(Robot.SN);
        userInfo.setAgentID("Please replace it with your key");
        userInfo.setPublicKey("Please replace it with your key");
        builder.setEnv(VUIApi.EnvType.ENV_ONLINE).setUserInfo(userInfo).setLanguage(currentLanguage).setMicModel(micModel).addOfflineFileName(wakeupGrammarFile)
                .setVUIType(VUIApi.VUIType.AUTO).setAudioGenerator(csjbotAudioGenerator);


        mVUI.init(this, builder.build(),
                new InitListener() {
                    @Override
                    public void onSuccess() {
                        CsjlogProxy.getInstance().debug("onSucess: called");
                        setListener();
                        reprotLocation();

                        start();
                    }

                    @Override
                    public void onFail(RError rError) {
                        CsjlogProxy.getInstance().debug("onFail: " + rError.getFailDetail());
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            CsjlogProxy.getInstance().debug("重新初始化init Nuance");
                            init();
                        },30000);
                    }
                });

        voiceClientAgent = new VoiceClientAgent(((VoiceEventListener) voiceClientEvent -> {
            switch (voiceClientEvent.eventType) {
                case VoiceClientConstant.EVENT_RECONNECTED:
                case VoiceClientConstant.EVENT_CONNECT_SUCCESS:
                    VoiceLogger.info("EVENT_CONNECT_SUCCESS");
                    break;
                case VoiceClientConstant.EVENT_CONNECT_FAILD:
                    VoiceLogger.error("EVENT_CONNECT_FAILD " + String.valueOf(voiceClientEvent.data));
                    break;
                case VoiceClientConstant.EVENT_CONNECT_TIME_OUT:
                    VoiceLogger.error("EVENT_CONNECT_TIME_OUT  " + String.valueOf(voiceClientEvent.data));
                    break;
                case VoiceClientConstant.SEND_FAILED:
                    VoiceLogger.error("SEND_FAILED");
                    break;
                case VoiceClientConstant.EVENT_DISCONNET:
                    VoiceLogger.error("EVENT_DISCONNET");
                    break;
                case VoiceClientConstant.EVENT_PACKET:
                    // 收到数据就不停的写
                    byte[] data = (byte[]) voiceClientEvent.data;

                    d++;
                    if(d > 25){
                        CsjlogProxy.getInstance().info("EVENT_PACKET 音频数据");
                        d = 0;
                    }


                    if(((len+data.length)>mDataBuf.length)){
                        byte[] temp = new byte[(len-((FRAME_SIZE*count)-FRAME_SIZE))];
                        System.arraycopy(mDataBuf, ((FRAME_SIZE*count)-FRAME_SIZE), temp, 0, temp.length);
                        System.arraycopy(temp, 0, mDataBuf, 0, temp.length);

                        count = 1;
                        len = 0;
                    }

                    System.arraycopy(data, 0, mDataBuf, len, data.length);
                    len += data.length;
                    if(len >= (FRAME_SIZE*count)){
                        System.arraycopy(mDataBuf, ((FRAME_SIZE*count)-FRAME_SIZE), mTempDataBuf, 0, FRAME_SIZE);

                        csjbotAudioGenerator.setData(mTempDataBuf);
                        count++;
                    }
                    break;
                default:
                    break;
            }
        }));
        voiceClientAgent.connect(ConnectConstants.serverIp,60004);

    }

    private void setListener() {
        mVUI.setASRListener(new RASRListener() {
            @Override
            public void onASRResult(final ASRResult result) {
                CsjlogProxy.getInstance().debug("ASRResult " + (result.getResultType() == ASRResult.TYPE_OFFLINE ? "offline " : " online ") + " text " + result.getResultText());

                String dataSource = "{\"msg_id\":\"SPEECH_ISR_ONLY_RESULT_NTF\",\"text\":\""+result.getResultText()+"\"}";
                Robot.getInstance().pushSpeech(dataSource, Robot.SPEECH_ASR_ONLY);

//                String answer = "";
//                String dataSource2 = "{\n" +
//                        "\t\"msg_id\": \"SPEECH_ISR_LAST_RESULT_NTF\",\n" +
//                        "\t\"result\": {\n" +
//                        "\t\t\"text\": \""+result.getResultText()+"\",\n" +
//                        "\t\t\"answer\": {\n" +
//                        "\t\t\t\"type\": 0,\n" +
//                        "\t\t\t\"answer_text\": \""+answer+"\"\n" +
//                        "\t\t}\n" +
//                        "\t},\n" +
//                        "\t\"data\": {\n" +
//                        "\n" +
//                        "\t},\n" +
//                        "\t\"error_code\": 0\n" +
//                        "}";
//                new Handler().postDelayed(() -> Robot.getInstance().pushSpeech(dataSource2, Robot.SPEECH_LAST_RESULT),500);
                new Handler().postDelayed(() -> getAnswer(result.getResultText()),100);
            }

            @Override
            public void onFail(final RError message) {
                CsjlogProxy.getInstance().debug("asr onFail ");
            }

            @Override
            public void onWakeUp(final String json) {
                CsjlogProxy.getInstance().debug("onWakeup: " + json);
            }

            @Override
            public void onEvent(EventType event) {
                CsjlogProxy.getInstance().debug("EventType: " + event);
            }
        });


        mVUI.setOnAIResponseListener(new OnAIResponseListener() {
            @Override
            public void onResult(final String json) {

            }

            @Override
            public void onFail(final RError rError) {

            }
        });
    }

    private void getAnswer(String text){

        String requestJson = "{\"sn\":\""+Robot.SN +"\",\"question\":\""+text+"\"}";
        com.csjbot.blackgaga.model.http.factory.ServerFactory.createApi().getAnswer(requestJson, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String bodyJson = "";
                try {
                    bodyJson = responseBody.string();
                } catch (IOException e) {
                }
                CsjlogProxy.getInstance().info("getAnswer:onNext");
                CsjlogProxy.getInstance().info("getAnswer:bodyJson:"+bodyJson);
                if(!TextUtils.isEmpty(bodyJson)){
                    try {
                        JSONObject jsonObject = new JSONObject(bodyJson);
                        int state = jsonObject.getInt("state");


                        String jsonResult = "";

                        if(state == 0){
                            String message = jsonObject.getString("message");
                            jsonResult = "{\"result\":{\"answer\":{\"answer_text\":\""+message+"\",\"type\":0},\"data\":"+bodyJson+",\"error_code\":0,\"text\":\""+text+"\"}}";
                        }else if(state == 1){
                            String answerText = jsonObject.getJSONObject("data").getString("answer");
                            jsonResult = "{\"result\":{\"answer\":{\"answer_text\":\""+answerText+"\",\"type\":0},\"data\":"+bodyJson+",\"error_code\":0,\"text\":\""+text+"\"}}";
                        }else if(state < 0){
                            jsonResult = "{\"result\":{\"answer\":{\"answer_text\":\"\",\"type\":-1},\"data\":null,\"error_code\":10119,\"text\":\""+text+"\"}}";
                        }
                        Robot.getInstance().pushSpeech(jsonResult,Robot.SPEECH_LAST_RESULT);
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().error("e:"+e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                CsjlogProxy.getInstance().info("getAnswer:onError");
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void reprotLocation() {
        WifiManager wifiManager = (WifiManager) getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mVUI.reportLocationInfo(wifiManager.getScanResults());
    }

    private void start(){
//        if (checkRecordStateSuccess()) {
//
//            mASRController = mVUI.startRecognize();
//
//            new Handler().postDelayed(() -> {
//                if (mASRController instanceof AutoTypeController) {
//                    ((AutoTypeController) mASRController).manualWakeup();
//                }
//            },2000);
//        } else {
//            //请检查录音权限及AudioRecorder是否被占用
//            Toast.makeText(this, "请检查录音权限及AudioRecorder是否被占用", Toast.LENGTH_SHORT).show();
//        }

        mASRController = mVUI.startRecognize();

            new Handler().postDelayed(() -> {
                if (mASRController instanceof AutoTypeController) {
                    ((AutoTypeController) mASRController).manualWakeup();
                }
                int languageMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
                switch (languageMode){
                    case Constants.Language.CHINESE:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Mandarin_China);
                        break;
                    case Constants.Language.ENGELISH_US:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.English_US);
                        break;
                    case Constants.Language.ENGELISH_UK:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.English_UK);
                        break;
                    case Constants.Language.JAPANESE:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Japanese);
                        break;
                    case Constants.Language.FRANCH_FRANCE:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.French_France);
                        break;
                    case Constants.Language.SPANISH_SPAIN:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Spanish_Spain);
                        break;
                    case Constants.Language.PORTUGUESE_PORTUGAL:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Portuguese_Portugal);
                        break;
                    case Constants.Language.INDONESIA:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Bahasa_Indonesia);
                        break;
                    case Constants.Language.RUSSIAN:
                        VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Russian);
                        break;
                }
            },2000);


    }

    private void stop(){
        mVUI.stopRecognize();
        mVUI.release();
    }

    public static boolean checkRecordStateSuccess() {
        int minBuffer = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, (minBuffer * 2));
        short[] point = new short[minBuffer];
        int readSize = 0;
        try {
            audioRecord.startRecording();//检测是否可以进入初始化状态
        } catch (Exception e) {
            if (audioRecord != null) {
                audioRecord.release();
                audioRecord = null;
                Log.d("CheckAudioPermission", "无法进入录音初始状态");
            }
            return false;
        }
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            //6.0以下机型都会返回此状态，故使用时需要判断bulid版本
            //检测是否在录音中
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.d("CheckAudioPermission", "录音机被占用");
            }
            return false;
        } else {
            //检测是否可以获取录音结果

            readSize = audioRecord.read(point, 0, point.length);
            if (readSize <= 0) {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;

                }
                Log.d("CheckAudioPermission", "录音的结果为空");
                return false;

            } else {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;

                }

                return true;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}
