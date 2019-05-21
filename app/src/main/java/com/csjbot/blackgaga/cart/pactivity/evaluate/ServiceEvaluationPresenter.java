package com.csjbot.blackgaga.cart.pactivity.evaluate;

import android.content.Context;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/19.
 */

public class ServiceEvaluationPresenter implements ServiceEvaluationContract.presenter {
    /**
     * 上下文
     */
    private Context context;

    private ServiceEvaluationContract.view view;

    public ServiceEvaluationPresenter(Context context) {
        this.context = context;
    }

    @Override
    public ServiceEvaluationContract.view getView() {
        return view;
    }

    @Override
    public void initView(ServiceEvaluationContract.view view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
    }
}
