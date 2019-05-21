package com.csjbot.blackgaga.util;

import android.media.MediaPlayer;
import android.text.TextUtils;

/**
 * Created by jingwc on 2017/7/20.
 */

public class AudioUtil {

    private MediaPlayer mediaPlayer = null;

    public AudioUtil() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void play(String path, PlayCompleteListener playCompleteListener) {
        if(TextUtils.isEmpty(path)){
            return;
        }
        if (mediaPlayer != null) {
            try {
                //重用MediaPlayer对象
                mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                    if(playCompleteListener != null) {
                        playCompleteListener.complete();
                    }
                });
                mediaPlayer.setDataSource (path);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void restart() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface PlayCompleteListener {
        void complete();
    }
}
