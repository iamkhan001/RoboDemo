package com.csjbot.blackgaga.robot_test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.cameraclient.CameraClientAgent;
import com.csjbot.cameraclient.entity.PicturePacket;
import com.csjbot.cameraclient.listener.CameraEventListener;
import com.csjbot.voiceclient.constant.VoiceClientConstant;
import com.csjbot.voiceclient.listener.VoiceClientEvent;
import com.csjbot.voiceclient.listener.VoiceEventListener;
import com.csjbot.voiceclient.utils.VoiceLogger;

import java.io.FileOutputStream;
import java.io.IOException;

public class AVTestActivity extends Activity implements View.OnClickListener {

    /**
     * 相机数据通信
     */
    private CameraClientAgent cameraClientAgent;


    /**
     * 麦克风整列音频数据
     */
    private ImageView imageView;
    private TextView textShow;
    private ScrollView scrollView;
    private Button recVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_av_test);

        initAudio();

        initViews();
        createAgents();
        connectAgnets();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        textShow = findViewById(R.id.textShow);
        scrollView = findViewById(R.id.scrollView);
        recVoice = findViewById(R.id.recVoice);


        recVoice.setOnClickListener(this);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recVoice:
                initAudioOut = !initAudioOut;
                break;

            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onDestroy() {
        try {
            if (fos != null)
                fos.close();// 关闭写入流
        } catch (IOException e) {
            e.printStackTrace();
        }

        initAudioOut = false;
//        voiceClientAgent.destroy();
        super.onDestroy();
    }

    private void showInfo(final String text) {
        runOnUiThread(() -> {
            textShow.append(text + "\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    /**
     * setp. 1
     * 初始化各个Agnet
     */
    private void createAgents() {
        cameraClientAgent = CameraClientAgent.createRosClientAgent(cameraAgentEeventListener, true);
    }

    /**
     * setp. 2
     * 让各个客户端都连接到底层Linux
     */
    private void connectAgnets() {
        if (cameraClientAgent != null) {
            cameraClientAgent.connect("192.168.99.101", 60003);
        }
    }

    private CameraEventListener cameraAgentEeventListener = new CameraEventListener() {

        @Override
        public void onCameraEvent(com.csjbot.cameraclient.listener.ClientEvent event) {
            switch (event.eventType) {
                case com.csjbot.cameraclient.constant.ClientConstant.EVENT_PACKET:
                    PicturePacket packet = (PicturePacket) event.data;
                    byte[] bitSrc = packet.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bitSrc, 0, bitSrc.length);
                    if (bitmap != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                    break;
                case com.csjbot.cameraclient.constant.ClientConstant.DUPLICATE_PIC_ERROR:
                    // TODO: 2017/11/16 0016 add pu， 当重复发一个图片5次会报这个错误，说明底下已经不行了

                    break;
                default:
                    break;
            }
        }
    };

    //********************** voiceClientAgent **************************//
    //********************** voiceClientAgent **************************//
    //********************** voiceClientAgent **************************//
    private AudioTrack trackplayer;
    private boolean initFile, initAudioOut;
    private FileOutputStream fos = null;

    private void initAudio() {

        int bufsize = AudioTrack.getMinBufferSize(16000,//每秒8K个点 
                AudioFormat.CHANNEL_OUT_MONO,//双声道
                AudioFormat.ENCODING_PCM_16BIT);//一个采样点16比特-2个字节

        trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC,
                16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufsize,
                AudioTrack.MODE_STREAM);

        trackplayer.play();
    }


    private VoiceEventListener voiceEventListener = new VoiceEventListener() {
        @Override
        public void onEvent(VoiceClientEvent event) {
            switch (event.eventType) {
                case VoiceClientConstant.EVENT_RECONNECTED:
                case VoiceClientConstant.EVENT_CONNECT_SUCCESS:
//                connectStatus(VoiceClientConstant.EVENT_CONNECT_SUCCESS);
                    VoiceLogger.info("EVENT_CONNECT_SUCCESS");
                    break;
                case VoiceClientConstant.EVENT_CONNECT_FAILD:
                    VoiceLogger.error("EVENT_CONNECT_FAILD " + String.valueOf(event.data));
                    break;
                case VoiceClientConstant.EVENT_CONNECT_TIME_OUT:
                    VoiceLogger.error("EVENT_CONNECT_TIME_OUT  " + String.valueOf(event.data));
                    break;
                case VoiceClientConstant.SEND_FAILED:
                    VoiceLogger.error("SEND_FAILED");
                    break;
                case VoiceClientConstant.EVENT_DISCONNET:
                    VoiceLogger.error("EVENT_DISCONNET");
                    break;
                case VoiceClientConstant.EVENT_PACKET:
                    // 收到数据就不停的写
                    byte[] data = (byte[]) event.data;
                    if (initFile) {
                        try {
                            fos.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (initAudioOut) {
                        trackplayer.write(data, 0, data.length);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void clickToGoBack(View view) {
        onBackPressed();
    }

    //********************** voiceClientAgent **************************//

}
