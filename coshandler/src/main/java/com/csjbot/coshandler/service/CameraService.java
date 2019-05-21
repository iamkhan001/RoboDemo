package com.csjbot.coshandler.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.csjbot.cameraclient.CameraClientAgent;
import com.csjbot.cameraclient.constant.ClientConstant;
import com.csjbot.cameraclient.entity.PicturePacket;
import com.csjbot.cameraclient.listener.CameraEventListener;
import com.csjbot.cameraclient.listener.ClientEvent;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.ConnectConstants;
import com.csjbot.coshandler.log.CsjlogProxy;

public class CameraService extends Service implements CameraEventListener {

    private CameraClientAgent clientAgent;

    public CameraService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        CsjlogProxy.getInstance().debug("class:CameraService method:onCreate");
        connect();
    }

    private void connect() {
        clientAgent = CameraClientAgent.createRosClientAgent(this, false);
        clientAgent.connect(ConnectConstants.serverIp, ConnectConstants.serverCaeraPort);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCameraEvent(ClientEvent event) {
        switch (event.eventType) {
            case ClientConstant.EVENT_PACKET:
                PicturePacket packet = (PicturePacket) event.data;
                byte[] bitSrc = packet.getContent();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bitSrc, 0, bitSrc.length);
                if(bitmap != null) {
                    Robot.getInstance().pushCamera(bitmap);
                }
                break;
            case ClientConstant.DUPLICATE_PIC_ERROR:
                // TODO: 2017/11/16 0016 add pu， 当重复发一个图片5次会报这个错误，说明底下已经不行了

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (clientAgent != null && clientAgent.isConnected()) {
            clientAgent.disConnect();
        }
    }
}
