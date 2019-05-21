package com.csjbot.blackgaga.util;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.cameraclient.utils.CameraLogger;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UpdateApkManagerUtil {
    private static final String TAG = "UpdateApkManagerUtil";
    // 应用程序Context

    private Context mContext;
    private LauncherActivity splashActivity; //入口是否是欢迎界面
    private boolean isFromWelcomeUI; //入口是否是欢迎界面
    // 提示消息
    private String updateMsg = "有最新的软件包，请下载！";
    // 下载安装包的网络路径
    private String apkUrl;
    private static final String savePath = "/sdcard/Download/";// 保存apk的文件夹
    private static final String saveFileName = savePath + "blackgaga.apk"; //保存的apk名称
    // 进度条与通知UI刷新的handler和msg常量
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    private int progress;// 当前进度
    private Thread downLoadThread; // 下载线程
    // 通知处理刷新界面的handler
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    //   mProgress.setProgress(progress);
//                    System.out.println("progressprogress"+Formatter.formatFileSize(mContext, progress));

                    break;
                case DOWN_OVER:
                    CameraLogger.debug("--------------下载完成");
                    progress = 0;
                    Toast.makeText(mContext, mContext.getString(R.string.download_completes), Toast.LENGTH_SHORT).show();
                    // 下载完成后将md5值存入配置文件
                    SharedPreUtil.putString(SharedKey.APK_INFO, SharedKey.APK_MD5, md5);
                    BlackgagaLogger.debug("下载完成，将md5存入本地文件，存入md5为：" + md5);
//                    installApk();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String mVersion_name;

    // 下载安装包的网络路径
    public static String UPDATEURL = "";//版本更新
    private int mVersion;
    private String md5;

    public UpdateApkManagerUtil(Context context, boolean isFromWelcomeUI) {
        this.mContext = context;
        this.isFromWelcomeUI = isFromWelcomeUI;
        this.mContext = context;
        if (isFromWelcomeUI) {
            this.splashActivity = (LauncherActivity) context;
        }
    }

    // 显示更新程序对话框，供主程序调用nre
    public void checkUpdateInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resolveJson();
                } catch (Exception e) {

                }

            }
        }).start();


    }

    private void resolveJson() throws Exception {
        BlackgagaLogger.debug("检查更新开始。。。");
        String url = UPDATEURL;
        OkHttpClient okHttpClient_get = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build();

        Request request = new Request.Builder().get()// get请求方式
                .url(url)
                .build();

        Response response;
        response = okHttpClient_get.newCall(request).execute();

        String json = response.body().string();
        BlackgagaLogger.debug("更新信息：" + json);

        JSONObject jsonObject = new JSONObject(json);
        JSONObject scoreObj = (JSONObject) jsonObject.get("result");
        JSONObject obj = (JSONObject) scoreObj.get("resule");

        int version_code = obj.getInt("version_code");//版本号
        BlackgagaLogger.debug("version_code" + version_code);

        apkUrl = obj.getString("url");//更新地址
        String channel = obj.getString("channel");//版本类型

        //版本名
        mVersion_name = obj.getString("version_name");
        mVersion = getLocalVersion(mContext);

        String category = obj.getString("category");//机器人类型
        // 2.进行版本号比对

        // md5 校验码
        md5 = obj.getString("checksum");
        String localMd5 = SharedPreUtil.getString(SharedKey.APK_INFO, SharedKey.APK_MD5, "null");
        if (localMd5.equals(md5)) {
            // 如果本地 md5 和远程的md5值相同，就不用再下载了。
            // 但是如果本地没有这个 apk 那么就下载下来。。
            File f = new File(savePath + saveFileName);
            if (!f.exists()) {
                downloadApk();
            } else {
                BlackgagaLogger.debug("本地文件md5与服务器文件md5值相同！");
            }
            return;
        }

        if (version_code > mVersion) {
            downloadApk();
        } else {
            BlackgagaLogger.debug("本地版高于或者就是服务器版本");
        }
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        //包管理器
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    protected void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");// File.toString()会返回路径信息
        mContext.startActivity(i);
    }


    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url;
            BufferedOutputStream bos = null;
            try {
                url = new URL("http://" + apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }

                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                if (!ApkFile.exists()) ApkFile.createNewFile();
                bos = new BufferedOutputStream(new FileOutputStream(ApkFile));
                int count = 0;
                byte buf[] = new byte[1024];
                while ((count = bis.read(buf)) != -1) {
                    progress += count;
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    bos.write(buf, 0, count);
                    if (progress >= length) {
                        mHandler.sendEmptyMessage(DOWN_OVER);
                    }
                }
//                do {
//                    int numread = bis.read(buf);
//                    count += numread;
//                    progress = (int) (((float) count / length) * 100);
//                    // 下载进度
//                    mHandler.sendEmptyMessage(DOWN_UPDATE);
//                    if (numread <= 0) {
//                        // 下载完成通知安装
//                        mHandler.sendEmptyMessage(DOWN_OVER);
//                        break;
//                    }
//                    bos.write(buf, 0, numread);
//                } while (!interceptFlag);// 点击取消停止下载
                bos.flush();
                bos.close();
                bis.close();
            } catch (Exception e) {
                Log.e("UpdateApkManagerUtil",
                        "run(UpdateApkManagerUtil.java:218)" + e.toString());

                e.printStackTrace();
            }
        }
    };

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}

