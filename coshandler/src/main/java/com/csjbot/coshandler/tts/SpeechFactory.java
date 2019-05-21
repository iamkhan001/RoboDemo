package com.csjbot.coshandler.tts;

import android.content.Context;

/**
 * 语音合成工厂类
 * Created by jingwc on 2017/9/12.
 */

public class SpeechFactory {
    private static ISpeechSpeak speechSpeak = null;

    /**
     * 获取一个语音合成提供类
     *
     * @param context
     * @param type
     * @return
     */
    public static ISpeechSpeak createSpeech(Context context, int type) {
        switch (type) {
            case SpeechType.IFLY:
                speechSpeak = getIflySpeech(context);
                break;
            case SpeechType.JAPAN:
                speechSpeak = getJapanSpeech(context);
                break;
            case SpeechType.JAPAN_SNOW:
                speechSpeak = getJapanSpeechSnow(context);
                break;
            case SpeechType.GOOGLE:
            default:
                speechSpeak = getGoogleSpeech(context);
                break;
        }
        return speechSpeak;
    }


    public void setSpeakerName(String speakerName) {
        speechSpeak.setSpeakerName(speakerName);
    }


    /**
     * 获取讯飞语音合成实现
     *
     * @param context
     * @return
     */
    private static ISpeechSpeak getIflySpeech(Context context) {
        return IflySpeechImpl.newInstance(context);
    }

    private static ISpeechSpeak getJapanSpeech(Context context) {
        return JapanSpeechImpl.newInstance(context);
    }

    private static ISpeechSpeak getJapanSpeechSnow(Context context) {
        return JapanSpeechSnowImpl.newInstance(context);
    }

    private static ISpeechSpeak getGoogleSpeech(Context context) {
        return GoogleSpechImpl.newInstance(context);
    }


    /**
     * 语音合成标识类(可用于标识使用哪家第三方的语音合成)
     */
    public static final class SpeechType {

        /* 科大讯飞 */
        public static final int IFLY = 0;

        /* 日语 */
        public static final int JAPAN = 1;

        /* 日语 小雪 */
        public static final int JAPAN_SNOW = 2;

        /* Google tts */
        public static final int GOOGLE = 3;
    }
}
