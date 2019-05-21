package com.csjbot.blackgaga.feature.content2;

/**
 * Created by 孙秀艳 on 2018/1/24.
 */

public class Content2Presenter implements Content2Contract.Presenter{

    Content2Contract.View view;

    @Override
    public Content2Contract.View getView() {
        return view;
    }

    @Override
    public void initView(Content2Contract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            this.view = null;
        }
    }
}
