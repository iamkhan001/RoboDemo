package com.csjbot.blackgaga.jpush.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.csjbot.blackgaga.jpush.constant.JPushConstant;
import com.csjbot.blackgaga.jpush.diaptch.CsjPushDispatch;
import com.csjbot.blackgaga.jpush.event.JPushEvent;
import com.csjbot.blackgaga.jpush.event.JPushMessageEvent;
import com.csjbot.blackgaga.jpush.event.JPushTagsAliasEvent;
import com.csjbot.blackgaga.jpush.listener.JPushEventListener;
import com.csjbot.blackgaga.jpush.manager.CsjJPushManager;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;


/**
 * @author Ben
 * @date 2018/2/8
 */

public class GetMessageService extends Service implements JPushEventListener {

    @Override
    public void onCreate() {
        super.onCreate();
        CsjJPushManager.getInstance().setJPushEventListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onEvent(JPushEvent event) {
        switch (event.getEventType()) {
            case JPushConstant.JPUSH_MESSAGE:
                JPushMessageEvent messageEvent = (JPushMessageEvent) event.getData();
                switch (messageEvent.getType()) {
                    case JPushConstant.ACTION_REGISTRATION_ID:
                        CsjlogProxy.getInstance().info("ACTION_REGISTRATION_ID:" + messageEvent.getMsgContent());
                        break;
                    case JPushConstant.ACTION_MESSAGE_RECEIVED:
                        CsjlogProxy.getInstance().info("ACTION_MESSAGE_RECEIVED:" + messageEvent.getMsgContent());
                        CsjPushDispatch.getInstance().execute(messageEvent.getMsgContent());
                        break;
                    default:
                        break;
                }
                break;
            case JPushConstant.JPUSH_TAGS_ALIAS:
                JPushTagsAliasEvent tagsAliasEvent = (JPushTagsAliasEvent) event.getData();
                switch (tagsAliasEvent.getType()) {
                    case JPushConstant.onTagOperatorResult:
                        if (tagsAliasEvent.getMessage().getErrorCode() == 0) {
                            CsjlogProxy.getInstance().info("成功");
                        }
                        CsjlogProxy.getInstance().info("onTagOperatorResult: " + tagsAliasEvent.getMessage());
                        break;
                    case JPushConstant.onCheckTagOperatorResult:
                        if (tagsAliasEvent.getMessage().getErrorCode() == 0) {
                            CsjlogProxy.getInstance().info("成功");
                        }
                        CsjlogProxy.getInstance().info("onCheckTagOperatorResult: " + tagsAliasEvent.getMessage());
                        break;
                    case JPushConstant.onAliasOperatorResult:
                        if (tagsAliasEvent.getMessage().getErrorCode() == 0) {
                            CsjlogProxy.getInstance().info("成功");
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> CsjJPushManager.getInstance().setAlias((int) System.currentTimeMillis(), Robot.SN),5000);
                        }
                        CsjlogProxy.getInstance().info("onAliasOperatorResult: " + tagsAliasEvent.getMessage());
                        break;
                    case JPushConstant.onMobileNumberOperatorResult:
                        if (tagsAliasEvent.getMessage().getErrorCode() == 0) {
                            CsjlogProxy.getInstance().info("成功");
                        }
                        CsjlogProxy.getInstance().info("onMobileNumberOperatorResult: " + tagsAliasEvent.getMessage());
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
