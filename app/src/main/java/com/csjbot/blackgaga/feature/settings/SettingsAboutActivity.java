package com.csjbot.blackgaga.feature.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.dialog.NewRetailEdittextDialog;
import com.csjbot.blackgaga.model.http.factory.RetrofitFactory;
import com.csjbot.blackgaga.model.http.product.ProductService;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.AndroidCopyVersion;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.ZxingUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnSetSNListener;
import com.csjbot.coshandler.log.Csjlogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 孙秀艳 on 2017/10/20.
 * 系统设置关于界面
 */

public class SettingsAboutActivity extends BaseModuleActivity {

    @BindView(R.id.settings_about_info_version)
    TextView tvRobotInfoVersion;//机器人信息版本
    @BindView(R.id.settings_about_model)
    TextView tvRobotModel;//机器人型号
    @BindView(R.id.settings_about_memory)
    TextView tvRobotMemory;//机器人运行内存
    @BindView(R.id.settings_about_sn)
    TextView tvRobotSN;//机器人SN
    @BindView(R.id.settings_about_ui_version)
    TextView tvRobotUIVersion;//机器人UI版本
    @BindView(R.id.settings_about_service_version)
    TextView tvRobotServiceVersion;//机器人服务版本
    @BindView(R.id.settings_about_copyright)
    TextView tvRobotVersion;//机器人版权
    @BindView(R.id.setting_about_title)
    TextView tvAboutTitle;//机器人关于界面title
    @BindView(R.id.image_qrcode)
    ImageView image_qrcode;
    @BindView(R.id.button_get)
    Button get;

    private String code;

    private AndroidCopyVersion androidCopyVersion;
    private IChassis chassis;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        tvRobotModel.setText(BuildConfig.robotType);
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        chassis = ServerFactory.getChassisInstance();
        setUIVersion();
    }

    /**
     * 设置机器人信息版本
     */
    private void setRobotVersion() {
        RobotManager.getInstance().addListener((OnGetVersionListener) version -> runOnUiThread(() -> {
            tvRobotInfoVersion.setText(version);
        }));
        new Handler().postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getVersion(), 200);
    }

    /**
     * 设置机器人软件版本
     */
    private void setUIVersion() {
        androidCopyVersion = new AndroidCopyVersion(this);
        Csjlogger.debug(androidCopyVersion.getVersion());
        tvRobotUIVersion.setText(androidCopyVersion.getVersion());
    }

    /**
     * 设置机器人SN
     */
    private void setSN() {
        RobotManager.getInstance().addListener((OnSNListener) sn -> {
            try {
                JSONObject jo = new JSONObject(sn);
                String str = jo.optString("sn");
                if (TextUtils.isEmpty(str) || str.contains("empty")) {
                    // sn 未能获取到
                    BlackgagaLogger.debug("未能获取到SN信息");
                    canGetDeviceInfo = true;
                } else {
                    runOnUiThread(() -> {
                        if (dialog != null) dialog.dismiss();
                        BlackgagaLogger.debug("获取到SN信息");
                        Robot.setSN(str);
                        tvRobotSN.setText(Robot.SN);
                        insertQrCodeInImage();
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                BlackgagaLogger.debug("发生异常");
            }
        });
        RobotManager.getInstance().robot.reqProxy.getSN();
    }

    private void insertQrCodeInImage() {
        String content = Robot.SN;
        Bitmap bitmap = ZxingUtil.createQRCode(content, image_qrcode.getWidth(), image_qrcode.getHeight());
        if (bitmap != null) {
            image_qrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    boolean canGetDeviceInfo = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (RobotManager.getInstance().getConnectState()) {
            setSN();
            setRobotVersion();
        } else {
            Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
            canGetDeviceInfo = true;
            BlackgagaLogger.debug("底层未连接");
        }
        tvRobotSN.setText(Robot.SN);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_about : R.layout.activity_settings_about;

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    int count = 0;
    ProgressDialog dialog = null;


    Handler h = new Handler();

    @OnClick(R.id.button_get)
    public void getRecognitionNumber() {
        Bean bean = new Bean();
        bean.setSn(Robot.SN);
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("0000");
        code = df.format(random.nextInt(10000));
        bean.setDynamiccode(code);
        RetrofitFactory.create(ProductService.class).getCode(bean).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {
                if (result == null) {
                    Toast.makeText(SettingsAboutActivity.this, R.string.send_failed, 1).show();
                } else {
                    if (result.getResult() == null) {
                        Toast.makeText(SettingsAboutActivity.this, R.string.send_failed, 1).show();
                    } else {
                        if (result.getResult().isCheck_result()) {
                            showCode();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SettingsAboutActivity.this, R.string.send_failed, 1).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void showCode() {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(getResources().getDimension(R.dimen.btn_text_size));
        textView.setText(code);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textView);
        builder.setNegativeButton(getString(R.string.sure), (dialog, which) -> {
            dialog.dismiss();
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @OnLongClick(R.id.ll_sn)
    public boolean setSn() {
        if (!RobotManager.getInstance().getConnectState()) {
            Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
            BlackgagaLogger.debug("底层未连接");
            return false;
        }

        if (!canGetDeviceInfo) {
            return false;
        }

        NewRetailEdittextDialog dialog = new NewRetailEdittextDialog(this);
        dialog.setTitle("SN");
        dialog.setListener(new NewRetailEdittextDialog.OnDialogClickListener() {
            @Override
            public void yes(String sn) {
                RobotManager.getInstance().robot.reqProxy.setSN(sn);
                RobotManager.getInstance().addListener((OnSetSNListener) BlackgagaLogger::debug);
                dialog.dismiss();
            }

            @Override
            public void no() {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }

    @OnClick(R.id.ll_sn)
    public void getSn() {
        if (!canGetDeviceInfo) return;

        count++;
        if (count >= 3 && count < 6) {
            Toast.makeText(this, getString(R.string.click_again) + (6 - count) + getString(R.string.enter_into_sn_view), Toast.LENGTH_SHORT).show();
        }

        if (count >= 6) {
            if (!RobotManager.getInstance().getConnectState()) {
                Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
                BlackgagaLogger.debug("底层未连接");
                return;
            }

            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.commution));
            dialog.setMessage(getString(R.string.get_device_info));
            dialog.show();
            dialog.setOnDismissListener(dialog1 -> h.removeCallbacksAndMessages(null));

//            RobotManager.getInstance().robot.reqProxy.getDeviceInfo();
//            RobotManager.getInstance().addListener(new OnDeviceInfoListener() {
//                @Override
//                public void response(String info) {
//                    DeviceInfo deviceInfo = new Gson().fromJson(info, DeviceInfo.class);
//                    runOnUiThread(() -> {
//                        if (deviceInfo.getDownPlate().contains("empty")) {
//                            Toast.makeText(context, "未能获取下身板信息", Toast.LENGTH_SHORT).show();
//                            h.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getDeviceInfo(), 500);
//                        } else if (deviceInfo.getUpComputer().contains("empty")) {
//                            Toast.makeText(context, "未能获取上位机信息", Toast.LENGTH_SHORT).show();
//                            h.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getDeviceInfo(), 500);
//                        } else if (deviceInfo.getUpPlate().contains("empty")) {
//                            Toast.makeText(context, "未能获取上身板信息", Toast.LENGTH_SHORT).show();
//                            h.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getDeviceInfo(), 500);
//                        } else if (deviceInfo.getNav().contains("empty")) {
//                            Toast.makeText(context, "未能获取导航板信息", Toast.LENGTH_SHORT).show();
//                            h.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getDeviceInfo(), 500);
//                        } else {
//                            Intent intent = new Intent(SettingsAboutActivity.this, ProductSNActivity.class);
//                            intent.putExtra(com.csjbot.blackgaga.global.Constants.DOWN_PLATE, deviceInfo.getDownPlate());
//                            intent.putExtra(com.csjbot.blackgaga.global.Constants.UP_PLATE, deviceInfo.getUpPlate());
//                            intent.putExtra(com.csjbot.blackgaga.global.Constants.UP_COMPUTER, deviceInfo.getUpComputer());
//                            intent.putExtra(com.csjbot.blackgaga.global.Constants.NAV, deviceInfo.getNav());
//                            dialog.dismiss();
//                            startActivity(intent);
//                            count = 0;
//                        }
//                    });
//                }
//            });
        }
    }

    public class Bean {

        /**
         * sn :
         * dynamiccode  :
         */

        private String sn;
        private String dynamiccode;

        public Bean() {
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getDynamiccode() {
            return dynamiccode;
        }

        public void setDynamiccode(String dynamiccode) {
            this.dynamiccode = dynamiccode;
        }
    }

    public class Result {

        /**
         * message : ok
         * result : {"check_result":true}
         * status : 200
         */

        private String message;
        private ResultBean result;
        private String status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public class ResultBean {
            /**
             * check_result : true
             */

            private boolean check_result;

            public boolean isCheck_result() {
                return check_result;
            }

            public void setCheck_result(boolean check_result) {
                this.check_result = check_result;
            }
        }
    }
}
