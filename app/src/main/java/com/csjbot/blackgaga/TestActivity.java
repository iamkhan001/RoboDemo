package com.csjbot.blackgaga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.chat_handle.HandleProblemFactory;
import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.settings.ProductSNActivity;
import com.csjbot.blackgaga.feature.take_number.TakeNumberActivity;
import com.csjbot.blackgaga.localbean.DeviceInfo;
import com.csjbot.blackgaga.model.tcp.dance.DanceImpl;
import com.csjbot.blackgaga.model.tcp.dance.IDance;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.proxy.SpeakProxy;
import com.csjbot.cameraclient.utils.CameraLogger;
import com.csjbot.cosclient.CosClientAgent;
import com.csjbot.cosclient.entity.CommonPacket;
import com.csjbot.cosclient.entity.MessagePacket;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnDeviceInfoListener;
import com.csjbot.coshandler.listener.OnExpressionListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnSetSNListener;
import com.csjbot.coshandler.listener.OnSpeechListener;
import com.csjbot.coshandler.tts.SpeechFactory;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseFullScreenActivity {

    RobotManager mRobotManager;

    @BindView(R.id.iv_camera)
    ImageView iv_camera;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

        init();
    }

    @Override
    public void init() {
        SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.IFLY);
        mRobotManager = RobotManager.getInstance();
        mRobotManager.addListener(new OnFaceListener() {
            @Override
            public void personInfo(String json) {

            }

            @Override
            public void personNear(boolean person) {
                CosLogger.debug("person--->" + person);
                if (person) {
                    SpeakProxy.getInstance().startSpeaking(getString(R.string.hello_welcome), null);
                    CosLogger.debug("有人来啦!");
                } else {
                    CosLogger.debug("人都走啦!");
                }
            }

            @Override
            public void personList(String json) {
                CosLogger.debug("database:json:" + json);
            }
        });
        mRobotManager.addListener(new OnCameraListener() {
            @Override
            public void response(Bitmap reponse) {
                CosLogger.debug("========");
                if (reponse != null) {
                    runOnUiThread(() -> iv_camera.setImageBitmap(reponse));

                }
            }
        });
        mRobotManager.addListener(new OnExpressionListener() {
            @Override
            public void response(int expression) {
                CosLogger.debug("当前表情为:" + expression);
            }
        });
        mRobotManager.addListener(new OnSpeechListener() {
            @Override
            public void speechInfo(String json, int type) {
                CosLogger.debug("识别语音内容:" + json);

                String answerText = null;
                try {
                    answerText = new JSONObject(json).getJSONObject("result")
                            .getJSONObject("answer")
                            .getString("answer_text");
                    String json_str = "{\"msg_id\":\"SPEECH_TTS_REQ\",\"content\":\"" + answerText + "\"}";
                    MessagePacket packet = new CommonPacket(json.getBytes());
                    CosClientAgent.getRosClientAgent().sendMessage(packet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CosLogger.debug("answer_text:" + answerText);
            }
        });

        RobotManager.getInstance().addListener(new OnSNListener() {
            @Override
            public void response(String sn) {
                CameraLogger.debug(sn);
            }
        });

        RobotManager.getInstance().addListener(new OnDeviceInfoListener() {
            @Override
            public void response(String info) {
                CameraLogger.debug(info);
                DeviceInfo deviceInfo = new Gson().fromJson(info, DeviceInfo.class);
                if (deviceInfo.containsEmpty()) {
                    RobotManager.getInstance().robot.reqProxy.getDeviceInfo();
                    return;
                }
                Intent intent = new Intent(TestActivity.this, ProductSNActivity.class);
                intent.putExtra(com.csjbot.blackgaga.global.Constants.DOWN_PLATE, deviceInfo.getDownPlate());
                intent.putExtra(com.csjbot.blackgaga.global.Constants.UP_PLATE, deviceInfo.getUpPlate());
                intent.putExtra(com.csjbot.blackgaga.global.Constants.UP_COMPUTER, deviceInfo.getUpComputer());
                intent.putExtra(com.csjbot.blackgaga.global.Constants.NAV, deviceInfo.getNav());
                startActivity(intent);
            }
        });

        mRobotManager.addListener(new OnSetSNListener() {
            @Override
            public void response(String resp) {
                CameraLogger.debug(resp);
                try {
                    JSONObject jo = new JSONObject(resp);
                    int error_code = jo.optInt("error_code");
                    if (error_code == 0) {
                        CameraLogger.debug("设置成功");
                    } else {
                        CameraLogger.debug("设置失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.bt_connect)
    public void connect() {
        mRobotManager.connect(this);
    }

    @OnClick(R.id.bt_disconnect)
    public void disconnect(View v) {
        mRobotManager.disconnect(this);
    }

    @OnClick(R.id.bt_start_speech)
    public void startSpeechService() {
        ServerFactory.getSpeechInstance().startSpeechService();
    }

    @OnClick(R.id.bt_stop_speech)
    public void stopSpeechService() {
        ServerFactory.getSpeechInstance().closeSpeechService();
    }

    @OnClick(R.id.bt_start_isr)
    public void startISR() {
        ServerFactory.getSpeechInstance().startIsr();
    }

    @OnClick(R.id.bt_stop_isr)
    public void stopISR() {
        ServerFactory.getSpeechInstance().stopIsr();
    }

    @OnClick(R.id.bt_open_face)
    public void openFaceService() {
        ServerFactory.getFaceInstance().openFace();
    }

    @OnClick(R.id.bt_close_face)
    public void closeFaceService() {
        ServerFactory.getFaceInstance().closeFace();
    }


    @OnClick(R.id.bt_smile)
    public void smile() {
        ServerFactory.getExpressionInstantce().smile();
    }

    @OnClick(R.id.bt_sadness)
    public void sadness() {
        ServerFactory.getExpressionInstantce().sadness();
    }

    @OnClick(R.id.bt_surprised)
    public void surprised() {
        ServerFactory.getExpressionInstantce().surprised();
    }

    @OnClick(R.id.bt_angry)
    public void angry() {
        ServerFactory.getExpressionInstantce().angry();
    }


    @OnClick(R.id.bt_deny)
    public void denyAction() {
        ServerFactory.getActionInstance().denyAction();
    }

    @OnClick(R.id.bt_nod)
    public void nodAction() {
        ServerFactory.getActionInstance().nodAction();
    }

    @OnClick(R.id.bt_speech_handle)
    public void speechHandle() {
        Constants.SchemeStatus.CUSTOM_DISABLE = true;
        Constants.SchemeStatus.CLOUD_DISABLE = true;
        Constants.SchemeStatus.PROFESSIONAL_DISABLE = true;
        HandleProblemFactory
                .getInstance()
                .getAnswer("你好呀", "哈喽",
                        message -> SpeakProxy.getInstance().startSpeaking(message.getAnswerText(), null));
    }

    @OnClick(R.id.bt_open_camera)
    public void openCameraStream() {
        mRobotManager.cameraConnect(this);
    }

    @OnClick(R.id.bt_close_camera)
    public void closeCameraStream() {
        mRobotManager.cameraDisconnect(this);
    }

    @OnClick(R.id.bt_get_position)
    public void getPosition() {
        ServerFactory.getChassisInstance().getPosition();
    }

    @OnClick(R.id.bt_turn_left)
    public void turnLeft() {
        ServerFactory.getChassisInstance().turnLeft();
    }

    @OnClick(R.id.bt_turn_right)
    public void turnRight() {
        ServerFactory.getChassisInstance().turnRight();
    }

    @OnClick(R.id.bt_forward)
    public void goForward() {
        ServerFactory.getChassisInstance().moveForward();
    }

    @OnClick(R.id.bt_back)
    public void goBack() {
        ServerFactory.getChassisInstance().moveBack();

    }

    @OnClick(R.id.bt_go_left)
    public void goLeft() {
        ServerFactory.getChassisInstance().moveLeft();
    }

    @OnClick(R.id.bt_go_right)
    public void goRight() {
        ServerFactory.getChassisInstance().moveRight();
    }

    @OnClick(R.id.bt_navi)
    public void goNavi() {
        startActivity(new Intent(this, NaviActivity.class));
    }


    @OnClick(R.id.bt_swing_arm)
    public void swingArm() {
        ServerFactory.getActionInstance().righLargeArmUp();
        ServerFactory.getActionInstance().rightSmallArmUp();
    }

    IDance dance = new DanceImpl();

    @OnClick(R.id.bt_dance)
    public void dance() {
//        dance.dance(60000, "/mnt/sdcard/cd.mp3");
    }

    @OnClick(R.id.bt_stop_dance)
    public void stopDance() {
        dance.stopDance();
    }

    @OnClick(R.id.bt_go_take_number)
    public void goTakeNumber() {
        startActivity(new Intent(this, TakeNumberActivity.class));
    }

    @OnClick(R.id.bt_sn)
    public void getSN() {
        RobotManager.getInstance().robot.reqProxy.getSN();
    }

    @OnClick(R.id.bt_device)
    public void getDeviceInfo() {
        RobotManager.getInstance().robot.reqProxy.getDeviceInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.bt_set_sn)
    public void setSN() {
        EditText editText = new EditText(this);
        editText.setHint(getString(R.string.input_sn));
        new AlertDialog.Builder(this).setView(editText)
                .setPositiveButton(getString(R.string.sure), (dialog, which) -> {
                    String sn = editText.getText().toString();
                    RobotManager.getInstance().robot.reqProxy.setSN(sn);
                    dialog.dismiss();
                }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }
}
