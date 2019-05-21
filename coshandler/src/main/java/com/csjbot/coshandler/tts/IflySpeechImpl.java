package com.csjbot.coshandler.tts;

import android.content.Context;
import android.os.Bundle;

import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 讯飞语音合成具体实现类
 * Created by jingwc on 2017/9/12.
 */

public class IflySpeechImpl implements ISpeechSpeak {


    // 语音合成对象
    private SpeechSynthesizer mSpeechSynthesizer;

    /**
     * 创建实例
     *
     * @param context
     * @return
     */
    public static IflySpeechImpl newInstance(Context context) {
        return new IflySpeechImpl(context, new InitListener() {
            @Override
            public void onInit(int i) {
                CosLogger.debug("CosSpeechSynthesizer:init");
            }

        });
    }

    /**
     * 私有有参构造
     *
     * @param ctx
     * @param initListener
     */
    private IflySpeechImpl(Context ctx, InitListener initListener) {
        WeakReference<Context> mContext = new WeakReference<>(ctx.getApplicationContext());
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext.get(), initListener);
        setParamsLocal("jiajia");
    }

    public IflySpeechImpl(Context ctx, InitListener initListener, String name) {
        WeakReference<Context> mContext = new WeakReference<>(ctx.getApplicationContext());
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext.get(), initListener);
        setParams(name);
    }

    /**
     * 设置合并语音参数
     */
    public void setParamsLocal(String name) {
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        //设置云端合成引擎
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置在线合成发音人:xiaoyan
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, name);
        //设置合成语速:50
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "60");
        //设置合成音调:50
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "70");
        //设置合成音量:50
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
    }

    public void setParams(String name) {
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        //设置云端合成引擎
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人:xiaoyan
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, name);
        //设置合成语速:50
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "60");
        //设置合成音调:50
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "70");
        //设置合成音量:50
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
    }

    /**
     * 开始说话
     *
     * @param text
     * @param listener
     */
    @Override
    public void startSpeaking(String text, OnSpeakListener listener) {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.startSpeaking(text, new DefaultSynthesizerListener(listener));
        }
    }

    /**
     * 停止说话
     */
    @Override
    public void stopSpeaking() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    /**
     * 暂停说话
     */
    @Override
    public void pauseSpeaking() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.pauseSpeaking();
        }
    }

    /**
     * 重新说话
     */
    @Override
    public void resumeSpeaking() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.resumeSpeaking();
        }
    }

    /**
     * 是否在说话
     *
     * @return
     */
    @Override
    public boolean isSpeaking() {
        return mSpeechSynthesizer != null ? mSpeechSynthesizer.isSpeaking() : false;
    }

    @Override
    public boolean setSpeakerName(String name) {
        return mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, name);
    }

    @Override
    public boolean setLanguage(Locale language) {
        return false;
    }

    @Override
    public ArrayList<String> getSpeakerNames(String language, String country) {
        return null;
    }

    @Override
    public void setVolume(float volume) {
        volume = volume * 100;
        CsjlogProxy.getInstance().info("ifly:volume:"+volume);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, String.valueOf(volume));
    }


    private static class DefaultSynthesizerListener implements SynthesizerListener {

        private OnSpeakListener speakListener;

        public DefaultSynthesizerListener() {

        }

        public DefaultSynthesizerListener(OnSpeakListener speakListener) {
            this.speakListener = speakListener;
        }

        @Override
        public void onSpeakBegin() {
            if (speakListener != null) {
                speakListener.onSpeakBegin();
            }
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
            if (speakListener != null) {
                speakListener.onCompleted(speechError);
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
}
