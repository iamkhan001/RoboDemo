package com.csjbot.blackgaga.jpush.diaptch;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.csjbot.blackgaga.jpush.diaptch.event.ExpressionEvent;
import com.csjbot.blackgaga.jpush.diaptch.event.GlobalNoticeEvent;
import com.csjbot.blackgaga.jpush.diaptch.event.SceneEvent;
import com.csjbot.blackgaga.jpush.diaptch.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 推送消息分发总控
 * Created by jingwc on 2018/3/2.
 */

public class CsjPushDispatch {

    private volatile static CsjPushDispatch csjPushDispatch;

    public static CsjPushDispatch getInstance() {
        if (csjPushDispatch == null) {
            synchronized (CsjPushDispatch.class) {
                if (csjPushDispatch == null) {
                    csjPushDispatch = new CsjPushDispatch();
                }
            }
        }
        return csjPushDispatch;
    }


    /**
     * 线程池
     */
    private ExecutorService executorService;

    private CsjPushDispatch() {
        // 实例化线程池
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
    }

    // 对推送来的消息进行归类处理
    public void execute(String json){
        if(TextUtils.isEmpty(json)){
            return;
        }
        MessageHandler handler = new MessageHandler(json);
        executorService.execute(handler);
    }

    // 全局公告事件集合
    private List<GlobalNoticeEvent> globalNoticeEvents = new ArrayList<>();

    public void addGlobalNoticeEvent(GlobalNoticeEvent event){
        globalNoticeEvents.add(event);
    }

    public void removeGlobalNoticeEvent(GlobalNoticeEvent event){
        globalNoticeEvents.remove(event);
    }

    public void pushGlobalNoticeMessage(String id,int sid, String data){
        for (GlobalNoticeEvent event : globalNoticeEvents){
            new Handler(Looper.getMainLooper()).post(()->{
                event.pushEvent(id,sid,data);
            });

        }
    }

    // 表情事件集合
    private List<ExpressionEvent> expressionEvents = new ArrayList<>();

    public void addExpressionEvent(ExpressionEvent event){
        expressionEvents.add(event);
    }

    public void removeExpressionEvent(ExpressionEvent event){
        expressionEvents.remove(event);
    }

    public void pushExpressionMessage(String id,int sid, String data){
        for (ExpressionEvent event : expressionEvents){
            new Handler(Looper.getMainLooper()).post(()->{
                event.pushEvent(id,sid,data);
            });

        }
    }


    // 全局公告事件集合
    private List<SceneEvent> sceneEvents = new ArrayList<>();

    public void addSceneEvent(SceneEvent event){
        sceneEvents.add(event);
    }

    public void removeSceneEvent(SceneEvent event){
        sceneEvents.remove(event);
    }

    public void pushSceneMessage(String id,int sid, String data){
        for (SceneEvent event : sceneEvents){
            new Handler(Looper.getMainLooper()).post(()->{
                event.pushEvent(id,sid,data);
            });

        }
    }

}
