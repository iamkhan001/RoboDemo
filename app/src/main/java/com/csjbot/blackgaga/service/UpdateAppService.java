package com.csjbot.blackgaga.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.bean.OnUpdateAppEvent;
import com.csjbot.blackgaga.bean.UpdateAPKBean;
import com.csjbot.blackgaga.model.http.ApiUrl;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Ben
 * @date 2018/4/17
 */

public class UpdateAppService extends BaseService {

    private static final String TAG = "UpdateAppService";
    private boolean isNetworkAvailable = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    new Thread(() -> checkAppVersion()).start();
                    break;
                case 2:
                    if (!isNetworkAvailable()) {
                        mHandler.sendEmptyMessageDelayed(2, 10000);
                    } else {
                        mHandler.removeMessages(2);
                        new Thread(() -> checkAppVersion()).start();
                    }
                    break;
                case 3:
                    if (!SharedPreUtil.getBoolean(SharedKey.UPDATE_APP, "check_app", false)) {
                        EventBus.getDefault().post(new OnUpdateAppEvent(true));
                        mHandler.sendEmptyMessageDelayed(3, 3000);
                    } else {
                        mHandler.removeMessages(3);
                        SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", false);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        isNetworkAvailable = isNetworkAvailable();
        if (isNetworkAvailable) {
            mHandler.sendEmptyMessage(1);
        } else {
            mHandler.sendEmptyMessage(2);
        }
    }

    /**
     * 检查程序是否有更新
     */
    private void checkAppVersion() {
        String retail = BuildConfig.category;

        if (TextUtils.isEmpty(retail)) {
            Log.e("检查App更新", "checkUpdate: retail is null");
            return;
        }
        //版本更新
        String url = ApiUrl.DEFAULT_ADRESS;
        CsjlogProxy.getInstance().debug("url---" + url);
        OkHttpClient okHttpClient_get = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().get()
                .url(url)
                .build();
        Response response;
        String json = null;
        try {
            response = okHttpClient_get.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                CsjlogProxy.getInstance().debug(json);
            } else {
                CsjlogProxy.getInstance().error("检查APP更新网络请求失败: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        UpdateAPKBean updateAPKBean = new Gson().fromJson(json, UpdateAPKBean.class);
        int version_code = 0;
        if (updateAPKBean != null && updateAPKBean.getResult() != null && updateAPKBean.getResult().getResule() != null) {
            //版本号
            version_code = updateAPKBean.getResult().getResule().getVersion_code();
            CsjlogProxy.getInstance().debug("version_code " + version_code);
        } else {
            return;
        }
//        //更新地址
//        String apkUrl = updateAPKBean.getResult().getResule().getUrl();
//        //版本类型
//        String channel = updateAPKBean.getResult().getResule().getChannel();
//        //版本名
//        String mVersion_name = updateAPKBean.getResult().getResule().getVersion_name();
//        //机器人类型
//        String category = updateAPKBean.getResult().getResule().getCategory();
//        // md5
//        String md5 = updateAPKBean.getResult().getResule().getChecksum();

        // 2.进行版本号比对
        int current_version = getVersionCode();
        CsjlogProxy.getInstance().debug("current_version " + current_version);
        if (version_code > current_version) {
            mHandler.sendEmptyMessage(3);
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private int getVersionCode() {
        //包管理器
        PackageManager packageManager = getApplicationContext().getPackageManager();
        String packageName = getApplicationContext().getPackageName();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 检查网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", false);
    }
}
