package com.csjbot.blackgaga.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by 孙秀艳 on 2017/11/8.
 */

public class TonePlayer {
    private static final String TAG = "TonePlayer";

    private static MediaPlayer player;

    /**
     * 如需要取消播放，直接调用返回的MediaPlayer.release()
     */
    public static MediaPlayer play(Context ctx, int resId, Runnable completionRun) {
        MediaPlayer player = MediaPlayer.create(ctx, resId);
        playInternal(player, completionRun);
        return player;
    }

    /**
     * 如需要取消播放，直接调用返回的MediaPlayer.release()
     */
    public static MediaPlayer play(Context ctx, String assetsFileName, Runnable completionRun) {
        MediaPlayer player = createMediaPlayer(ctx, assetsFileName);
        playInternal(player, completionRun);
        return player;
    }

    public static MediaPlayer play(Context ctx, int resId) {
        MediaPlayer player = MediaPlayer.create(ctx, resId);
        playInternal(player, null);
        return player;
    }

    public static MediaPlayer play(Context ctx, String assetsFileName) {
        MediaPlayer player = createMediaPlayer(ctx, assetsFileName);
        playInternal(player, null);
        return player;
    }

    public static void playUseSoundPool(Context context, final String path, final int looptimes) {
        try {
            if (null == context) {
                return;
            }

            final SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            AssetFileDescriptor afd = context.getAssets().openFd(path);
            final int id = soundpool.load(afd, 1);

            if (null != afd) {
                afd.close();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    soundpool.play(id, 1, 1, 0, looptimes, 1);
                }
            }).start();

//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    soundpool.play(id, 1, 1, 0, looptimes, 1);
//                }
//            }, 1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void playInternal(final MediaPlayer player, final Runnable completionRun) {
        if (player == null) { // 播放错误或失败时继续执行下面的操作，避免卡住
            if (completionRun != null) {
                completionRun.run();
            }
            return;
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (completionRun != null) {
                    completionRun.run();
                }
                mp.release();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                BlackgagaLogger.debug(TAG, "Error=" + what + ",extra=" + extra);
                player.start();
                return true;
            }
        });
        player.start();
    }

    private static MediaPlayer createMediaPlayer(Context context, String assetFileName) {
        MediaPlayer mp = new MediaPlayer();
        try {
            if (assetFileName.contains("/AIUIProductDemo/")) {
                mp.setDataSource(assetFileName);
                mp.prepare();
                return mp;
            } else if (assetFileName.contains("http")) {
                mp.setDataSource(assetFileName);
                mp.prepare();
                return mp;
            } else {
                AssetFileDescriptor afd = context.getAssets().openFd(assetFileName);
                if (afd == null) {
                    return null;
                }
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mp.prepare();
                return mp;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            mp.release();
        }
        return null;
    }

    public static void playTone(Context context, String assetFileName) {
        player = new MediaPlayer();
        try {
            synchronized (TonePlayer.class) {
                if (!TextUtils.isEmpty(assetFileName)) {
                    AssetFileDescriptor afd = context.getAssets().openFd(assetFileName);
                    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                    if (null != afd) {
                        afd.close();
                    }

                    player.prepare();
                    player.start();
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            mp = null;
                            player = null;
                            BlackgagaLogger.debug(TAG, "release");
                        }
                    });
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static void stopPlay() {
        synchronized (TonePlayer.class) {
            if (null != player) {
                player.stop();
            }
        }
    }

}
