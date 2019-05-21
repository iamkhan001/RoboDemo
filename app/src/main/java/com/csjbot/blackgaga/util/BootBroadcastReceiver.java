package com.csjbot.blackgaga.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.csjbot.blackgaga.SplashActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, false);
            SharedPreUtil.putInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 0);//冷启动
            Intent intent1 = new Intent(context, SplashActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}