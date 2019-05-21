package com.csjbot.blackgaga.customer_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.service.BaseService;
import com.csjbot.blackgaga.util.VolumeUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.listener.OnCustomServiceMsgListener;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerHelpService extends BaseService implements OnCustomServiceMsgListener {
    private IComplexActionWorker worker;
    private Map<String, Runnable> actionFromCMD = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        RobotManager.getInstance().addListener(this);
        worker = new ComplexActionWorker2();

        initMap();

        registerReceiver(tempReceiver, new IntentFilter("CustomerHelpService"));
    }

    private void initMap() {
        actionFromCMD.put("ROBOT_ACTION_NOD", ROBOT_ACTION_NOD);
        actionFromCMD.put("ROBOT_ACTION_SHAKE_HEAD", ROBOT_ACTION_SHAKE_HEAD);
        actionFromCMD.put("ROBOT_ACTION_WELCOME", ROBOT_ACTION_WELCOME);
        actionFromCMD.put("ROBOT_ACTION_UP_DOWN_LEFT_ARM", ROBOT_ACTION_UP_DOWN_LEFT_ARM);
        actionFromCMD.put("ROBOT_ACTION_UP_DOWN_RIGHT_ARM", ROBOT_ACTION_UP_DOWN_RIGHT_ARM);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    BroadcastReceiver tempReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("json");

            onMsg(json);
        }
    };

    @Override
    public void onMsg(String json) {
        JSONObject jsonObject = null;
        String msgId;
        try {
            jsonObject = new JSONObject(json);
            msgId = jsonObject.getString("msg_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (TextUtils.isEmpty(msgId)) {
            return;
        }

        switch (msgId) {
            case NTFConstants.ROBOT_COMPLEX_ACTION_NTF:
                parseComplexAction(jsonObject);
                break;
            case NTFConstants.ROBOT_SET_VOLUME_NTF:
                try {
                    int volume = jsonObject.getInt("volume");
                    setVolume(volume);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        CsjlogProxy.getInstance().debug("RbOther default " + json);
    }

    private void parseComplexAction(JSONObject jsonObject) {
        String action;
        try {
            action = jsonObject.getString("action");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Runnable job = actionFromCMD.get(action);
        if (job != null) {
            pushJob(job);
        } else {
            CsjlogProxy.getInstance().warn("未知的动作 {}", action);
        }
    }

    private synchronized void pushJob(Runnable job) {
        if (!worker.pushJob(job)) {
            CsjlogProxy.getInstance().warn("worker is busy !");
        }
    }

    private Runnable ROBOT_ACTION_WELCOME = () -> {
        //机器人点头示意，伸右手，语音播报“欢迎光临
        Robot.getInstance().AliceHeadDown();
        Robot.getInstance().AliceRightArmUp();
        Robot.getInstance().startSpeaking(getResources().getString(R.string.hello_welcome), null);
        CsjlogProxy.getInstance().debug("低头、抬右手、说您好");


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Robot.getInstance().AliceHeadUp();
        Robot.getInstance().AliceRightArmDown();
        CsjlogProxy.getInstance().debug("抬头、放右手");
    };

    /**
     * 抬放左手一次
     */
    private Runnable ROBOT_ACTION_UP_DOWN_LEFT_ARM = () -> {
        Robot.getInstance().AliceLeftArmUp();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Robot.getInstance().AliceLeftArmDown();
    };

    /**
     * 抬放右手一次
     */
    private Runnable ROBOT_ACTION_UP_DOWN_RIGHT_ARM = () -> {
        Robot.getInstance().AliceRightArmUp();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Robot.getInstance().AliceRightArmDown();
    };

    /**
     * 摇头
     */
    private Runnable ROBOT_ACTION_SHAKE_HEAD = () -> {
        Robot.getInstance().AliceHeadLeft();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Robot.getInstance().AliceHeadRight();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.getInstance().AliceHeadHReset();
    };


    /**
     * 点头
     */
    private Runnable ROBOT_ACTION_NOD = () -> {
        Robot.getInstance().AliceHeadDown();
        CsjlogProxy.getInstance().debug("低头");


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Robot.getInstance().AliceHeadUp();
        CsjlogProxy.getInstance().debug("抬头");
    };


    private void setVolume(int volume) {
        VolumeUtil.setMediaVolume(this, volume);
    }
}
