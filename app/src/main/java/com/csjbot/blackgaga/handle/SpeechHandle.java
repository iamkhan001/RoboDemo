package com.csjbot.blackgaga.handle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.csjbot.blackgaga.feature.consult.ConsultActivity;
import com.csjbot.blackgaga.service.HomeService;

/**
 * 语音消息处理类
 * Created by jingwc on 2017/9/19.
 */

public class SpeechHandle{

    private static class SpeechHolder{
        private static final SpeechHandle INSTANCE = new SpeechHandle();
    }

    public static final SpeechHandle getInstance(){
        return SpeechHolder.INSTANCE;
    }

    private SpeechHandle(){

    }

    /**
     * 接受语音消息方法
     * @param json
     */
    public void onReceive(Context context,String json){
        String text = "";
        if(text.contains("咨询")){
            /* 启动咨询页面 */
            sendStartActMsg(context,ConsultActivity.class.getName());
        }
    }

    /**
     * 发送启动activity的广播
     * @param context
     * @param className
     */
    public void sendStartActMsg(Context context,String className){
        Intent intent = new Intent(HomeService.HomeBroadcast.ACTION_NAME);
        Bundle bundle = new Bundle();
        bundle.putInt(HomeService.HomeBroadcast.TYPE,HomeService.START_ACTIVITY);
        bundle.putString(HomeService.HomeBroadcast.VALUE, className);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }
}
