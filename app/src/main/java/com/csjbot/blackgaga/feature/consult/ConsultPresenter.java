package com.csjbot.blackgaga.feature.consult;

import com.csjbot.blackgaga.model.http.consult.IConsult;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultPresenter implements ConsultContract.Presenter {

    ConsultContract.View view;

    IConsult iConsult;

    public ConsultPresenter(){
        iConsult = ServerFactory.createConsult();
    }

    @Override
    public ConsultContract.View getView() {
        return view;
    }

    @Override
    public void initView(ConsultContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }

    @Override
    public void getConsult() {

    }
}
