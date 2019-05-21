package com.csjbot.blackgaga.feature.product.order;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;
import com.csjbot.blackgaga.model.http.bean.PayQrCodeBean;

/**
 * Created by 孙秀艳 on 2017/10/25.
 */

public class OrderContract {

    interface Presenter extends BasePresenter<View> {
        void getCodeUrl();
        void getPayResult(String orderid);//获取支付结果
        void cancelSearch();//取消轮询

        void pullPayQrCodeBean();
    }

    interface View extends BaseView {
        void showCode(String codeUrl);
        String getStartTime();
        int getOrderCount();
        void showLoading();
        void hideLoading();
        void paySuccess();
        void payError(String msg);
        void getPayQrCodeBean(PayQrCodeBean bean);
    }
}
