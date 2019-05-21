package com.csjbot.blackgaga.model.http.qrcode;

import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.PayQrCodeBean;
import com.csjbot.blackgaga.model.http.bean.QrCodeBean;

import io.reactivex.Observer;

/**
 * Created by  Wql , 2018/3/1 18:10
 */

public class QrCodeImpl extends BaseImpl implements IQrCode {

    @Override
    public QrCodeService getRetrofit() {
        return getRetrofit(QrCodeService.class);
    }

    @Override
    public void getQrCode(String sn, Observer<QrCodeBean> ob) {
        scheduler(getRetrofit().getQrCode(sn)).subscribe(ob);
    }

    @Override
    public void getPayQrCode(String sn, Observer<PayQrCodeBean> observer) {
        scheduler(getRetrofit().getPayQrCode(sn)).subscribe(observer);
    }
}
