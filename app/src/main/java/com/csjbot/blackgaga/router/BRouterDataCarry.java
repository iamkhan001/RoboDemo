package com.csjbot.blackgaga.router;

import android.os.Parcelable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;

import static android.R.attr.value;

/**
 * Created by xiasuhuei321 on 2017/11/28.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BRouterDataCarry {
    private Postcard postcard;

    public BRouterDataCarry(String url) {
        postcard = ARouter.getInstance().build(url);
    }

    public BRouterDataCarry withString(String key, String value) {
        postcard.withString(key, value);
        return this;
    }

    public BRouterDataCarry withSerializable(String key, Serializable serializable) {
        postcard.withSerializable(key, value);
        return this;
    }

    public BRouterDataCarry withParcelable(String key, Parcelable parcel) {
        postcard.withParcelable(key, parcel);
        return this;
    }

    public BRouterDataCarry withShort(String key, short value) {
        postcard.withShort(key, value);
        return this;
    }

    public BRouterDataCarry withInt(String key, int value) {
        postcard.withInt(key, value);
        return this;
    }
    
    public BRouterDataCarry withLong(String key, long value) {
        postcard.withLong(key, value);
        return this;
    }

    public BRouterDataCarry withDouble(String key, double value) {
        postcard.withDouble(key, value);
        return this;
    }

    public BRouterDataCarry withFloat(String key, float value) {
        postcard.withFloat(key, value);
        return this;
    }

    public BRouterDataCarry withBoolean(String key, boolean value) {
        postcard.withBoolean(key, value);
        return this;
    }

    public BRouterDataCarry withByte(String key, byte value) {
        postcard.withByte(key, value);
        return this;
    }

    public BRouterDataCarry withChar(String key, char value) {
        postcard.withChar(key, value);
        return this;
    }

    public BRouterDataCarry withCharSequence(String key, CharSequence value) {
        postcard.withCharSequence(key, value);
        return this;
    }

    public BRouterDataCarry withFlags(int flags){
        postcard.withFlags(flags);
        return this;
    }

    public Postcard get(){
        return postcard;
    }

    public void navigation() {
        postcard.navigation();
    }
}
