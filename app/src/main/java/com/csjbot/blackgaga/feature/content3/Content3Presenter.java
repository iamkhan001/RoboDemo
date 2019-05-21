package com.csjbot.blackgaga.feature.content3;

/**
 * Created by 孙秀艳 on 2018/2/1.
 */

public class Content3Presenter implements Content3Contract.Presenter{

    Content3Contract.View view;

    @Override
    public Content3Contract.View getView() {
        return view;
    }

    @Override
    public void initView(Content3Contract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            this.view = null;
        }
    }
}
