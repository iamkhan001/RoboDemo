package com.csjbot.blackgaga.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.csjbot.blackgaga.event.BusFactory;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.lang.ref.WeakReference;

/**
 * Created by xiasuhuei321 on 2017/8/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BggSpeechSynthesizer {
    private static BggSpeechSynthesizer ourInstance;
    // 语音合成对象
    private SpeechSynthesizer mSpeechSynthesizer;

    private BggSpeechSynthesizer(Context ctx, InitListener initListener) {
        WeakReference<Context> mContext = new WeakReference<>(ctx.getApplicationContext());
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext.get(), initListener);
        setParams();
    }

    public static BggSpeechSynthesizer createSynthesizer(@NonNull Context ctx, InitListener initListener) {
        if (ourInstance == null) {
            try {
                ourInstance = new BggSpeechSynthesizer(ctx, initListener);
            } catch (Exception e) {
                BlackgagaLogger.error(e);
            }
        }

        return ourInstance;
    }

    public static BggSpeechSynthesizer getSynthesizer() {
        return ourInstance;
    }

    public void setParams() {
        BlackgagaLogger.debug("setup Synthesizer");
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        //设置云端合成引擎
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置在线合成发音人:xiaoyan
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "jiajia");
        //设置合成语速:50
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "60");
        //设置合成音调:50
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "70");
        //设置合成音量:50
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
    }

    public int startSpeaking(String string, SynthesizerListener synthesizerListener) {

//        if (SharedUtil.getPreferInt(SharedKey.TTSSWITCH,1) == 0){
//            return -1;
//        }
        if (synthesizerListener == null) {
            synthesizerListener = new DefaultSynthesizerListener();
        }

        return mSpeechSynthesizer.startSpeaking(string, synthesizerListener);
    }


    public void setParameter(String var1, String var2) {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.setParameter(var1, var2);
        }
    }

    public boolean isSpeaking() {
        if (null != mSpeechSynthesizer) {
            return mSpeechSynthesizer.isSpeaking();
        }

        return false;
    }

    public void stopSpeaking() {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    public void pauseSpeaking() {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.pauseSpeaking();
        }
    }

    public void resumeSpeaking() {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.resumeSpeaking();
        }
    }

    public void destroy() {
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.destroy();
        }
        BusFactory.getBus().unregister(this);
    }

    private class DefaultSynthesizerListener implements SynthesizerListener {
        @Override
        public void onSpeakBegin() {
//            BusFactory.getBus().post(new ExpressionEvent(Constant.Expression.EXPRESSION_SPEAK));
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
//            BusFactory.getBus().post(new ExpressionEvent(Constant.Expression.EXPRESSION_NORMAL));
        }


    }


    public abstract static class BggSynthesizerListener implements SynthesizerListener {
        @Override
        public abstract void onSpeakBegin();

        @Override
        public abstract void onCompleted(SpeechError speechError);

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }


    }
}
