package com.csjbot.blackgaga.advertisement.factory;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by Ben on 2018/3/23.
 */

public class ProxyFactory {

    private static HttpProxyCacheServer sharedProxy;

    private ProxyFactory() {
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        return sharedProxy == null ? (sharedProxy = newProxy(context)) : sharedProxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(1024 * 1024 * 100)
                .maxCacheFilesCount(10)
                .build();
    }
}
