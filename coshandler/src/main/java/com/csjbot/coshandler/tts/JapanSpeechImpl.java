package com.csjbot.coshandler.tts;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;

import com.csjbot.coshandler.R;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import kr.co.voiceware.HARUKA;

/**
 * 日语tts实现类
 * Created by jingwc on 2018/1/10.
 */

public class JapanSpeechImpl implements ISpeechSpeak {

    private WeakReference<Context> context;
    private final byte[] license = new byte[2048];
    private MediaPlayer mediaPlayer;

    /**
     * 创建实例
     *
     * @param context
     * @return
     */
    public static JapanSpeechImpl newInstance(Context context) {
        return new JapanSpeechImpl(context);
    }

    /**
     * 私有有参构造
     *
     * @param ctx
     */
    private JapanSpeechImpl(Context ctx) {
        context = new WeakReference<>(ctx.getApplicationContext());

        mediaPlayer = new MediaPlayer();

        checkLicense();

        checkDBFile();

    }

    /**
     * 校验许可证文件
     */
    private void checkLicense() {
        int rtnCode;
        try {
            rtnCode = context.get().getResources().openRawResource(R.raw.verification).read(license);
        } catch (Exception ex) {
            rtnCode = -1;
        }
        if (rtnCode == -1) {
            CsjlogProxy.getInstance().warn("请确认许可证文件:/res/raw/verification.txt");
        } else {
            CsjlogProxy.getInstance().info("日语TTS验证完成");
        }
    }

    private void checkDBFile() {
        int rtnCode = -1;
        String db_path_detail = Environment.getExternalStorageDirectory().getPath() + "/micro_h16/tts_single_db_haruka.vtdb";
        String db_path = Environment.getExternalStorageDirectory().getPath() + "/micro_h16/";
        if (copyFile(db_path_detail, db_path)) {
            try {
                rtnCode = HARUKA.LOADTTS(db_path, license);
            } catch (Exception e) {
                CsjlogProxy.getInstance().warn("日语TTS:加载DB失败");
            }
        } else {
            CsjlogProxy.getInstance().warn("日语TTS:移动DB文件失败");
        }
        if (rtnCode != 0) {
            CsjlogProxy.getInstance().warn("日语TTS:请确认数据库文件:" + db_path + "*.vtdb");
        } else {
            CsjlogProxy.getInstance().info("日语TTS:加载DB文件成功");
        }
    }

    private boolean copyFile(String sourceFile, String destFile) {
        boolean isSuccess = true;
        File file = new File(sourceFile);
        try {
            if (!file.exists()) {
                new File(destFile).mkdirs();
                InputStream is;
                is = context.get().getAssets().open("tts_single_db_haruka.vtdb");
                FileOutputStream fos = new FileOutputStream(new File(sourceFile));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节       
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 开始说话
     *
     * @param text
     * @param listener
     */
    @Override
    public void startSpeaking(String text, final OnSpeakListener listener) {
        if (listener != null) {
            listener.onSpeakBegin();
        }

        text = text + "~";

        String filename = (context.get().getFilesDir().getAbsolutePath() + "/play.wav");
        int rtnCode = 0;
        try {
            //合成语音文件输出
            rtnCode = HARUKA.TextToFile(HARUKA.VT_FILE_API_FMT_S16PCM_WAVE, text, filename, -1, -1, -1, -1, -1, -1);
            if (rtnCode == -3) {
                CsjlogProxy.getInstance().warn("请确认许可证文件:/res/raw/verification.txt");
            }
        } catch (Exception e) {
            CsjlogProxy.getInstance().error(e.toString());
        }
        CsjlogProxy.getInstance().info("startSpeaking:rtnCode:" + rtnCode);
        if (rtnCode == 1) {
            mediaPlayer.reset();
            try {
                //设置音频文件
                mediaPlayer.setDataSource(filename);
                //再生语音文件
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (listener != null) {
                            listener.onCompleted(null);
                        }
                    }
                });
            } catch (Exception e) {
                CsjlogProxy.getInstance().error(e.toString());
            }
        }
    }

    /**
     * 停止说话
     */
    @Override
    public void stopSpeaking() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 暂停说话
     */
    @Override
    public void pauseSpeaking() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * 重新说话
     */
    @Override
    public void resumeSpeaking() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * 是否在说话
     *
     * @return
     */
    @Override
    public boolean isSpeaking() {
        return mediaPlayer != null ? mediaPlayer.isPlaying() : false;
    }

    @Override
    public boolean setSpeakerName(String name) {
        return false;
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
        mediaPlayer.setVolume(volume,volume);
    }
}
