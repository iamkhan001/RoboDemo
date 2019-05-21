package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.blackgaga.widget.RangeSeekBar;
import com.csjbot.coshandler.listener.OnSpeedSetListener;

import butterknife.BindView;

/**
 * Created by 孙秀艳 on 2017/10/20.
 * 系统设置 速度设置
 */

public class SettingsSpeedActivity extends BaseModuleActivity implements View.OnClickListener {
    public static float speed;
    private float process = 0;
    //手指按下的点为(startX)手指离开屏幕的点为(stopX)
    private int startX = 0;
    private int stopX = 0;
    private int MoveX;
    private float mTempSpeed;
    private float mTempProcess;
    private IChassis chassis;
    private boolean isFinish = false;

    @BindView(R.id.speed_setting)
    RangeSeekBar speedSetting;
    @BindView(R.id.tvSpeedSave)
    TextView tvSpeedSave;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        chassis = ServerFactory.getChassisInstance();
        speedsetting();
        tvSpeedSave.setOnClickListener(this);
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    private void speedsetting() {
        speed = SharedPreUtil.getFloat(SharedKey.SPEED_NAME, SharedKey.SPEED_KEY, 0.5f);
        if (speed == 0.4) {
            process = 0;
        } else if (speed == 0.45f) {
            process = 16;
        } else if (speed == 0.5f) {
            process = 33;
        } else if (speed == 0.55f) {
            process = 50;
        } else if (speed == 0.6f) {
            process = 66;
        } else if (speed == 0.65f) {
            process = 83;
        } else if (speed == 0.7f) {
            process = 100;
        }
        speedSetting.setValue(process);
        speedSetting.setOnRangeChangedListener((view, min, max, isFromUser) -> {
            if (isFromUser) {
                if (min == 0) {
                    speed = 0.4f;
                    process = 0;
                } else if ((int) min == 16) {
                    speed = 0.45f;
                    process = 16;
                } else if ((int) min == 33) {
                    speed = 0.5f;
                    process = 33;
                } else if ((int) min == 50) {
                    speed = 0.55f;
                    process = 50;
                } else if ((int) min == 66) {
                    speed = 0.6f;
                    process = 66;
                } else if ((int) min == 83) {
                    speed = 0.65f;
                    process = 83;
                } else if ((int) min == 100) {
                    speed = 0.7f;
                    process = 100;
                }
            }
        });

        speedSetting.setOnTouchListener(new View.OnTouchListener() {
            public int disX;
            private boolean isCloseDialog;
            private int upx;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当手指按下的时候
                        startX = (int) event.getX();
                        mTempSpeed = speed;
                        mTempProcess = process;

                        if (speed >= 0.7f && startX <= upx) {
                            isCloseDialog = true;
                        } else {
                            isCloseDialog = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        MoveX = (int) event.getX();
                        disX = MoveX - startX;
                        break;
                    case MotionEvent.ACTION_UP:
                        //当手指离开的时候
                        stopX = (int) event.getX();
                        upx = stopX;
                        if (stopX < startX) {//如果抬起小于按下就是像做滑动,不弹出警告
                            break;
                        }
                        if (speed > 0.6f && disX > 50) {//如果设置的速度大于0.6 弹出警告框
                            if (isCloseDialog && disX > 0) {
                                break;
                            }
                            showNewRetailDialog(getString(R.string.hint), getString(R.string.setting_exceed_speed), new NewRetailDialog.OnDialogClickListener() {
                                @Override
                                public void yes() {
                                    if (speed == 0.65f) {
                                        speed = 0.65f;
                                        process = 83;
                                        speedSetting.setValue(process);
                                    } else {
                                        speed = 0.7f;
                                        process = 100;
                                        speedSetting.setValue(process);
                                    }
                                    dismissNewRetailDialog();
                                }

                                @Override
                                public void no() {
                                    speed = mTempSpeed;
                                    process = mTempProcess;
                                    speedSetting.setValue(process);
                                    dismissNewRetailDialog();
                                }
                            });
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_speed : R.layout.activity_settings_speed;

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void onClick(View v) {
        isFinish = false;
        if (RobotManager.getInstance().getConnectState()) {
            checkConnect();
            chassis.setSpeed(speed);
            RobotManager.getInstance().addListener(new OnSpeedSetListener() {
                @Override
                public void setSpeedResult(boolean isSuccess) {
                    isFinish = true;
                    if (isSuccess) {
                        SharedPreUtil.putFloat(SharedKey.SPEED_NAME, SharedKey.SPEED_KEY, speed);
                        runOnUiThread(() -> Toast.makeText(SettingsSpeedActivity.this, R.string.speed_set_success, Toast.LENGTH_SHORT).show());
                        //底层设置速度
                    } else {
                        runOnUiThread(() -> Toast.makeText(context, R.string.set_failed, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } else {
            Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkConnect() {
        speedSetting.postDelayed(() -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinish) {
                        Toast.makeText(SettingsSpeedActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }, Constants.internalCheckLinux);
    }
}
