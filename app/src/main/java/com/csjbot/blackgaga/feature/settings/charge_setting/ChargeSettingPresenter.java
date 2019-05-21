package com.csjbot.blackgaga.feature.settings.charge_setting;

import android.content.Context;

import com.csjbot.blackgaga.global.Constants;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 *
 * update_person:jingwc
 * update_time : 2017/12/26.
 */

public class ChargeSettingPresenter implements ChargeSettingContract.presenter {

    private Context context;

    private ChargeSettingContract.view view;

    private int electricity;

    private boolean isAutoCharging;

    private boolean isChargingPile;

    public ChargeSettingPresenter(Context context) {
        this.context = context;
        this.electricity = Constants.Charging.getLowElectricity();
    }

    @Override
    public ChargeSettingContract.view getView() {
        return view;
    }

    @Override
    public void initView(ChargeSettingContract.view view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

    @Override
    public int getLowElectricity() {
        return electricity;
    }

    @Override
    public void setLowElectricity(int electricity) {
        this.electricity = electricity;
    }

    @Override
    public boolean getAutoCharging() {
        return Constants.Charging.getAutoCharging();
    }

    @Override
    public void setAutoCharging(boolean b) {
        this.isAutoCharging = b;
    }

    @Override
    public boolean getChargingPile() {
        return Constants.Charging.getChargingPile();
    }

    @Override
    public void setChargingPile(boolean b) {
        this.isChargingPile = b;
    }

    @Override
    public void save() {

        Constants.Charging.saveLowElectricity(electricity);
        Constants.Charging.saveAutoCharging(isAutoCharging);
        Constants.Charging.saveChargingPile(isChargingPile);

        view.saveShowDialog();
    }

}
