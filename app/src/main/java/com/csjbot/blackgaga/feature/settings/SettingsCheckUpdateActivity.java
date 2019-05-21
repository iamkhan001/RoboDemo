package com.csjbot.blackgaga.feature.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.UpdateAPKBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.dialog.ProgressDialog;
import com.csjbot.blackgaga.model.http.ApiUrl;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.AndroidCopyVersion;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.DownloadUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.coshandler.listener.OnUpgradeListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiasuhuei321 on 2017/10/20.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SettingsCheckUpdateActivity extends BaseModuleActivity {

    @BindView(R.id.tv_system_version)
    TextView mTvSystemVersion;
    @BindView(R.id.tv_version_number)
    TextView mTvVersionNumber;//版本号
    @BindView(R.id.bt_update)
    Button mBtUpdate;
    @BindView(R.id.bt_linuxupdate)
    Button mBtLinuxupdate;
    @BindView(R.id.tv_linux_version_number)
    TextView mTvLinuxVersionNumber;
    private Context mContext;

    private String NewRetail = null;
    //当前版本
    private int presentVersionCode;
    //远程版本
    private int version_code = -1;
    // 下载安装包的网络路径
    private String apkUrl;
    // 保存apk的文件夹
    private static final String savePath = "/sdcard/Download/";
    //保存的apk名称
    private static final String saveFileName = savePath + "newRetail.apk";
    private ProgressDialog downloadDialog; // 下载对话框
    private ProgressDialog linuxDownloadDialog;
    // 通知处理刷新界面的handler
    private String mVersion_name;
    private String md5;
    AndroidCopyVersion androidCopyVersion = new AndroidCopyVersion(this);
    private DownloadUtil downloadUtil;
    private String channel;
    private String category;
    private File apkfile;
    private int mDownloadProgress;
    private boolean linuxClicked;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) ? R.layout.vertical_activity_check_update : R.layout.activity_check_update;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        presentVersionCode = getVersionCode(getApplication());
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvVersionNumber.setText(androidCopyVersion.getVersion());

    }

    @Override
    protected void onResume() {
        super.onResume();
        linuxDownloadDialog = new ProgressDialog(this);
        if (RobotManager.getInstance().getConnectState()) {
            setRobotVersion();
        } else {
            Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
            BlackgagaLogger.debug("底层未连接");
        }
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linuxClicked = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        linuxClicked = false;
    }

    /**
     * 设置机器人信息版本
     */
    private void setRobotVersion() {
        RobotManager.getInstance().robot.reqProxy.getVersion();
        RobotManager.getInstance().addListener((OnGetVersionListener) version -> runOnUiThread(() -> mTvLinuxVersionNumber.setText(version)));
    }

    private void initData() {
        new Thread(() -> {
            try {
                resolveJson();
            } catch (Exception e) {
                CsjlogProxy.getInstance().debug("e" + e.toString());
                e.printStackTrace();
            }
        }).start();

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

    @OnClick({R.id.bt_linuxupdate, R.id.bt_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_linuxupdate:
                if (!RobotManager.getInstance().getConnectState()) {
                    Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerFactory.getVersionInstance().softwareCheck();
                RobotManager.getInstance().addListener(new OnUpgradeListener() {
                    @Override
                    public void checkRsp(int errorCode) {
                        if (errorCode == 60002) {
                            view.post(() -> CSJToast.showToast(getApplicationContext(), R.string.already_latest_version, 100));
                        } else if (errorCode == 60001) {
                            view.post(() -> CSJToast.showToast(getApplicationContext(), R.string.Please_check_network, 100));
                        } else if (errorCode == 0) {
                            if (!linuxClicked) {
                                ServerFactory.getVersionInstance().softwareUpgrade();
                                view.post(() -> linuxShowDownloadDialog());

                            }
                        }
                    }

                    @Override
                    public void upgradeRsp(int errorCode) {
                        CsjlogProxy.getInstance().debug("errorCode66666");
                    }

                    @Override
                    public void upgradeProgress(int downloadProgress) {
                        mDownloadProgress = downloadProgress;

                        CsjlogProxy.getInstance().debug("errorCode67777");
                        if (downloadProgress == 100) {
                            view.post(() -> linuxDownloadDialog.dismiss());
                            view.post(() -> CSJToast.showToast(getApplicationContext(), R.string.download_completes_install, 1000));
                            SettingsCheckUpdateActivity.this.finish();
                        } else if (downloadProgress < 100) {
                            if (linuxDownloadDialog.getProgressId() != null) {
                                view.post(() -> linuxDownloadDialog.getProgressId().setProgress(downloadProgress));
                            }
                        }

                        CsjlogProxy.getInstance().info("linuxDownloadProgress---> " + downloadProgress);
                        CsjlogProxy.getInstance().debug("linuxDownloadProgress---> " + downloadProgress);
                    }
                });

                break;
            case R.id.bt_update:
                if (version_code > presentVersionCode) {
                    File apkfile = new File(saveFileName);
                    if (!apkfile.exists()) {
                        showNoticeDialog();
                        //            } else {
                        //                String fileMD5 = MD5.getFileMD5(saveFileName + savePath);
                        //                String md5 = SharedPreUtil.getString(SharedKey.APK_INFO, SharedKey.APK_MD5, "null");
                        //                // 校验 apk md5
                        //                new Thread(() -> {
                        //                    if (md5.equals(fileMD5)) {
                        //                        runOnUiThread(this::installApk);
                        //                    } else {
                        //                        runOnUiThread(() -> {
                        //                            BlackgagaLogger.debug("md5不一致，重新下载apk");
                        //                            apkfile.delete();
                        //                            showNoticeDialog();
                        //                        });
                        //
                        //                    }
                        //                }).start();
                    }
                } else if (version_code == -1) {
                    Toast.makeText(this, R.string.Please_check_network, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, R.string.already_latest_version, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void resolveJson() throws Exception {
        String NewRetail = BuildConfig.category;

        CsjlogProxy.getInstance().debug("robotType---" + BuildConfig.robotType);
        String url = ApiUrl.DEFAULT_ADRESS;//版本更新
        CsjlogProxy.getInstance().debug("url---" + url);
        OkHttpClient okHttpClient_get = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().get()
                .url(url)
                .build();
        Response response;
        response = okHttpClient_get.newCall(request).execute();
        String json = response.body().string();
        UpdateAPKBean updateAPKBean = new Gson().fromJson(json, UpdateAPKBean.class);
        //版本号
        version_code = updateAPKBean.getResult().getResule().getVersion_code();
        //更新地址
        apkUrl = updateAPKBean.getResult().getResule().getUrl();
        //版本类型
        channel = updateAPKBean.getResult().getResule().getChannel();
        //版本名
        mVersion_name = updateAPKBean.getResult().getResule().getVersion_name();
        //机器人类型
        category = updateAPKBean.getResult().getResule().getCategory();
        // md5
        md5 = updateAPKBean.getResult().getResule().getChecksum();
        // 2.进行版本号比对
        int versionCode = getVersionCode(SettingsCheckUpdateActivity.this);
        CsjlogProxy.getInstance().debug("---获取的数据是: 机器人类型:" + category + "  服务器版本号:" + version_code + "  " +
                " 当前版本" + versionCode + "  更新地址:" + apkUrl + "  版本类型:" + channel + "  版本名:" + mVersion_name);
        //        mTvSystemVersion.post(() -> mTvSystemVersion.setText(getString(R.string.current_version) + mVersion_name));

    }

    private void showNoticeDialog() {
        showNewRetailDialog(getString(R.string.version_update), mVersion_name, getString(R.string.apk_download), getString(R.string.update_later), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                showDownloadDialog();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }

    public void showDownloadDialog() {
        downloadDialog = new ProgressDialog(this);
        downloadDialog.setTitle(getString(R.string.version_update));
        downloadDialog.setListener(() -> {
            downloadDialog.dismiss();
            /*手动取消下载,删除文件*/
            DownloadUtil.isCanceled = true;
        });
        downloadDialog.show();
        downloadDialog.setCancelable(false);
        downloadDialog.setOnKeyListener(keylistener);
        downloadApk();
    }

    public void linuxShowDownloadDialog() {
        linuxClicked = true;
        linuxDownloadDialog.setTitleShow(getString(R.string.version_update_linux), View.VISIBLE);
        linuxDownloadDialog.setButtonState(View.GONE);
        linuxDownloadDialog.show();
        linuxDownloadDialog.setCancelable(false);
        linuxDownloadDialog.setOnKeyListener(linuxKeylistener);
    }

    private void downloadApk() {
        downloadUtil = new DownloadUtil(1, "http://" + apkUrl, savePath);
        downloadUtil.setOnDownloadListener(new DownloadUtil.OnDownloadListener() {
            @Override
            public void onSuccess(int downloadId) {
                installApk();
                BlackgagaLogger.debug(downloadId + "下载成功");
                downloadDialog.dismiss();
            }

            @Override
            public void onStart(int downloadId, long fileSize) {
                long localFileLength = downloadUtil.getLocalFileLength();
                if (localFileLength == fileSize) {
                    installApk();
                    downloadDialog.dismiss();
                    return;
                }

                BlackgagaLogger.debug("localFileLength--开始下载-----：" + localFileLength);
                BlackgagaLogger.debug("downloadUtil.getFileName()--开始下载-----：" + downloadUtil.getFileName());
                BlackgagaLogger.debug(downloadId + "开始下载，文件大小-------：" + fileSize);

                new Handler().postDelayed(() -> {
                    if (downloadDialog != null && downloadDialog.isShowing()) {
                        int progress = downloadDialog.getProgressId().getProgress();
                        if (progress <= 0) {
                            Toast.makeText(SettingsCheckUpdateActivity.this, getString(R.string.download_speed_slow) + "," + getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000 * 60);

            }

            @Override
            public void onPublish(int downloadId, long size) {
                BlackgagaLogger.debug("更新文件----------111111111111---------->" + size);
                /*进度条*/
                downloadDialog.getProgressId().setProgress((int) size);
            }

            @Override
            public void onPause(int downloadId) {
                BlackgagaLogger.debug("暂停下载" + downloadId);
            }

            @Override
            public void onGoon(int downloadId, long localSize) {
                BlackgagaLogger.debug("继续下载" + downloadId + "----" + localSize);

            }

            @Override
            public void onError(int downloadId) {
                BlackgagaLogger.debug("下载出错" + downloadId);
                Toast.makeText(SettingsCheckUpdateActivity.this, getString(R.string.download_fail) + "," + getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
                downloadDialog.dismiss();

            }

            @Override
            public void onCancel(int downloadId) {
                BlackgagaLogger.debug("取消下载" + downloadId);
                DownloadUtil.isCanceled = false;
                downloadDialog.dismiss();
            }
        });

        downloadUtil.start(false);

    }

    ProgressDialog.OnKeyListener keylistener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            DownloadUtil.isCanceled = true;
            downloadDialog.setCancelable(false);
            CsjlogProxy.getInstance().debug("返回键按了-false--");
            return false;
        } else {
            CsjlogProxy.getInstance().debug("返回键按了-true--");
            return true;
        }
    };
    ProgressDialog.OnKeyListener linuxKeylistener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            linuxDownloadDialog.setCancelable(false);
            CsjlogProxy.getInstance().debug("返回键按了-false--");
            return false;
        } else {
            CsjlogProxy.getInstance().debug("返回键按了-true--");
            return true;
        }
    };

    protected void installApk() {
        ProductProxy proxy = new ProductProxy();
        proxy.removeSceneRes();
        File apkfile = new File(savePath + downloadUtil.getFileName());
        BlackgagaLogger.debug("在安装------>");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");// File.toString()会返回路径信息
        SettingsCheckUpdateActivity.this.startActivity(i);
        BlackgagaLogger.debug("---apkfile" + apkfile.toString());
    }
}
