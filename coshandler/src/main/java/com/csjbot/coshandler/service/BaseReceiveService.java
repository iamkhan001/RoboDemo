package com.csjbot.coshandler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.csjbot.cosclient.CosClientAgent;
import com.csjbot.cosclient.constant.ClientConstant;
import com.csjbot.cosclient.entity.CommonPacket;
import com.csjbot.cosclient.entity.MessagePacket;
import com.csjbot.cosclient.listener.ClientEvent;
import com.csjbot.cosclient.listener.EventListener;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.global.ConnectConstants;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * 负责和底层通信的service
 * Created by jingwc on 2017/8/11.
 */

public abstract class BaseReceiveService extends Service implements EventListener {

    /**
     * 底层通信对外接口
     */
    private CosClientAgent mAgent;

    private SparseArray<String> testJsons = new SparseArray<>();

    private String data;

    private long timestamp;

    @Override
    public void onCreate() {
        super.onCreate();
        CsjlogProxy.getInstance().debug("class:BaseReceiveService method:onCreate");
        connect();

        // TODO: 2018/03/27 0027   puyz add to test, 其他人看到了都请删了
        {
            testJsons.put(1, "{\"msg_id\":\"GET_HARDWARE_INFO_RSP\",\"sid\":1521716485000,\"version\":\"HARDWARE_V1.0.0\"}");
            testJsons.put(2, "{\"msg_id\":\"GET_HARDWARE_INFO_RSP\",\"sid\":1521716485000,\"version\":\"HARDWARE_V2.0.0\"}");

            registerReceiver(TestReceiver, new IntentFilter("puyz"));
        }
    }

    /**
     * 与底层建立连接
     */
    private void connect() {
        mAgent = CosClientAgent.createRosClientAgent(this, true);
        if (mAgent.isConnected()) {
            mAgent.disConnect();
        }
        mAgent.connect(ConnectConstants.serverIp, ConnectConstants.serverPort);

    }

    /**
     * 接受底层消息.并分发下去
     *
     * @param event
     */
    @Override
    public void onEvent(ClientEvent event) {
        switch (event.eventType) {
            case ClientConstant.EVENT_RECONNECTED:
            case ClientConstant.EVENT_CONNECT_SUCCESS:
                CsjlogProxy.getInstance().debug("EVENT_CONNECT_SUCCESS");
                connectStatus(ClientConstant.EVENT_CONNECT_SUCCESS);
                break;
            case ClientConstant.EVENT_CONNECT_FAILD:
                connectStatus(ClientConstant.EVENT_CONNECT_FAILD);
                CsjlogProxy.getInstance().error("EVENT_CONNECT_FAILD" + String.valueOf(event.data));
                break;
            case ClientConstant.EVENT_CONNECT_TIME_OUT:
                connectStatus(ClientConstant.EVENT_CONNECT_TIME_OUT);
                CsjlogProxy.getInstance().error("EVENT_CONNECT_TIME_OUT  " + String.valueOf(event.data));
                break;
            case ClientConstant.SEND_FAILED:
                sendFailed();
                CsjlogProxy.getInstance().error("SEND_FAILED");
                break;
            case ClientConstant.EVENT_DISCONNET:
                connectStatus(ClientConstant.EVENT_DISCONNET);
                CsjlogProxy.getInstance().error("EVENT_DISCONNET");
                break;
            case ClientConstant.EVENT_PACKET:
                MessagePacket packet = (MessagePacket) event.data;
//                CosLogger.warn("rec packet");
                CosLogger.warn(((CommonPacket) packet).getContentJson());

                // 数据去重
                String tempData = new String(packet.getContent());
                long tempTimestamp = System.currentTimeMillis();
                if(tempData.equals(data) && (tempTimestamp - timestamp) < 100){
                    // 如果100毫秒内接受两条相同数据,则丢弃第二条
                    return;
                }
                data = tempData;
                timestamp = tempTimestamp;

                handleMessage(new String(packet.getContent()));
                break;
            default:
                break;
        }
    }


    /**
     * 浦耀宗测试使用
     * TODO: 2018/03/27 0027   puyz add to test, 其他人看到了都请删了
     */
    BroadcastReceiver TestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String json = intent.getStringExtra("json");
            String json = testJsons.get(intent.getIntExtra("key", 1));
            CosLogger.warn(json);
            handleMessage(json);
        }
    };


    /**
     * 处理消息
     *
     * @param json
     */
    protected abstract void handleMessage(String json);

    /**
     * 连接状态
     *
     * @param type
     */
    protected abstract void connectStatus(int type);

    /**
     * 发送失败
     */
    protected abstract void sendFailed();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAgent != null && mAgent.isConnected()) {
            mAgent.disConnect();
        }
    }
}
