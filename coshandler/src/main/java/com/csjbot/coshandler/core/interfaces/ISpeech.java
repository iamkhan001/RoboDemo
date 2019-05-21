package com.csjbot.coshandler.core.interfaces;

import android.content.Context;

import com.csjbot.coshandler.tts.ISpeechSpeak;

/**
 * Created by jingwc on 2017/9/5.
 */

public interface ISpeech extends ISpeechSpeak {
    void initSpeak(Context context, int speechType);
}
