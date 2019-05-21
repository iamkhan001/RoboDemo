package com.csjbot.blackgaga.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.csjbot.blackgaga.event.BusFactory;
import com.csjbot.blackgaga.event.IBus;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * service基类
 * Created by jingwc on 2017/8/14.
 */

public abstract class BaseService extends Service {
    protected static IBus ibus = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CsjlogProxy.getInstance().info("Service-Started [{}], using eventbus {}", this.getClass().getSimpleName(), useEventBus());

        if (useEventBus()) {
            ibus = BusFactory.getBus();
            ibus.register(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ibus != null) {
            ibus.unregister(this);
        }
        super.onDestroy();
    }

    public boolean useEventBus() {
        return false;
    }

    public void postEvent(IBus.IEvent event) {
        if (ibus != null) {
            ibus.post(event);
        }
    }

    public void postSticky(IBus.IEvent event) {
        if (ibus != null) {
            ibus.postSticky(event);
        }
    }
}
