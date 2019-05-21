package com.csjbot.blackgaga.util;

/**
 * Created by 孙秀艳 on 2017/10/19.
 */

import android.content.Context;
import android.media.AudioManager;

/**
 * 音量控制类
 */
public class VolumeUtil {
    static AudioManager audioManager;

    public static int addMediaVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        BlackgagaLogger.debug("VolumeUtil addMediaVolume" + volume);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (volume + 1 >= maxVolume) {
            volume = maxVolume;
        } else {
            volume = volume + 1;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        SharedPreUtil.putInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, volume);
        return volume;
    }

    public static boolean setMediaVolume(Context ctx, int volume) {
        Context context = ctx.getApplicationContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null) {
            return false;
        }

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        BlackgagaLogger.debug("maxVolume " + maxVolume);

        if (volume > maxVolume) {
            volume = maxVolume;
        }

        if (volume < 0) {
            volume = 0;
        }

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        return true;
    }

    public static int cutMediaVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volumeSub = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        BlackgagaLogger.debug("VolumeUtil cutMediaVolume" + volumeSub);
        if (volumeSub - 1 <= 0) {
            volumeSub = 0;
        } else {
            volumeSub = volumeSub - 1;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeSub, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        SharedPreUtil.putInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, volumeSub);
        return volumeSub;
    }
}
