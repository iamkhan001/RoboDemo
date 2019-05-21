package com.csjbot.blackgaga.feature.settings.charge_setting;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 *
 * update_person:jingwc
 * update_time : 2017/12/26.
 */

public interface ChargeSettingContract {

    interface presenter extends BasePresenter<view> {

        /** 设置是否自动充电 */
        void setAutoCharging(boolean b);

        /** 设置是否使用充电桩 */
        void setChargingPile(boolean b);

        /** 获取是否自动充电 */
        boolean getAutoCharging();

        /** 获取是否使用充电桩 */
        boolean getChargingPile();

        /** 保存所有信息到本地 */
        void save();

        /** 获取设置的低电量回充 */
        int getLowElectricity();

        /** 设置低电量回充*/
        void setLowElectricity(int electricity);
    }

    interface view extends BaseView {
        /** 保存成功*/
        void saveShowDialog();
    }
}
