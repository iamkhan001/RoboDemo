package com.csjbot.blackgaga.feature.settings.charge_setting;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.event.ListenerState;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.client_req.ReqFactory;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 * 充电设置页面
 *
 *
 * update_person:jingwc
 * update_time : 2017/12/26.
 *
 */

public class ChargeSettingActivity extends BaseModuleActivity implements ChargeSettingContract.view {

    @BindView(R.id.auto_charging_switch)
    CheckBox auto_charging_switch;

    @BindView(R.id.charging_pile_switch)
    CheckBox charging_pile_switch;

    @BindView(R.id.tv_electric)
    TextView tv_electric;

    private ChargeSettingContract.presenter presenter;

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_charge_setting : R.layout.activity_charge_setting;
    }


    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        presenter = new ChargeSettingPresenter(this);
        presenter.initView(this);
        auto_charging_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            presenter.setAutoCharging(isChecked);
        });

        charging_pile_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            presenter.setChargingPile(isChecked);
        });

        auto_charging_switch.setChecked(presenter.getAutoCharging());
        charging_pile_switch.setChecked(presenter.getChargingPile());
        showElectricity();
    }


    @Override
    public boolean isOpenTitle() {
        return true;
    }

    /**
     * 保存所有电量修改信息
     */
    @OnClick(R.id.save)
    public void saveInfo(){
        // 保存修改信息
        presenter.save();
    }

    /**
     * 立即充电
     */
    @OnClick(R.id.charge)
    public void charing(){
        goHome();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    private void goHome(){
        speak(getString(R.string.go_back_recharge));
        if(presenter.getChargingPile()) {
            // 关闭导航的回调监听
            ListenerState state = new ListenerState();
            state.isOpenListener = false;
            EventBus.getDefault().post(state);
            // 回到开机点,并寻找充电桩
            ReqFactory.getChassisReqInstance().goHome();
        }else{
            // 关闭导航界面的回调监听
            ListenerState state = new ListenerState();
            state.isOpenListener = false;
            EventBus.getDefault().post(state);
            // 回到开机点,不寻找充电桩
            String json = "{" + "\"x\":0," + "\"y\":0," + "\"z\":0," + "\"rotation\":0" + "}";
            ServerFactory.getChassisInstance().navi(json);
        }
    }

    @Override
    public void saveShowDialog() {
        showNewRetailDialog(getString(R.string.charge_administration), getString(R.string.charge_save), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }

    /**
     * 增加最低充电量
     */
    @OnClick(R.id.iv_top)
    public void riseLowElectricity(){
        int electricity = presenter.getLowElectricity()+10;
        if(electricity > 50){
            Toast.makeText(ChargeSettingActivity.this,  getString(R.string.charge_too_much),Toast.LENGTH_SHORT).show();
        }else{
            presenter.setLowElectricity(electricity);
            showElectricity();
        }
    }

    private void showElectricity(){
        tv_electric.setText(presenter.getLowElectricity()+"%");
    }

    /**
     * 减少最低充电量
     */
    @OnClick(R.id.iv_bottom)
    public void reduceLowElectricity(){
        int electricity = presenter.getLowElectricity()-10;
        if(electricity < 20){
            Toast.makeText(ChargeSettingActivity.this,  getString(R.string.charge_too_ittle),Toast.LENGTH_SHORT).show();
        }else{
            presenter.setLowElectricity(electricity);
            showElectricity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerState state = new ListenerState();
        state.isOpenListener = true;
        EventBus.getDefault().post(state);
    }
}
