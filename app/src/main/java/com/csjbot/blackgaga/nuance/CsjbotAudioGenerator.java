package com.csjbot.blackgaga.nuance;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

import com.roobo.vui.api.audiosource.BaseAudioGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by ChaoPei on 2017/12/15.
 * 　这里展示的是如何从标准的Android AudioRecord读取数据
 */
public class CsjbotAudioGenerator implements BaseAudioGenerator {

    private static final String TAG = "AudioGenerator";

    private static final int FRAME_SIZE = 2560;
    private byte[] mWriteDataBuf = new byte[FRAME_SIZE];

    // 20*80 = 1600ms
    private final BlockingQueue<byte[]> mRecorderData = new LinkedBlockingDeque<>(20);

    private volatile boolean mFinished;
    private HandlerThread mWorkerThread;
    private Handler mWorkerHandler;

    public CsjbotAudioGenerator() {

    }

    @Override
    public int getChannelCount() {
        return 1;
    }

    @Override
    public int getRefCount() {
        return 0;
    }


    @Override
    public short[][] getNextFrame() {

        short[][] data = new short[1][];
        synchronized (mRecorderData) {
            byte[] bytes = null;
            try {
                bytes = mRecorderData.poll(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bytes != null) {
                data[0] = convertByteArrayToShortArray(bytes, FRAME_SIZE, true);
            }
        }
        return data;
    }

    private void pushDataQueueLocked() {
        if (mFinished) {
            return;
        }
        byte[] data = new byte[FRAME_SIZE];
        System.arraycopy(mWriteDataBuf, 0, data, 0, FRAME_SIZE);
        if (!mRecorderData.offer(data)) {
            Log.d(TAG, "[pushDataQueueLocked]: too slow to take data. clear all.");
            mRecorderData.clear();
            mRecorderData.add(data); // 这里不再验证能否成功，仅靠其抛异常
        }
    }

    public void setData(byte[] data){
        System.arraycopy(data, 0, mWriteDataBuf, 0, FRAME_SIZE);
        pushDataQueueLocked();
    }

    @Override
    synchronized public void onStart() {
        if (mWorkerThread != null || mWorkerHandler != null) {
            Log.e(TAG, "[onStart]: recorder has been started!!!");
        }
        mFinished = false;
        mWorkerThread = new HandlerThread("RooboRecorder", Process.THREAD_PRIORITY_URGENT_AUDIO);
        mWorkerThread.start();
        mWorkerHandler = new Handler(mWorkerThread.getLooper());
    }

    @Override
    synchronized public void onStop() {
        mFinished = true;
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerHandler.removeMessages(0);
        mWorkerHandler = null;
        mWorkerThread.quitSafely();
        mWorkerThread = null;
    }

    private short[] convertByteArrayToShortArray(byte[] data, int length, boolean littleEndian) {
        short[] shorts = new short[length / 2];
        for (int i = 0; i < length - 1; i += 2) {
            if (littleEndian) {
                shorts[i / 2] = (short) ((((data[i + 1] << 8) & 0xFF00) | (data[i] & 0xFF)));
            } else {
                shorts[i / 2] = (short) ((((data[i] << 8) & 0xFF00) | (data[i + 1] & 0xFF)));
            }
        }

        return shorts;
    }
}
