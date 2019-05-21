package com.csjbot.blackgaga.manual_control.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.manual_control.constants.ConnectConstants;
import com.csjbot.blackgaga.manual_control.handler.MessageServerHandler;
import com.csjbot.blackgaga.manual_control.handler.MsgRecListener;
import com.csjbot.blackgaga.manual_control.server.MobileServer;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.cosclient.CosClientAgent;
import com.csjbot.cosclient.entity.CommonPacket;
import com.csjbot.cosclient.entity.MessagePacket;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ShenBen
 * @date 2018/11/17 11:18
 * @email 714081644@qq.com
 */

public class ServerService extends Service implements MsgRecListener, OnPositionListener {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileServer.getInstace().init();
        Robot.getInstance().registerPositionListener(this);
        MessageServerHandler.setMsgRecListener(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Robot.getInstance().unregisterPositionListener(this);
    }

    @Override
    public void messageRec(String msg) {
        CsjlogProxy.getInstance().debug(ConnectConstants.TAG + msg);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        try {
            JSONObject object = new JSONObject(msg);
            if (object.has("msg_id")) {
                String msg_id = object.getString("msg_id");
                switch (msg_id) {
                    case "USER_SPEECH"://说话
                        String content = object.getString("content");
                        if (!TextUtils.isEmpty(content)) {
                            ServerFactory.getSpeakInstance().startSpeaking(content, null);
                        }
                        break;
                    case "NAVI_ROBOT_MOVE_REQ"://前后左右
                        int direction;
                        direction = object.getInt("direction");
                        if (direction != -1) {
                            ServerFactory.getChassisInstance().move(direction);
                        }
                        break;
                    case "NAVI_GET_CURPOS_REQ"://获取当前点的坐标
                        ServerFactory.getChassisInstance().getPosition();
                        break;
                    case "NAVI_ROBOT_MOVE_TO_REQ"://移动至某个点
                        ServerFactory.getChassisInstance().navi(object.getString("pos"));
                        break;
                    case "SET_ROBOT_EXPRESSION_REQ"://设置机器人表情
                        int expression = object.getInt("expression");
                        int once = object.getInt("once");
                        int time = object.getInt("time");
                        RobotManager.getInstance().robot.reqProxy.setExpression(expression, once, time);
                        break;
                    case "ROBOT_BODY_CTRL_CMD"://机器人肢体操作
                        int body_part = object.getInt("body_part");
                        int action = object.getInt("action");
                        if (body_part == 2 && action == 7) {//点头
                            ServerFactory.getActionInstance().nodAction();
                        } else {
                            RobotManager.getInstance().robot.reqProxy.action(body_part, action);
                        }
                        break;
                    case "REQ_HUMAN_INTERVENTI": {//人工客服主动介入
                        String json = "{\"msg_id\":\"REQ_HUMAN_INTERVENTI\",\"sid\":" + System.currentTimeMillis() +
                                ",\"sn\":\"" + Robot.SN + "\"}";
                        MessagePacket packet = new CommonPacket(json.getBytes());
                        CosClientAgent.getRosClientAgent().sendMessage(packet);
                        break;
                    }
                    case "REQ_HANGUP_HUMAN_INTERVENTI": {//人工客服取消介入
                        String json = "{\"msg_id\":\"REQ_HANGUP_HUMAN_INTERVENTI\",\"sid\":" + System.currentTimeMillis() +
                                ",\"sn\":\"" + Robot.SN + "\"}";
                        MessagePacket packet = new CommonPacket(json.getBytes());
                        CosClientAgent.getRosClientAgent().sendMessage(packet);
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void positionInfo(String json) {
        if (MessageServerHandler.sIsConnected) {
            MobileServer.getInstace().setCmd(json);
            CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "positionInfo: " + json);
        }

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
