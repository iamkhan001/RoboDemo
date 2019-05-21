package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.csjbot.blackgaga.R;

import java.io.IOException;

/**
 * Created by jingwc on 2018/2/27.
 */

public class PlusVideoView extends FrameLayout {

    Context mContext;

    SurfaceView mSurfaceview;

    MediaPlayer mMediaPlayer;

    SurfaceHolder mSurfaceHolder;

    public PlusVideoView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlusVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlusVideoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.layout_plus_video, this, true);

        mSurfaceview = findViewById(R.id.surfaceview);

        mMediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/raw/" + R.raw.vertical_default);
        try {
            mMediaPlayer.setDataSource(mContext,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSurfaceHolder = mSurfaceview.getHolder();


        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if(mMediaPlayer != null){
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
            }
        });

        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    private void initVideo(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
            mMediaPlayer.start();
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mSurfaceHolder.setKeepScreenOn(true);
        });
//        mMediaPlayer.setOnErrorListener(null);
//        mMediaPlayer.setOnBufferingUpdateListener(null);
        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/raw/" + R.raw.vertical_default);
        try {
            mMediaPlayer.setDataSource(mContext,uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playVideo(Uri uri) {
        try {
            mMediaPlayer.setDataSource(mContext,uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
