package com.csjbot.nuance;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.nuance.speechkit.DetectionType;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Recognition;
import com.nuance.speechkit.RecognitionType;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;

import java.util.ArrayList;
import java.util.List;

public class NuanceService extends Service {

    private List<Session> sessions;

    private List<Transaction> transactions;

    private MyReceiver myReceiver;

    private boolean isClose;

    private int currentTaskIndex = 0;

    public NuanceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","NuanceService onCreate");
        init();
        recognize();
    }

    private void init(){
        Log.d("TAG","NuanceService init");
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NuanceManager.CANCEL);
        filter.addAction(NuanceManager.RECOGNIZE);
        filter.addAction(NuanceManager.STOP_RECORDING);
        registerReceiver(myReceiver, filter);

        sessions = new ArrayList<>();

        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));
        sessions.add(Session.Factory.session(this,Configuration.SERVER_URI,Configuration.APP_KEY));

        transactions = new ArrayList<>();
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
        transactions.add(null);
    }

    private void recognize(){
        Log.d("TAG","NuanceService isClose:"+isClose);
        if(isClose){
            return;
        }
        Log.d("TAG","NuanceService recognize");
        Transaction.Options options = new Transaction.Options();
        options.setRecognitionType(RecognitionType.DICTATION);
        options.setDetection(DetectionType.Short);
        options.setLanguage(new Language(NuanceManager.LANGUAGE));


        if(currentTaskIndex >= sessions.size()){
            currentTaskIndex = 0;
        }
        final int index = currentTaskIndex;

        Log.d("TAG","NuanceService recognize index"+index);
        Transaction transaction = sessions.get(currentTaskIndex).recognize(options, new Transaction.Listener() {
            @Override
            public void onStartedRecording(Transaction transaction) {
                super.onStartedRecording(transaction);
                Log.d("TAG","onStartedRecording:"+index);
                NuanceManager.getInstance().pushStart();
            }

            @Override
            public void onFinishedRecording(Transaction transaction) {
                super.onFinishedRecording(transaction);
                Log.d("TAG","onFinishedRecording:"+index);
                NuanceManager.getInstance().pushFinish();
                currentTaskIndex++;
                recognize();
            }

            @Override
            public void onRecognition(Transaction transaction, Recognition recognition) {
                super.onRecognition(transaction, recognition);
                Log.d("TAG","onRecognition:"+index+":"+recognition.getText());
                NuanceManager.getInstance().pushRecognitionText(recognition.getText());
            }

            @Override
            public void onSuccess(Transaction transaction, String s) {
                super.onSuccess(transaction, s);
                Log.d("TAG","onSuccess:"+index+":"+s);
                NuanceManager.getInstance().pushSuccess();
            }

            @Override
            public void onError(Transaction transaction, String s, TransactionException e) {
                super.onError(transaction, s, e);
                Log.d("TAG","onError:"+index+":"+s+" "+e);
                NuanceManager.getInstance().pushError(s);
            }
        });
        transactions.set(currentTaskIndex,transaction);
    }

    private void stopRecording(){
        Log.d("TAG","NuanceService stopRecording");
        if(transactions == null || transactions.size() == 0){
            return;
        }
        transactions.get(currentTaskIndex).stopRecording();
    }

    private void cancel(){
        Log.d("TAG","NuanceService cancel");
        if(transactions == null || transactions.size() == 0){
            return;
        }
        transactions.get(currentTaskIndex).cancel();
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case NuanceManager.CANCEL:
                    isClose = true;
                    cancel();
                    break;
                case NuanceManager.RECOGNIZE:
                    isClose = false;
                    recognize();
                    break;
                case NuanceManager.STOP_RECORDING:
                    stopRecording();
                    break;
                default:
                        break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
